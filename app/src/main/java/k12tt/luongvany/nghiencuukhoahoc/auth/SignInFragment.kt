package k12tt.luongvany.nghiencuukhoahoc.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.MainLoginBinding

class SignInFragment: BaseFragment() {
    private var _binding: MainLoginBinding? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var parentAppBarLayout: AppBarLayout
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentAppBarLayout = activity?.findViewById<AppBarLayout>(R.id.appBarLayout)!!
        bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)!!

        parentAppBarLayout.setExpanded(false, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView.let {
            it.visibility = View.GONE
        }
        Log.d("TEST", "SignInFragment1")

         binding.loginWithGmail.setOnClickListener {
            if (activity is MainActivity) {
                (activity as MainActivity).startSignIn()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        parentAppBarLayout.setExpanded(true)
        bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }
}
