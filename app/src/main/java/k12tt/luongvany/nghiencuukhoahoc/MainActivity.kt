package k12tt.luongvany.nghiencuukhoahoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding

private const val TAG = "TEST"
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST", "Main")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        if (FirebaseAuth.getInstance().currentUser == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_app_logo)
                    .setTheme(R.style.LoginTheme)
                    .setAvailableProviders(providers).build(), 1
            )
        }
        if (!checkGooglePlayServices()) {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
        } else {
            createToken()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.list_notification)

        val controller = binding.bottomNavigationView.setupWithNavController(navGraphIds = navGraphIds,
                fragmentManager =  supportFragmentManager,
                containerId =  R.id.nav_host_container,
                intent = intent)

        controller.observe(this, Observer { navController->
            binding.topAppBar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        })

        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun checkGooglePlayServices(): Boolean{
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        return if (status != ConnectionResult.SUCCESS){
            Log.d(TAG, "ERROR")
            false
        }else{
            Log.i(TAG, "Google play services updated")
            true
        }
    }

    private fun createToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful){
                    return@OnCompleteListener
                }

                val token = task.result

                val msg = getString(R.string.msg_token_fms, token)
                Toast.makeText(baseContext ,msg, Toast.LENGTH_SHORT).show()
            })
    }


}