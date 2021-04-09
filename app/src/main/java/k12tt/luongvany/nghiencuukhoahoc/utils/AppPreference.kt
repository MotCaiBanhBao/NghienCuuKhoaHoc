package k12tt.luongvany.nghiencuukhoahoc.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreference {
    private const val NAME = "k12tt.luongvany.nghiencuukhoahoc"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_LOGIN = Pair("isLogin", false)
    private val IS_FIRST_LAUNCH = Pair("isFirstLaunch", true)

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit{
            it.putBoolean(IS_LOGIN.first, value)
        }
    var isFirstLaunch: Boolean
        get() = preferences.getBoolean(IS_FIRST_LAUNCH.first, IS_FIRST_LAUNCH.second)
        set(value) = preferences.edit{
            it.putBoolean(IS_FIRST_LAUNCH.first, value)
        }
}