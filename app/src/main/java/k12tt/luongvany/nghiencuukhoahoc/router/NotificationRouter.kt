package k12tt.luongvany.nghiencuukhoahoc.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.notificationview.NotificationDetailsFragment
import k12tt.luongvany.nghiencuukhoahoc.notificationview.SlideBar
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.binding.notification.NotificationBinding

class NotificationRouter(val activity: FragmentActivity): Router {
    private val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    private val parentAppBarLayout = activity.findViewById<AppBarLayout>(R.id.appBarLayout)
    private val rootLayout = activity.findViewById<DrawerLayout>(R.id.drawer_layout)

    private val navController: NavController by lazy {
        Navigation.findNavController(activity, R.id.nav_host_container)
    }

    private val rootScreens = setOf(R.id.signInFragment, R.id.listNotification)


    override fun showLogin() {
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(R.id.signInFragment, false)
            .build()
        navController.navigate(R.id.signInFragment, null, options)
    }

    override fun showNotificationsList() {
        showAppBarAndBottom(SHOW_ALL)
        navController.navigate(R.id.listNotification)
    }


    override fun showNotificationForm(notificationBinding: NotificationBinding?) {
        val navOptions = NavOptions.Builder().apply {
            setEnterAnim(R.anim.slide_in_left)
            setExitAnim(R.anim.slide_out_left)
            setPopEnterAnim(R.anim.slide_in_right)
            setPopExitAnim(R.anim.slide_out_right)
        }.build()

        val args = notificationBinding?.let {
            Bundle().apply {
                putParcelable("notification", it)
            }
        }

        navController.navigate(R.id.formNotification, args, navOptions)
    }

    override fun showNotificationDetails(notificationBinding: NotificationBinding) {
        val args = Bundle().apply {
            putParcelable("notification", notificationBinding)
        }
        NotificationDetailsFragment().apply {
            arguments = args
        }.show(activity.supportFragmentManager, NotificationDetailsFragment.TAG)

    }

    override fun back() {
        navController.popBackStack()
    }

    override fun navigationUp(): Boolean {
        return navController.navigateUp()
    }

    override fun isInRootScreen(): Boolean {
        return rootScreens.contains(navController.currentDestination?.id)
    }

    override fun showUserDetail() {
        val navOptions = NavOptions.Builder().apply {
            setEnterAnim(R.anim.slide_in_left)
            setExitAnim(R.anim.slide_out_left)
            setPopEnterAnim(R.anim.slide_in_right)
            setPopExitAnim(R.anim.slide_out_right)
        }.build()

        navController.navigate(R.id.userDetailFragment, null, navOptions)
    }

    override fun showLoginWithAccount() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.signInFragment, false)
            .build()

        navController.navigate(R.id.loginWithAccountFragment, null, options)
    }

     override fun hideAppBarAndBottom(id: Int){
        when(id){
            HIDE_BOT -> hideBot()
            HIDE_TOP -> hideAppBar()
            else -> {
                hideBot()
                hideAppBar()
            }
        }

    }

    override fun showAppBarAndBottom(id: Int){
        when(id){
            SHOW_BOT -> showBot()
            SHOW_TOP -> showAppBar()
            else -> {
                showBot()
                showAppBar()
            }
        }

    }

    override fun showSlideBar(context: Context) {
        context.startActivity(Intent(context, SlideBar::class.java))
    }

    override fun showView() {
        rootLayout.visibility = View.VISIBLE
    }


    private fun hideBot(){
        if (bottomNavigationView.isVisible){
            bottomNavigationView.visibility = View.GONE
        }
    }

    private fun hideAppBar(){
        parentAppBarLayout.setExpanded(false, false)
    }

    private fun showBot(){
        if (!bottomNavigationView.isVisible){
            bottomNavigationView.visibility = View.VISIBLE
        }
    }

    private fun showAppBar(){
            parentAppBarLayout.setExpanded(true, false)
    }

   companion object{
       const val HIDE_TOP = 1
       const val HIDE_BOT = 2
       const val HIDE_ALL = 3
       const val SHOW_TOP = 1
       const val SHOW_BOT = 2
       const val SHOW_ALL = 3
    }
}