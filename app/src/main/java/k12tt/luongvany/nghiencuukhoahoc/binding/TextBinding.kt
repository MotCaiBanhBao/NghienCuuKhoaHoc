package k12tt.luongvany.nghiencuukhoahoc.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import k12tt.luongvany.nghiencuukhoahoc.R
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding

object TextBinding {
    @JvmStatic
    @BindingAdapter("android:text")
    fun setMediaTypeText(textView: TextView, notificationTypeBinding: NotificationTypeBinding?) {
        if (notificationTypeBinding == null) {
            textView.text = null
            return
        }
        val context = textView.context
        when (notificationTypeBinding) {
            NotificationTypeBinding.THOIKHOABIEU -> textView.text = context.getString(R.string.text_notification_type_thoikhoabieu)
            NotificationTypeBinding.THONGBAO -> textView.text = context.getString(R.string.text_notification_type_thongbao)
        }
    }
}
