package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.binding.RecyclerViewBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.AnotherUserChatMessageBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.PersonalChatMessageBinding
import k12tt.luongvany.presentation.binding.message.MessageBinding
import k12tt.luongvany.presentation.utils.dateMinusTo
import java.util.*


class MessageAdapter: RecyclerView.Adapter<MessageAdapter.ViewHolder>(),
        RecyclerViewBinding.BindableAdapter<List<MessageBinding>> {
    private var messages: List<MessageBinding>? = null

    fun updateTimes(){
        messages?.let {
            for (i in 0 until  messages!!.size){
                val message = messages!![i]
                val updateTime = message.timestampMessage.toLong().dateMinusTo(Date().time)

                if(message.hourOfMessage != updateTime){
                    message.hourOfMessage = updateTime
                    notifyItemChanged(i)
                }
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.personal_chat_message, parent, false)
            ViewHolder(view, VIEW_TYPE_MESSAGE_SENT)
        } else{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.another_user_chat_message, parent, false)
            ViewHolder(view, VIEW_TYPE_MESSAGE_RECEIVED)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val message = messages?.get(position)
        if (message != null) {
            if(message.userUidMessage == currentUser.uid)
                return VIEW_TYPE_MESSAGE_SENT
        }
        return VIEW_TYPE_MESSAGE_RECEIVED
    }

    override fun getItemCount() = messages?.size ?: 0

    override fun setData(data: List<MessageBinding>?) {
        messages = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, kind: Int) : RecyclerView.ViewHolder(itemView) {
        var bindingSent: PersonalChatMessageBinding? = null
        var bindingRecevied: AnotherUserChatMessageBinding? = null

        init {
            Log.d("TEST", kind.toString())
            when(kind){
                VIEW_TYPE_MESSAGE_SENT -> {
                    bindingSent = DataBindingUtil.bind<PersonalChatMessageBinding>(itemView)
                }
                VIEW_TYPE_MESSAGE_RECEIVED -> {
                    bindingRecevied = DataBindingUtil.bind<AnotherUserChatMessageBinding>(itemView)
                }
            }
        }
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(holder.itemViewType){
            VIEW_TYPE_MESSAGE_SENT -> {
                holder.bindingSent?.apply {
                    messages?.get(position)?.let { Log.d("TEST", it.contextMessage) }
                    messages?.get(position).let { currentMessage ->
                        message = currentMessage
                        executePendingBindings()
                    }
                }
            }

            VIEW_TYPE_MESSAGE_RECEIVED -> {
                holder.bindingRecevied?.apply {
                    messages?.get(position).let { currentMessage ->
                        if (currentMessage != null) {
                            Picasso.get().load(currentMessage.photoUrlMessage).fit().centerCrop().into(this.imageGchatProfileOther)
                        }

                        message = currentMessage
                        executePendingBindings()
                    }
                }
            }
        }
    }

    companion object{
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}