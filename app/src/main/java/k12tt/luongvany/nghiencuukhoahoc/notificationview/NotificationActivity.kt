package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import k12tt.luongvany.nghiencuukhoahoc.MyFirebaseMessagingService
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.FragmentNotificationListBinding
import k12tt.luongvany.nghiencuukhoahoc.databinding.UserDetailBinding
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.auth.Auth
import k12tt.luongvany.presentation.auth.AuthStateListener
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

const val TOPIC = "/topics/myTopic2"
class NotificationActivity: AppCompatActivity() {

    val router: Router by inject { parametersOf(this@NotificationActivity) }

    val auth: Auth<Int, Intent> by inject { parametersOf(this@NotificationActivity) }

    private val authListener: AuthStateListener =
        object : AuthStateListener {
            override fun onAuthChanged(isLoggedIn: Boolean) {
                if (!isLoggedIn) {
                    router.showLogin()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_fragment_container)

        createToken()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthChangeListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthChangeListener(authListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return router.navigationUp()
    }

    override fun onBackPressed() {
        if (router.isInRootScreen()) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                auth.handleSignInResult(data,
                    {
                        router.showNotificationsList()
                    },
                    {
                        showErrorSignIn()
                    })
            } catch (e: Exception) {
                showErrorSignIn()
            }
        }
    }



    fun startSignIn() {
        auth.startSignIn(RC_GOOGLE_SIGN_IN)
    }

    private fun showErrorSignIn() {
        Toast.makeText(this, R.string.string_error_signin, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }

    private fun createToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result
                MyFirebaseMessagingService.token = token

                val msg = getString(R.string.msg_token_fms, token)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }


}