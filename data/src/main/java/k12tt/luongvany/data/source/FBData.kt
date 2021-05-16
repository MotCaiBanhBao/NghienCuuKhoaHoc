package k12tt.luongvany.data.source

import k12tt.luongvany.data.model.NotificationData
import k12tt.luongvany.domain.entities.Notification
import kotlinx.coroutines.flow.Flow

interface FBData {
    fun loadNotifications(): Flow<List<NotificationData>>
    fun loadNotification(notificationId: String): Flow<NotificationData?>
    suspend fun pushNotification(notification: NotificationData)
}