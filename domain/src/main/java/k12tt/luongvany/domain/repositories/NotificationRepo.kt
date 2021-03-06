package k12tt.luongvany.domain.repositories

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.Topics
import kotlinx.coroutines.flow.Flow

interface NotificationRepo {
    fun loadNotifications(): Flow<List<Notification>>
    fun loadNotification(notificationId: String): Flow<Notification?>
    suspend fun pushNotification(notification: Notification, topics: List<Topics>)
}