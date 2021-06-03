package k12tt.luongvany.data.repository

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.NotificationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class NotificationsRepositoryImpl (private val fbData: FBData,
                                            private val entityMapper: Mapper<NotificationData, Notification>,
                                            private val topicMapper: Mapper<Topics, TopicsData>,
                                            private val dataMapper: Mapper<Notification, NotificationData>): NotificationRepo{
    override fun loadNotifications(): Flow<List<Notification>> {
        return fbData.loadNotifications().map { it.map(entityMapper::map) }
    }

    override fun loadNotification(notificationId: String): Flow<Notification?> {
        return fbData.loadNotification(notificationId).map { it?.let(entityMapper::map) }
    }

    override suspend fun pushNotification(notification: Notification, topics: List<Topics>) {
        return fbData.pushNotification(dataMapper.map(notification), topics.map(topicMapper::map))
    }
}