package k12tt.luongvany.nghiencuukhoahoc.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.MainLoginBinding
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_ALL
import k12tt.luongvany.presentation.viewmodel.user.MainActivityViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment: BaseFragment() {
    private var _binding: MainLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        router.showView()
        _binding = MainLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginWithEmail.setOnClickListener {
            router.showLoginWithAccount()
        }
         binding.loginWithGmail.setOnClickListener {
            if (activity is MainActivity) {
                (activity as MainActivity).startSignIn()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        router.hideAppBarAndBottom(HIDE_ALL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
