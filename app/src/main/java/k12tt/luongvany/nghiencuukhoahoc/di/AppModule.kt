package k12tt.luongvany.nghiencuukhoahoc.di

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import k12tt.luongvany.nghiencuukhoahoc.auth.FirebaseSignIn
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.auth.Auth
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object AppModule {

    fun load() {
        loadKoinModules(module {
            factory { (activity: FragmentActivity) ->
                NotificationRouter(activity) as Router
            }

            factory { (activity: FragmentActivity) ->
                FirebaseSignIn(activity) as Auth<Int, Intent>
            }
        })
    }
}
