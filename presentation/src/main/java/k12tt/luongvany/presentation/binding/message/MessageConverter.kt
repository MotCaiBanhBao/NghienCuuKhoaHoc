package k12tt.luongvany.presentation.binding.message

import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding


object MessageConverter{
    fun fromData(message: Message): MessageBinding {
        return MessageBinding().apply {
            idMessage = message.id
            timestampMessage = message.timestamp.toString()
            userNameMessage = message.userName
            userUidMessage = message.userUid
            contextMessage = message.context
            photoUrlMessage = message.photoUrl
        }
    }

    fun toData(messageBinding: MessageBinding): Message {
        return Message(
            id = messageBinding.idMessage,
            userName = messageBinding.userNameMessage,
            userUid = messageBinding.userUidMessage,
            context = messageBinding.contextMessage,
            photoUrl = messageBinding.contextMessage
        )
    }

}
