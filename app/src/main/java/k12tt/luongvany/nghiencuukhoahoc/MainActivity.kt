package k12tt.luongvany.nghiencuukhoahoc

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding
import k12tt.luongvany.nghiencuukhoahoc.notificationview.SlideBar
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference
import k12tt.luongvany.presentation.Router
import k12tt.luongvany.presentation.auth.Auth
import k12tt.luongvany.presentation.auth.AuthStateListener
import k12tt.luongvany.presentation.viewmodel.user.MainActivityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    val auth: Auth<Int, Intent> by inject { parametersOf(this@MainActivity) }
    val router: Router by inject { parametersOf(this@MainActivity) }

    private val authListener: AuthStateListener =
        object : AuthStateListener {
            override fun onAuthChanged(isLoggedIn: Boolean) {
                if (!isLoggedIn) {
                    Log.d("TEST4", "Co thay doi")
                    router.showLogin()
                }
            }
        }

    override fun onBackPressed() {
        if (router.isInRootScreen()) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (!auth.isLoggedIn()) {
            binding.root.visibility = View.GONE
        }
        setContentView(binding.root)
        Log.d("TEST", "MainOncreate")
        val context: ContextWrapper? = AppPreference.language?.let { Language.wrap(this@MainActivity, it) }
        if (context != null) {
            resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
        }

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("TEST", "onStart")
        auth.addAuthChangeListener(authListener)
        Picasso.get().load(UserSingleTon.getUserPhotoUrl()).fit().centerCrop().into(binding.userPictureProfile)

        binding.userPictureProfile.setOnClickListener{
           router.showUserDetail()
        }

        setUpLanguageChoose()
        createToken()
    }

    override fun onStop() {
        super.onStop()
        Log.d("TEST", "MainOnStop")
        auth.removeAuthChangeListener(authListener)
    }

    private fun setUpLanguageChoose(){
        binding.languageChoose.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                arrayListOf(
                    IconSpinnerItem(text = "", iconRes = R.drawable.ic_united_kingdom_flag_icon_round),
                    IconSpinnerItem(text = "", iconRes = R.drawable.ic_flag_of_vietnam),
                    IconSpinnerItem(text = "", iconRes = R.drawable.ic_flag_of_laos),
                    IconSpinnerItem(text = "", iconRes = R.drawable.ic_cambodia)
                )
            )

            setPadding(10,10,0,10)
            getSpinnerRecyclerView().layoutManager = LinearLayoutManager(context)
            selectItemByIndex(AppPreference.spinnerIndex)
            lifecycleOwner = this@MainActivity

            setOnSpinnerOutsideTouchListener{_: View, _: MotionEvent ->
                binding.languageChoose.dismiss()
            }

            setOnSpinnerItemSelectedListener<IconSpinnerItem>{ oldIndex, _, newIndex, _ ->
                when(newIndex){
                    0 -> {
                        AppPreference.language = Language.ENGLISH
                        changeLanguage(Language.ENGLISH)
                    }
                    1 -> {
                        AppPreference.language = Language.VIETNAMESE
                        changeLanguage(Language.VIETNAMESE)
                    }
                    2 -> {
                        AppPreference.language = Language.LAOS
                        changeLanguage(Language.LAOS)
                    }
                    3 -> {
                        AppPreference.language = Language.CAMBODIA
                        changeLanguage(Language.CAMBODIA)
                    }
                }
                AppPreference.spinnerIndex = newIndex
            }
        }
    }

    private fun changeLanguage(language: String){
        val context: Context = Language.wrap(this@MainActivity , language)
        resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
        val intent = Intent(baseContext, this::class.java)
        startActivity(intent)
        finish()
    }
    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                auth.handleSignInResult(data,
                    {
                        Picasso.get().load(UserSingleTon.getUserPhotoUrl()).fit().centerCrop().into(binding.userPictureProfile)
                        router.showNotificationsList()

                    }, {
                        showErrorSignIn()
                       },{
                        startActivity(Intent(this, SlideBar::class.java))
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.nav_graph, R.navigation.choose_target_nav, R.navigation.udck_detail_nav, R.navigation.my_notification_nav)

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

    private fun createToken(){
        viewModel.reSub()
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                MyFirebaseMessagingService.token = token
                val msg = getString(R.string.msg_token_fms, token)
                Log.d("TEST", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }


    companion object {
        const val TOPIC = "/topics/udck"
        const val RC_GOOGLE_SIGN_IN = 1
    }
}