package k12tt.luongvany.presentation.binding.user

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class UserBinding: BaseObservable(), Parcelable {
    @IgnoredOnParcel
    @Bindable
    var uid: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.uid)
        }
    @IgnoredOnParcel
    @Bindable
    var nameUser: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.nameUser)
        }

    @IgnoredOnParcel
    @Bindable
    var emailAdress: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.emailAdress)
        }
    @IgnoredOnParcel
    @Bindable
    var language: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.language)
        }
    @IgnoredOnParcel
    @Bindable
    var isAdmin: Boolean = false
        set(value){
            field = value
            notifyPropertyChanged(BR.isAdmin)
        }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserBinding
        if (nameUser != other.nameUser) return false
        if (uid != other.uid) return false
        if (emailAdress != other.emailAdress) return false
        if (language != other.language) return false
        if (isAdmin != other.isAdmin) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + nameUser.hashCode()
        result = 31 * result + emailAdress.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + isAdmin.hashCode()
        return result
    }
}