package k12tt.luongvany.data.repository

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.message.MessageData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.repositories.MessageRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MessageRepositoryImpl(private val fbData: FBData,
                                     private val entityMapper: Mapper<MessageData, Message>,
                                     private val dataMapper: Mapper<Message, MessageData>,
                                     ): MessageRepo {
    override fun loadMessage(notificationId: String): Flow<List<Message>> {
        return fbData.loadMessage(notificationId).map { it.map(entityMapper::map) }
    }

    override suspend fun pushMessage(message: Message, notificationId: String) {
        fbData.pushMessage(dataMapper.map(message), notificationId)
    }
}
