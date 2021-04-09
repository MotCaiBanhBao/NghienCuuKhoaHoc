package k12tt.luongvany.nghiencuukhoahoc

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object UserSingleTon {
    val user: FirebaseUser?
        get() = getCurrentUser()


    fun getUserPhotoUrl(): String? {
        return user?.photoUrl?.toString()
    }

    fun getUserName(): String? {
        return if (user != null) {
            user!!.displayName
        } else "ANONYMOUS"
    }

    private fun getCurrentUser(): FirebaseUser?{
        return if (FirebaseAuth.getInstance().currentUser == null) null else FirebaseAuth.getInstance().currentUser
    }
}
