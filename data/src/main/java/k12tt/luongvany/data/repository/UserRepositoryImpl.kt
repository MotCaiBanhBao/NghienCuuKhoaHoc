package k12tt.luongvany.data.repository

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.data.model.user.UserData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.entities.User
import k12tt.luongvany.domain.repositories.UserRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserRepositoryImpl (private val fbData: FBData,
                                            private val entityMapper: Mapper<UserData, User>,
                                            private val dataMapper: Mapper<User, UserData>,
                                    private val dataMapperTopic: Mapper<Topics, TopicsData>,
                                    private val dataEntityTopic: Mapper<TopicsData, Topics>,
                                   private val notificationEntity: Mapper<NotificationData, Notification>
): UserRepo {
    override fun getUserTopics(): Flow<List<Topics>> {
        return fbData.getUserTopics().map{it.map(dataEntityTopic::map)}
    }

    override fun loadNotifications(): Flow<List<Notification>> {
        return fbData.getUserNotification().map { it.map(notificationEntity::map) }
    }

    override suspend fun reSubcribe() {
        fbData.reSubcribe()
    }


    override fun getUser(userId: String): Flow<User?> {
        return fbData.getUser(userId).map { it?.let(entityMapper::map)}
    }

}