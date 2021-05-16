package k12tt.luongvany.domain.di

import k12tt.luongvany.domain.usecases.notification.ListNotificationsUseCase
import k12tt.luongvany.domain.usecases.notification.PushNotification
import k12tt.luongvany.domain.usecases.notification.ViewNotificationDetailUseCase
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DomainModules {
    fun load(){
        loadKoinModules(module = module {
            factory { ListNotificationsUseCase(repository = get()) }
            factory { PushNotification(repository = get()) }
            factory { ViewNotificationDetailUseCase(repository = get()) }
        })
    }
}