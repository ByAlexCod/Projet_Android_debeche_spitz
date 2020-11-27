package com.example.nasaobjects.ui.dashboard

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nasaobjects.NasaCustomDatabase
import com.example.nasaobjects.NasaObjectEntity
import com.example.nasaobjects.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class DashboardFragment : Fragment() {

    private var CAMERA_REQUEST = 123
    private var CAMERA_PERMISSION = 1888
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var addPictureButton: Button
    private lateinit var imageBox: ImageView
    private lateinit var root: View
    private lateinit var saveButton: Button
    private lateinit var objectNameText: EditText
    private lateinit var picture: Bitmap
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("aa", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = root.context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        this.root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        addPictureButton = root.findViewById<Button>(R.id.take_picture)
        imageBox = root.findViewById(R.id.imageBox)
        addPictureButton.setOnClickListener { this.takePicture() }
        saveButton = root.findViewById(R.id.register_button_save)
        saveButton.setOnClickListener { lifecycleScope.launch{saveObject()} }
        objectNameText = root.findViewById(R.id.register_input_name)
        this.createNotificationChannel()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun saveObject() = withContext(Dispatchers.IO){
        val db = NasaCustomDatabase.getDatabase(root.context)
        db.nasaObjectDao().insertAll(
            NasaObjectEntity(
                name = objectNameText.text.toString(),
                year = LocalDate.now().toString(),
                mass = 10.0,
                picture = encodeImage(
                    picture
                )
            )
        )

        val notification: Notification = Notification.Builder(root.context, "aa")
                .setSmallIcon(R.drawable.ic_launcher_background) // drawable for API 26
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.new_metorite_registered))
                .setContentText(getString(R.string.new_metorite_registered))
                .setColor(15665).build()

        val notifManager = root.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.notify(1555, notification)
        activity?.runOnUiThread {
            Toast.makeText(root.context, "Bravo", Toast.LENGTH_LONG).show()
        }
    }

    private fun encodeImage(bm: Bitmap): String? { // convert Bitmap image to Base64 string
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun takePicture() {
        if (context?.let { checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        } else {
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show()
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            addPictureButton.text = getString(R.string.retake_picture)

            val file = File(mCurrentPhotoPath);
            val bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    requireContext().getContentResolver(), Uri.fromFile(
                        file
                    )
                )
            );
            if (bitmap != null) {
                val out = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
                val decoded: Bitmap =
                    BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))

                imageBox.setImageBitmap(decoded)
                picture = decoded;
            }
        }
    }


    var mCurrentPhotoPath: String? = null


    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = activity?.getExternalFilesDir(DIRECTORY_PICTURES)!!
        val image: File = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath()
        return image
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            // Ensure that there's a camera activity to handle the intent
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        root.context,
                        "com.example.nasaobjects",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }

    }


}