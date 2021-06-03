package k12tt.luongvany.nghiencuukhoahoc.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.LoginFragmentBinding
import k12tt.luongvany.nghiencuukhoahoc.notificationview.SlideBar
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_ALL
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.SHOW_ALL

class SignInWithAccount : BaseFragment(){
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LoginFragmentBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        return view
    }

    override fun onStart() {
        super.onStart()
        binding.btnSave.setOnClickListener{
            it.isClickable = false
            auth.startSignInWithEmail(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),
                {
                    router.showNotificationsList()
                }, {
                    startActivity(
                        Intent(requireParentFragment().context, SlideBar::class.java)
                    )
                }
            )
            it.isClickable = true
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

    override fun onDetach() {
        super.onDetach()
        router.showAppBarAndBottom(SHOW_ALL)
    }
}