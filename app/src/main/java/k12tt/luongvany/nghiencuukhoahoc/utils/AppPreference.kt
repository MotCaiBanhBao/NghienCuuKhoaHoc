package k12tt.luongvany.nghiencuukhoahoc.utils

import android.content.Context
import android.content.SharedPreferences
import k12tt.luongvany.nghiencuukhoahoc.Language

object AppPreference {
    private const val NAME = "k12tt.luongvany.nghiencuukhoahoc"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val IS_LOGIN = Pair("isLogin", false)
    private val IS_FIRST_LAUNCH = Pair("isFirstLaunch", true)
    private val SPINNER_INDEX = Pair("spinnerIndex", 0)
    private val LANGUAGE = Pair("chooseLanguage", Language.ENGLISH)

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var spinnerIndex: Int
        get() = preferences.getInt(SPINNER_INDEX.first, SPINNER_INDEX.second)
        set(value) = preferences.edit{
            it.putInt(SPINNER_INDEX.first, value)
        }

    var language: String?
        get() = preferences.getString(LANGUAGE.first, LANGUAGE.second)
        set(value) = preferences.edit{
            it.putString(LANGUAGE.first, value)
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