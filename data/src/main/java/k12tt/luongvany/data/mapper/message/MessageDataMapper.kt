package k12tt.luongvany.data.mapper.message

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.message.MessageData
import k12tt.luongvany.domain.entities.Message

class MessageDataMapper: Mapper<Message, MessageData> {
    override fun map(source: Message): MessageData {
        return MessageData(
            id = source.id,
            timestamp = source.timestamp,
            userName = source.userName,
            userUid = source.userUid,
            context = source.context,
            photoUrl = source.photoUrl
        )
    }
}