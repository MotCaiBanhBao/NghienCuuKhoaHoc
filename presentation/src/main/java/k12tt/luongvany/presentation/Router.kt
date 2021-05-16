package k12tt.luongvany.presentation

import k12tt.luongvany.presentation.binding.NotificationBinding

interface Router {
    fun showLogin()
    fun showNotificationsList()
    fun showNotificationForm(notificationBinding: NotificationBinding?)
    fun showNotificationDetails(notificationBinding: NotificationBinding)
    fun back()
    fun navigationUp(): Boolean
    fun isInRootScreen(): Boolean
    fun showUserDetail()
}
