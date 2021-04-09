package k12tt.luongvany.nghiencuukhoahoc.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import k12tt.luongvany.nghiencuukhoahoc.R

class NotificationList : Fragment(){
    private lateinit var parentAppBarLayout: AppBarLayout
    private lateinit var parentBottomNavigation: BottomNavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parentAppBarLayout = activity?.findViewById<AppBarLayout>(R.id.appBarLayout)
        val parentBottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val view = inflater.inflate(R.layout.list_notification, container, false)

        val adapter = Adapter(Array(10) { "Notification ${it + 1}" }, parentAppBarLayout, parentBottomNavigation)

        view.findViewById<RecyclerView>(R.id.notification_list).run {
            setHasFixedSize(true)
            this.adapter = adapter
        }

        return view
    }

    companion object{
        fun newInstance() = NotificationList()
    }

}

class Adapter(
    private val testValue: Array<String>,
    private val parentAppBarLayout: AppBarLayout?,
    private val bottomNavigationView: BottomNavigationView?): RecyclerView.Adapter<Adapter.ViewHolder>(){

    class ViewHolder(val item: View): RecyclerView.ViewHolder(item)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.findViewById<TextView>(R.id.notification_item_title).text = testValue[position]
        holder.item.findViewById<TextView>(R.id.notification_item_content).text = "Text"
        holder.item.findViewById<TextView>(R.id.notification_item_date).text = "20/10/2000"

        holder.item.setOnClickListener{
            val bundle = bundleOf(KEY to testValue[position])
            parentAppBarLayout?.setExpanded(true)
            bottomNavigationView?.let {
               it.visibility = View.GONE

            }

            holder.item.findNavController().navigate(R.id.action_notification_recylceview_to_notification_detail, bundle)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notification_list_view_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount() = testValue.size

    companion object{
        const val KEY="NotificationKey"
    }
}