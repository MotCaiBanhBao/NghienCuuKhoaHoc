package k12tt.luongvany.presentation.binding.topic

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class TopicsBinding: BaseObservable(), Parcelable{
    @IgnoredOnParcel
    @Bindable
    var nameTopic: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.nameTopic)
        }
    
    @IgnoredOnParcel
    @Bindable
    var listTopic: List<Map<String, String>> = emptyList<Map<String, String>>()
        set(value){
            field = value
            notifyPropertyChanged(BR.listTopic)
        }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TopicsBinding
        if (nameTopic != other.nameTopic) return false
        if (listTopic != other.listTopic) return false
        return true
    }

    override fun hashCode(): Int {
        var result = nameTopic.hashCode()
        result = 31 * result + listTopic.hashCode()
        return result
    }
}