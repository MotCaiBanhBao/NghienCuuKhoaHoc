package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.binding.RecyclerViewBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.ItemNotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding

class NotificationAdapter(private val onClick: (NotificationBinding) -> Unit, val context: NotificationListFragment) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>(),
    RecyclerViewBinding.BindableAdapter<List<NotificationBinding>>, Filterable{

    private var notifications: List<NotificationBinding>? = null
    private var list: List<NotificationBinding>? = null
    private var chipChoose = "TATCA"

    override fun setData(data: List<NotificationBinding>?) {
        notifications = data
        list = notifications
        filter.filter(chipChoose)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.apply {
            notifications?.get(position)?.let { currentNotification ->
                notification = currentNotification
                executePendingBindings()
                root.setOnClickListener {
                    onClick(currentNotification)
                }
                root.findViewById<ImageButton>(R.id.share_notification).setOnClickListener{
                    shareNotification(currentNotification)
                }
            }
        }
    }
    override fun getItemCount() = notifications?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ItemNotificationBinding>(view)
    }

    private fun shareNotification(notification: NotificationBinding){
        Log.d("TEST", "Share co chay")
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.apply {
            putExtra(
                Intent.EXTRA_TITLE, notification.title
            )
            putExtra(
                Intent.EXTRA_TEXT, """${notification.title} 
                    |
                    |${notification.content}
                    |${notification.url}
                """.trimMargin()
            )
        }
        val shareIntent = Intent.createChooser(sharingIntent, null)

        context.startActivity(shareIntent)
    }

    override fun getFilter(): Filter {
        return textFilter
    }


    private val textFilter = object : Filter() {
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            notifications = results.values as List<NotificationBinding>?
            notifyDataSetChanged()
        }

        override fun performFiltering(constraint: CharSequence): FilterResults {
            var filteredResults: List<NotificationBinding>? = null
            chipChoose = constraint.toString()
            filteredResults = if(chipChoose == "TATCA") {
                notifications = list
                notifications
            } else {
                notifications = list
                getFilteredResults(chipChoose)
            }
            val results = FilterResults()
            results.values = filteredResults
            return results
        }

        private fun getFilteredResults(constraint: String): List<NotificationBinding> {
            val results: MutableList<NotificationBinding> = ArrayList()
            for (item in notifications!!) {

                if (NotificationTypeBinding.valueOf(constraint) == item.notificationType){
                    Log.d("TEST", "FILEFE")
                    results.add(item)
                }
            }
            return results
        }
    }
}