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
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nasaobjects.NasaCustomDatabase
import com.example.nasaobjects.NasaObjectEntity
import com.example.nasaobjects.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


class DashboardFragment : Fragment() {

    private var CAMERA_REQUEST = 123
    private var CAMERA_PERMISSION = 1888
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var addPictureButton: Button
    private lateinit var imageBox: ImageView
    private lateinit var root: View
    private lateinit var saveButton: Button
    private lateinit var objectNameText: EditText
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
        db.nasaObjectDao().insertAll(NasaObjectEntity(name = objectNameText.text.toString(), year = LocalDate.now().toString(), mass = 10.0))

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


    private fun takePicture() {
        if (context?.let { checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
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
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            imageBox.setImageBitmap(data?.getExtras()?.get("data") as Bitmap?)
            addPictureButton.text = getString(R.string.retake_picture)
        }
    }

}