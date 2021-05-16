package k12tt.luongvany.presentation.binding

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class NotificationBinding: BaseObservable(), Parcelable{
    @IgnoredOnParcel
    @Bindable
    var id: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.id)
        }
    @IgnoredOnParcel
    @Bindable
    var title: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.title)
        }
    @IgnoredOnParcel
    @Bindable
    var content: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.content)
        }
    @IgnoredOnParcel
    @Bindable
    var url: String? = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.url)
        }

    @Bindable
    @IgnoredOnParcel
    var publisher: String? = null
        set(value){
            field = value
            notifyPropertyChanged(BR.publisher)
        }
    @Bindable
    @IgnoredOnParcel
    var image: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.image)
        }

    @Bindable
    @IgnoredOnParcel
    var pdfFile: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.pdfFile)
        }

    @Bindable
    @IgnoredOnParcel
    var notificationType: NotificationTypeBinding = NotificationTypeBinding.THONGBAO
        set(value){
            field = value
            notifyPropertyChanged(BR.notificationType)
        }
    @Bindable
    @IgnoredOnParcel
    var like: Int = 0
        set(value){
            field = value
            notifyPropertyChanged(BR.like)
        }

    @Bindable
    @IgnoredOnParcel
    var target: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.target)
        }

    @Bindable
    @IgnoredOnParcel
    var timestamp: String = ""
        set(value){
            field = value
            notifyPropertyChanged(BR.timestamp)
        }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NotificationBinding
        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (url != other.url) return false
        if (publisher != other.publisher) return false
        if (image != other.image) return false
        if (notificationType != other.notificationType) return false
        if (pdfFile != other.pdfFile) return false
        if (like != other.like) return false
        if (target != other.target) return false
        if (timestamp != other.timestamp) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (publisher?.hashCode() ?: 0)
        result = 31 * result + image.hashCode()
        result = 31 * result + notificationType.hashCode()
        result = 31 * result + like.hashCode()
        result = 31 * result + target.hashCode()
        result = 31 * result + pdfFile.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}