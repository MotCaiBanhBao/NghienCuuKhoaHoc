package k12tt.luongvany.presentation.auth

abstract class Auth<Input, Result>{
    protected val callbacks = mutableListOf<AuthStateListener>()

    abstract fun isLoggedIn(): Boolean

    abstract fun startSignIn(authInfo: Input? = null)

    abstract fun startSignInWithEmail(userName: String, password: String, onSuccess: () -> Unit, onFirstLogin: () -> Unit)

    abstract fun handleSignInResult(result: Result?, onSuccess: () -> Unit, onError: () -> Unit, onFirstLogin: () -> Unit)

    fun addAuthChangeListener(listener: AuthStateListener) {
        callbacks.add(listener)
    }

    fun removeAuthChangeListener(listener: AuthStateListener) {
        callbacks.remove(listener)
    }

    open fun clear() {
        callbacks.clear()
    }

    abstract fun signOut()

    companion object{
        const val GOOGLE_AUTH = "1"
        const val EMAIL_AUTH = "2"
    }
}