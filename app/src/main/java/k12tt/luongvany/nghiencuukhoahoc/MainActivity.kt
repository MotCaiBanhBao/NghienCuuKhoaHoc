package k12tt.luongvany.nghiencuukhoahoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference


private const val TAG = "TEST"
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TEST", "Main")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        createToken()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onStart() {
        super.onStart()
        Picasso.get().load(UserSingleTon.getUserPhotoUrl()).fit().centerCrop().into(binding.userPictureProfile)
        binding.userPictureProfile.setOnClickListener{
            val intent = Intent(this, UserProfile::class.java)
            startActivityForResult(intent, USER_PROFILE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == USER_PROFILE){
            if (resultCode == RESULT_OK){
                val intent = Intent(baseContext, MainLogin::class.java).apply{
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                AppPreference.isLogin = false
                auth.signOut()
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(R.navigation.list_notification)

        val controller = binding.bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller.observe(this, Observer { navController ->
            binding.topAppBar.setupWithNavController(
                navController, AppBarConfiguration(
                    navController.graph
                )
            )
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
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result

                val msg = getString(R.string.msg_token_fms, token)
                Toast.makeText(baseContext ,msg, Toast.LENGTH_SHORT).show()
            })
    }

    companion object{
        private const val USER_PROFILE = 1
    }
}