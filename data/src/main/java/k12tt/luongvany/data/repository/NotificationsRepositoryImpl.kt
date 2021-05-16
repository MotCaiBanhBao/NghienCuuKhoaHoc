package k12tt.luongvany.data.repository

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.NotificationData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.repositories.NotificationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class NotificationsRepositoryImpl (private val localData: FBData,
                                            private val entityMapper: Mapper<NotificationData, Notification>,
                                            private val dataMapper: Mapper<Notification, NotificationData>): NotificationRepo{
    override fun loadNotifications(): Flow<List<Notification>> {
        return localData.loadNotifications().map { it.map(entityMapper::map) }
    }

    override fun loadNotification(notificationId: String): Flow<Notification?> {
        return localData.loadNotification(notificationId).map { it?.let(entityMapper::map) }
    }

    override suspend fun pushNotification(notification: Notification) {
        return localData.pushNotification(dataMapper.map(notification))
    }
}