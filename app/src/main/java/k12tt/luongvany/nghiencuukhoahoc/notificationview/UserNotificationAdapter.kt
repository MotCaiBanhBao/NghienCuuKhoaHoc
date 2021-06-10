package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.binding.RecyclerViewBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.ItemNotificationBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.MyNotifiactionItemBinding
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationConverter
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
            .inflate(R.layout.my_notifiaction_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding?.apply {
            notifications?.get(position)?.let { currentNotification ->
                notification = currentNotification
                executePendingBindings()
                root.setOnClickListener {
                    if (!currentNotification.checked){
                        currentNotification.checked = true
                        val fbAuth = FirebaseAuth.getInstance()
                        val fireStore = FirebaseFirestore.getInstance()
                        val db = fireStore
                        val collection = db.collection("users").document(fbAuth.currentUser.uid).collection("notifications")
                        collection.document(currentNotification.id).set(hashMapOf("checked" to true), SetOptions.merge())
                    }
                    onClick(currentNotification)
                }
            }
        }
    }
    override fun getItemCount() = notifications?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<MyNotifiactionItemBinding>(view)
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