package k12tt.luongvany.domain.di

import k12tt.luongvany.domain.usecases.message.GetListMessageUseCase
import k12tt.luongvany.domain.usecases.message.PostMessageUseCase
import k12tt.luongvany.domain.usecases.notification.ListNotificationsUseCase
import k12tt.luongvany.domain.usecases.notification.PushNotificationUseCase
import k12tt.luongvany.domain.usecases.notification.ViewNotificationDetailUseCase
import k12tt.luongvany.domain.usecases.topic.ChangeTopicUseCase
import k12tt.luongvany.domain.usecases.topic.GetTopicUseCase
import k12tt.luongvany.domain.usecases.user.*
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModules {
    fun load(){
        loadKoinModules(module = module {
            factory { ListNotificationsUseCase(repository = get()) }
            factory { PushNotificationUseCase(repository = get()) }
            factory { ViewNotificationDetailUseCase(repository = get()) }

            factory { ReSubcribeUseCase(repository = get()) }
            factory { GetUserUseCase(repo = get()) }
            factory { GetUserTopics(repo = get()) }
            factory { GetUseNotification(repository = get()) }
            factory { UnsubcribeUseCase(repository = get()) }
            factory { ChangeTopicUseCase(repo = get()) }
            factory { GetTopicUseCase(repo = get()) }
            factory { GetListMessageUseCase(repository = get()) }
            factory { PostMessageUseCase(repository = get()) }
        })
    }
}