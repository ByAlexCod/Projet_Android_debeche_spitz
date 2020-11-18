import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.R
import java.util.*


class NasaObjectAdapter(context: Context?, users: ArrayList<NasaObject>?) :
    ArrayAdapter<NasaObject>(context!!, 0, users!!) {

    override public fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var convertView: View? = convertView
        val user: NasaObject? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                R.layout.list_elem_nasa,
                parent,
                false
            )
        }
        val objectName = convertView!!.findViewById<View>(R.id.objectName) as TextView
        objectName.text = user!!.getName()
        return convertView
    }
}