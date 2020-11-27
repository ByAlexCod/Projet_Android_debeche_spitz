import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.NasaObjectEntity
import com.example.nasaobjects.R

class NasaObjectAdapter (private val mObjectEntities: List<NasaObject>) : RecyclerView.Adapter<NasaObjectAdapter.ViewHolder>()
{

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.objectName)
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
        textView.setText(nasaObjectEntity.getName() + " (" + nasaObjectEntity.getYear().year + ")")
    }

    override fun getItemCount(): Int {
        return mObjectEntities.size
    }
}