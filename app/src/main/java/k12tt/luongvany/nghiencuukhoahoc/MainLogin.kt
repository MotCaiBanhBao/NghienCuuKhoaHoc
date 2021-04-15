package k12tt.luongvany.nghiencuukhoahoc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.firebase.ui.auth.data.remote.FacebookSignInHandler
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import k12tt.luongvany.nghiencuukhoahoc.databinding.MainLoginBinding
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference

class MainLogin : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: MainLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        if(AppPreference.isLogin){
            sendToActivity(MainActivity::class.java)
        }
        else{
            binding = MainLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            auth = FirebaseAuth.getInstance()

            binding.loginWithGmail.setOnClickListener(this)
            binding.loginWithFacebook.setOnClickListener(this)


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            setupForFacebookLogin()
        }
    }

    private fun setupForFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        val login = LoginManager.getInstance()
        login.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d(TAG, "facebook:onSuccess:$result")
                    if (result != null) {
                        handleFacebookAccessToken(result.accessToken)
                    }
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")

                }

                override fun onError(error: FacebookException) {
                    Log.d(TAG, "facebook:onError", error)
                }
            })

        binding.loginWithFacebook.setOnClickListener{
            login.logInWithReadPermissions(this, listOf("email", "public_profile"))
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    LoginManager.getInstance().logOut()
                    AppPreference.isLogin = true
                    sendToActivity(MainActivity::class.java)
                    Log.d(TAG, "signInWithCredential:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    AppPreference.isLogin = true
                    googleSignInClient.signOut()
                    sendToActivity(MainActivity::class.java)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    val view = binding.root
                    Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInGmail(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.loginWithGmail -> signInGmail()
        }
    }

    private fun sendToActivity(cls: Class<*>){
        val intent = Intent(application, MainActivity::class.java).apply{
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}