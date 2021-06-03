package k12tt.luongvany.presentation

import android.content.Context
import k12tt.luongvany.presentation.binding.notification.NotificationBinding

interface Router {
    fun showLogin()
    fun showNotificationsList()
    fun showNotificationForm(notificationBinding: NotificationBinding?)
    fun showNotificationDetails(notificationBinding: NotificationBinding)
    fun back()
    fun navigationUp(): Boolean
    fun isInRootScreen(): Boolean
    fun showUserDetail()
    fun showLoginWithAccount()
    fun hideAppBarAndBottom(id: Int)
    fun showAppBarAndBottom(id: Int)
    fun showSlideBar(context: Context)
    fun showView()
}
