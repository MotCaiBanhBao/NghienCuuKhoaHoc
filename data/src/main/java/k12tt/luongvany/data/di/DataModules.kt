package k12tt.luongvany.data.di

import k12tt.luongvany.data.mapper.notification.NotificationMapper
import k12tt.luongvany.data.mapper.notification.NotificationsDataMapper
import k12tt.luongvany.data.mapper.topic.TopicDataMapper
import k12tt.luongvany.data.mapper.topic.TopicMapper
import k12tt.luongvany.data.mapper.user.UserDataMapper
import k12tt.luongvany.data.mapper.user.UserMapper
import k12tt.luongvany.data.repository.NotificationsRepositoryImpl
import k12tt.luongvany.data.repository.TopicRepositoryImpl
import k12tt.luongvany.data.repository.UserRepositoryImpl
import k12tt.luongvany.domain.repositories.NotificationRepo
import k12tt.luongvany.domain.repositories.TopicRepo
import k12tt.luongvany.domain.repositories.UserRepo
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModules {
    fun load(){
        loadKoinModules(module{
            factory<NotificationRepo>{
                NotificationsRepositoryImpl(
                    fbData = get(),
                    entityMapper = NotificationMapper(),
                    dataMapper = NotificationsDataMapper(),
                    topicMapper = TopicDataMapper()
                )
            }
            factory<UserRepo> {
                UserRepositoryImpl(fbData = get(),
                    entityMapper = UserMapper(),
                    dataMapper = UserDataMapper(),
                    dataMapperTopic = TopicDataMapper(),
                    dataEntityTopic = TopicMapper(),
                    notificationEntity = NotificationMapper()
                )
            }
            factory<TopicRepo> {
                TopicRepositoryImpl(fbData = get(),
                    entityMapper = TopicMapper(),
                    dataMapper = TopicDataMapper()
                )
            }
        }
        )
    }
}