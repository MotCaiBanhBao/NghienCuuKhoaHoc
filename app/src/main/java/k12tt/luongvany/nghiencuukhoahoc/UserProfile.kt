package k12tt.luongvany.nghiencuukhoahoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.UserDetailBinding
import k12tt.luongvany.presentation.viewmodel.user.MainActivityViewModel
import k12tt.luongvany.presentation.viewmodel.user.UserDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserProfile : BaseFragment(){
    private lateinit var binding: UserDetailBinding
    private val viewModel: UserDetailViewModel by viewModel(){
        parametersOf(FirebaseAuth.getInstance().currentUser.uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserDetailBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.userName.text = UserSingleTon.getUserName()
        Picasso.get().load(UserSingleTon.getUserPhotoUrl()).fit().centerCrop().into(binding.userPicture)
        binding.userClass.text = "null"

        binding.logout.setOnClickListener{
            viewModel.unSubAll()
            auth.signOut()
        }

    }
}