package k12tt.luongvany.nghiencuukhoahoc.homescreen

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.Fragment
//import com.firebase.ui.auth.AuthUI
//import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import k12tt.luongvany.nghiencuukhoahoc.MainActivity
//
//import k12tt.luongvany.nghiencuukhoahoc.R
//import k12tt.luongvany.nghiencuukhoahoc.databinding.ActivityMainBinding
//
//private const val TAG = "GetToken"
//class NotificationDetail : Fragment(){
//
//    private lateinit var parentBottomNavigation: BottomNavigationView
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        parentBottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)!!
//        val view = inflater.inflate(R.layout.notification_detail, container, false)
//
//        val button = view.findViewById<Button>(R.id.signin)
//
//
//        button.setOnClickListener {
//            val providers = arrayListOf(
//                    AuthUI.IdpConfig.EmailBuilder().build(),
//                    AuthUI.IdpConfig.GoogleBuilder().build(),
//                    AuthUI.IdpConfig.FacebookBuilder().build()
//            )
//
//            startActivityForResult(AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setLogo(R.mipmap.ic_app_logo)
//                .setAvailableProviders(providers).build(), 1)
//        }
//
//        return view
//    }
//
//    override fun onDetach() {
//        parentBottomNavigation.let {
//            it.animate().translationY(0F).duration = 200
//
//            it.visibility = View.VISIBLE
//        }
//
//        super.onDetach()
//    }
//}
