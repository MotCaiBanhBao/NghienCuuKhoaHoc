package k12tt.luongvany.domain.usecases.user

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.repositories.NotificationRepo
import k12tt.luongvany.domain.repositories.UserRepo
import kotlinx.coroutines.flow.Flow

open class GetUseNotification(private val repository: UserRepo) {
    fun execute(): Flow<List<Notification>> {
        return repository.loadNotifications()
    }
}