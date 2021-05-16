package k12tt.luongvany.domain.usecases.notification

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.repositories.NotificationRepo
import kotlinx.coroutines.flow.Flow

open class ViewNotificationDetailUseCase(private val repository: NotificationRepo) {
    fun execute(notificationId: String): Flow<Notification?> {
        return repository.loadNotification(notificationId)
    }
}