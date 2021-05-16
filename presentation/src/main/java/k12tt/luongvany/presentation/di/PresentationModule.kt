package k12tt.luongvany.presentation.di

import k12tt.luongvany.presentation.NotificationDetailsViewModel
import k12tt.luongvany.presentation.NotificationFormViewModel
import k12tt.luongvany.presentation.NotificationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object PresentationModule {
    fun load(){
        loadKoinModules(module {
            viewModel {
                NotificationListViewModel(get())
            }

            viewModel {
                (notificationId: String) -> NotificationDetailsViewModel(notificationId, get())
            }

            viewModel {
                NotificationFormViewModel(get())
            }
        })
    }
}