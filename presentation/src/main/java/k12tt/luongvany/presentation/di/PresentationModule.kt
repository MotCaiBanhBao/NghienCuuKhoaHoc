package k12tt.luongvany.presentation.di

import k12tt.luongvany.presentation.viewmodel.notification.NotificationDetailsViewModel
import k12tt.luongvany.presentation.viewmodel.notification.NotificationFormViewModel
import k12tt.luongvany.presentation.viewmodel.notification.NotificationListViewModel
import k12tt.luongvany.presentation.viewmodel.topic.TopicsViewModel
import k12tt.luongvany.presentation.viewmodel.user.MainActivityViewModel
import k12tt.luongvany.presentation.viewmodel.user.UserDetailViewModel
import k12tt.luongvany.presentation.viewmodel.user.UserNotificationViewModel
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
                (notificationId: String) -> NotificationDetailsViewModel(notificationId, get(), get(), get())
            }

            viewModel {
                NotificationFormViewModel(get(), get())
            }
            viewModel {
                TopicsViewModel(get(), get(), get())
            }

            viewModel {
                MainActivityViewModel(get())
            }
            viewModel {
                    (userId: String) -> UserDetailViewModel(userId, get())
            }
            viewModel {
                UserNotificationViewModel(get())
            }
        })
    }
}