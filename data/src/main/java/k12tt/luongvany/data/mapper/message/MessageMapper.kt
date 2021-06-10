package k12tt.luongvany.data.mapper.message

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.message.MessageData
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.notification.NotificationTypeData
import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType

class MessageMapper: Mapper<MessageData, Message> {
    override fun map(source: MessageData): Message {
        return Message(
            id = source.id,
            timestamp = source.timestamp,
            userName = source.userName,
            userUid = source.userUid,
            context = source.context,
            photoUrl = source.photoUrl
        )
    }
}