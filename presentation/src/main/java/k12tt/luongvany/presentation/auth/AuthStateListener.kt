package k12tt.luongvany.presentation.auth

interface AuthStateListener {
    fun onAuthChanged(isLoggedIn: Boolean)
}
