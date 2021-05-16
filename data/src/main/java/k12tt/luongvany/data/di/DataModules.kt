package k12tt.luongvany.data.di

import k12tt.luongvany.data.mapper.NotificationMapper
import k12tt.luongvany.data.mapper.NotificationsDataMapper
import k12tt.luongvany.data.repository.NotificationsRepositoryImpl
import k12tt.luongvany.domain.repositories.NotificationRepo
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataModules {
    fun load(){
        loadKoinModules(module{
            factory<NotificationRepo>{
                NotificationsRepositoryImpl(
                    localData = get(),
                    entityMapper = NotificationMapper(),
                    dataMapper = NotificationsDataMapper()
                )
            }
        })
    }
}