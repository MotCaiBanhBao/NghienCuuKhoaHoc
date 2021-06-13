package k12tt.luongvany.nghiencuukhoahoc.auth

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import k12tt.luongvany.domain.entities.User
import k12tt.luongvany.nghiencuukhoahoc.MainActivity
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.nghiencuukhoahoc.UserSingleTon
import k12tt.luongvany.presentation.auth.Auth
import javax.inject.Singleton

class FirebaseSignIn(private val activity: FragmentActivity): Auth<Int, Intent>() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authListener: FirebaseAuth.AuthStateListener =
        FirebaseAuth.AuthStateListener { fbAuth ->
            callbacks.forEach {
                it.onAuthChanged(fbAuth.currentUser != null)
            }
        }

    init {
        firebaseAuth.addAuthStateListener(authListener)
    }

    private val googleApiClient: GoogleSignInClient by lazy {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(activity, options)
    }

    override fun isLoggedIn() = firebaseAuth.currentUser != null

    override fun startSignIn(authInfo: Int?) {
        val signInIntent = googleApiClient.signInIntent
        activity.startActivityForResult(signInIntent, authInfo ?: 0)
    }

    override fun handleSignInResult(result: Intent?, onSuccess: () -> Unit, onError: () -> Unit, onFirstLogin: () -> Unit) {
        val signInTask = GoogleSignIn.getSignedInAccountFromIntent(result)

        val account = signInTask.getResult(ApiException::class.java)
        require(account != null)
        firebaseAuthWithGoogle(account, onSuccess, onError, onFirstLogin)
    }

    override fun signOut() {
        FirebaseMessaging.getInstance().deleteToken()
        googleApiClient.signOut()
        firebaseAuth.signOut()
    }

    override fun clear() {
        super.clear()
        callbacks.clear()
        firebaseAuth.removeAuthStateListener(authListener)
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?, onSuccess: () -> Unit, onError: () -> Unit, onFirstLogin: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    if (task.result?.additionalUserInfo?.isNewUser == true){
                        val fireStore = FirebaseFirestore.getInstance()
                        val db = fireStore

                        UserSingleTon.getUserUid()?.let {
                            val userData =  User(uid = UserSingleTon.getUserUid(),
                                name = UserSingleTon.getUserName(),
                                emailAdress = UserSingleTon.getEmail(),
                                language = UserSingleTon.getLanguageNumber(),
                                avatarUri = UserSingleTon.getUserPhotoUrl(),
                                isAdmin =  false)

                            db.collection("users").document(it).set(userData).addOnFailureListener{ e->
                                googleApiClient.signOut()
                                onError()
                            }
                        }

                        onFirstLogin()
                    }
                    else{
                        onSuccess()
                    }
                } else {
                    googleApiClient.signOut()
                    onError()
                }
            }
    }

    override fun startSignInWithEmail(userName: String, password: String, onSuccess: () -> Unit, onFirstLogin: () -> Unit){
        firebaseAuth.signInWithEmailAndPassword(userName, password).addOnFailureListener {
            AlertDialog.Builder(activity)
                .setMessage("Mật khẩu hoặc tài khoản sai")
                .setTitle("Lỗi")
                .setNegativeButton("OK", null)
                .show()
        }.addOnSuccessListener {
            if(it.additionalUserInfo?.  isNewUser == true){
                onFirstLogin()
            }else{
                onSuccess()
            }
        }
    }
}