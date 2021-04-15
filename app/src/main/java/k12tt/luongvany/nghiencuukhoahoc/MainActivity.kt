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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.database.Notification
import k12tt.luongvany.nghiencuukhoahoc.database.NotificationData
import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference


private const val TAG = "TEST"
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        val context: ContextWrapper? = AppPreference.language?.let { Language.wrap(this@MainActivity , it) }
        if (context != null) {
            resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
        }
        super.onCreate(savedInstanceState)
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
        setUpLanguageChoose()

        binding.sendNotificationFloatButton.setOnClickListener{
            Log.d("TEST", "FIREBASE")
            Notification.instance.pushNotification(NotificationData("Tuan", "TEST", "TEST", 1, "TEST"))
        }

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