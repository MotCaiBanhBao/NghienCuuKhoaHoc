package k12tt.luongvany.data.source

import k12tt.luongvany.data.model.message.MessageData
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.data.model.user.UserData
import k12tt.luongvany.domain.entities.Message
import k12tt.luongvany.domain.entities.Topics
import kotlinx.coroutines.flow.Flow

interface FBData {
    fun loadNotifications(): Flow<List<NotificationData>>
    fun loadNotification(notificationId: String): Flow<NotificationData?>
    suspend fun pushNotification(notification: NotificationData, topics: List<TopicsData>)
    suspend fun initUser(topics: List<TopicsData>)
    fun getUser(userId: String): Flow<UserData?>
    fun getTopic(): Flow<List<TopicsData>>
    fun getUserTopics():Flow<List<TopicsData>>
    fun getUserNotification(): Flow<List<NotificationData>>
    suspend fun reSubcribe()
    suspend fun unSubcribeAll()
    suspend fun changeTopic(topics: List<TopicsData>, oldTopics: List<TopicsData>)
    fun loadMessage(notificationId: String): Flow<List<MessageData>>
    suspend fun pushMessage(message: MessageData, notificationId: String)
}