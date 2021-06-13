package k12tt.luongvany.presentation.binding.message

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class MessageBinding: BaseObservable(), Parcelable {
    @IgnoredOnParcel
    @Bindable
    var idMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.idMessage)
        }
    @Bindable
    @IgnoredOnParcel
    var timestampMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.timestampMessage)
        }
    @Bindable
    @IgnoredOnParcel
    var userNameMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.userNameMessage)
        }
    @Bindable
    @IgnoredOnParcel
    var userUidMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.userUidMessage)
        }
    @Bindable
    @IgnoredOnParcel
    var contextMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.contextMessage)
        }
    @Bindable
    @IgnoredOnParcel
    var hourOfMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.hourOfMessage)
        }

    @Bindable
    @IgnoredOnParcel
    var photoUrlMessage: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.photoUrlMessage)
        }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MessageBinding
        if (hourOfMessage != other.hourOfMessage) return false
        if (idMessage != other.idMessage) return false
        if (timestampMessage != other.timestampMessage) return false
        if (userNameMessage != other.userNameMessage) return false
        if (userUidMessage != other.userUidMessage) return false
        if (contextMessage != other.contextMessage) return false
        if (photoUrlMessage != other.photoUrlMessage) return false
        return true
    }

    override fun hashCode(): Int {
        var result = idMessage.hashCode()
        result = 31 * result + hourOfMessage.hashCode()
        result = 31 * result + timestampMessage.hashCode()
        result = 31 * result + userNameMessage.hashCode()
        result = 31 * result + userUidMessage.hashCode()
        result = 31 * result + contextMessage.hashCode()
        result = 31 * result + photoUrlMessage.hashCode()
        return result
    }
}
