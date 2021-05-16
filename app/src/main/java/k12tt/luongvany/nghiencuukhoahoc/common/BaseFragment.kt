package k12tt.luongvany.nghiencuukhoahoc.common

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.notificationview.NotificationActivity
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.auth.Auth

abstract class BaseFragment : Fragment() {
    val auth: Auth<Int, Intent>
        get() = (activity as MainActivity).auth

    val router: Router
        get() = (activity as MainActivity).router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}