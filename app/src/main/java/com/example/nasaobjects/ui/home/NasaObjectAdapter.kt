import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.R

class NasaObjectAdapter (private val mObjects: List<NasaObject>) : RecyclerView.Adapter<NasaObjectAdapter.ViewHolder>()
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

    override fun onBindViewHolder(viewHolder: NasaObjectAdapter.ViewHolder, position: Int) {
        val nasaObject: NasaObject = mObjects.get(position)
        val textView = viewHolder.nameTextView
        textView.setText(nasaObject.getName() + " (" + nasaObject.getYear().year + ")")
    }

    override fun getItemCount(): Int {
        return mObjects.size
    }
}