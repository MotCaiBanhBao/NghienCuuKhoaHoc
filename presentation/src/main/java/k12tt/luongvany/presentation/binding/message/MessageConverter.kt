package k12tt.luongvany.presentation.binding.message

import android.annotation.SuppressLint
import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding
import k12tt.luongvany.presentation.utils.dateMinusTo
import java.text.SimpleDateFormat
import java.util.*


object MessageConverter{
    @SuppressLint("SimpleDateFormat")
    fun fromData(message: Message): MessageBinding {
        return MessageBinding().apply {
            idMessage = message.id
            timestampMessage = message.timestamp.time.toString()
            userNameMessage = message.userName
            userUidMessage = message.userUid
            contextMessage = message.context
            photoUrlMessage = message.photoUrl
            hourOfMessage = message.timestamp.time.dateMinusTo(Date().time)
        }
    }

    fun toData(messageBinding: MessageBinding): Message {
        return Message(
            id = messageBinding.idMessage,
            userName = messageBinding.userNameMessage,
            userUid = messageBinding.userUidMessage,
            context = messageBinding.contextMessage,
            photoUrl = messageBinding.photoUrlMessage
        )
    }

}
