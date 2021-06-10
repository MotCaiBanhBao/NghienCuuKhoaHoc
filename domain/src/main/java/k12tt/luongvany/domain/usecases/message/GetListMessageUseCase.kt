package k12tt.luongvany.domain.usecases.message

import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.repositories.MessageRepo
import kotlinx.coroutines.flow.Flow

open class GetListMessageUseCase(private val repository: MessageRepo) {
    fun execute(notificationId: String): Flow<List<Message>> {
        return repository.loadMessage(notificationId)
    }
}