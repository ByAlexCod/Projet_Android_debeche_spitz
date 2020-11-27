import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.R
import com.example.nasaobjects.ui.dashboard.DashboardFragment
import kotlin.coroutines.coroutineContext


class NasaObjectAdapter(private val mObjectEntities: List<NasaObject>, private val context: Context) : RecyclerView.Adapter<NasaObjectAdapter.ViewHolder>()
{

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.objectName)
        val getImageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NasaObjectAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val nasaObjectView = inflater.inflate(R.layout.list_elem_nasa, parent, false)

        return ViewHolder(nasaObjectView)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: NasaObjectAdapter.ViewHolder, position: Int) {

        val nasaObjectEntity: NasaObject = mObjectEntities.get(position)
        val textView = viewHolder.nameTextView
        val imageView = viewHolder.getImageView
        textView.setText(nasaObjectEntity.getName() + " (" + nasaObjectEntity.getYear().year + ")")
        if(nasaObjectEntity.getPicture() !== null) {
            imageView.setImageBitmap(nasaObjectEntity.getPicture())
            (imageView.parent as ConstraintLayout).setOnClickListener {
                val image: Bitmap = nasaObjectEntity.getPicture()!!
                imageView.post {
                    val builder = Dialog(context)
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    builder.getWindow()?.setBackgroundDrawable(
                            ColorDrawable(Color.TRANSPARENT))

                    val imagePopup = ImageView(context)
                    imagePopup.setImageBitmap(image)
                    builder.addContentView(imagePopup, RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT))
                    builder.show()
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return mObjectEntities.size
    }
}