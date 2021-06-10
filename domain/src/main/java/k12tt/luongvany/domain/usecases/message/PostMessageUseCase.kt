package k12tt.luongvany.domain.usecases.message

import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.MessageRepo
import kotlinx.coroutines.flow.Flow

open class PostMessageUseCase(private val repository: MessageRepo) {
    suspend fun execute(message: Message, notificationId: String) {
        return repository.pushMessage(message, notificationId)
    }
}