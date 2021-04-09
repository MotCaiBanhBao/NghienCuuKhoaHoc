package k12tt.luongvany.nghiencuukhoahoc

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import k12tt.luongvany.nghiencuukhoahoc.databinding.UserDetailBinding
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference

class UserProfile : AppCompatActivity(){
    private lateinit var binding: UserDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        binding.userName.text = UserSingleTon.getUserName()
        Picasso.get().load(UserSingleTon.getUserPhotoUrl()).fit().centerCrop().into(binding.userPicture)
        binding.userClass.text = "null"

        binding.logout.setOnClickListener{
            setResult(RESULT_OK, null)
            finish()
        }

    }
}