package k12tt.luongvany.domain.repositories

import k12tt.luongvany.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepo {
    fun loadMessage(notificationId: String): Flow<List<Message>>
    suspend fun pushMessage(message: Message, notificationId: String)
}