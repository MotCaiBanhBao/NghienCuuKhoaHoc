package k12tt.luongvany.domain.usecases.notification

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType
import k12tt.luongvany.domain.repositories.NotificationRepo
import kotlinx.coroutines.flow.Flow

open class ListNotificationsUseCase(private val repository: NotificationRepo) {
    fun execute(): Flow<List<Notification>>{
        return repository.loadNotifications()
    }
}