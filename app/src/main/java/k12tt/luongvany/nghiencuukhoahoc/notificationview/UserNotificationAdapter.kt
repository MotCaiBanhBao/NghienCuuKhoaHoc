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

class UserNotificationAdapter(
    private val onClick: (NotificationBinding) -> Unit, val context: UserNotificationListFragment) :
    RecyclerView.Adapter<UserNotificationAdapter.ViewHolder>(),
    RecyclerViewBinding.BindableAdapter<List<NotificationBinding>> {

    private var notifications: List<NotificationBinding>? = null

    override fun setData(data: List<NotificationBinding>?) {
        notifications = data
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
}