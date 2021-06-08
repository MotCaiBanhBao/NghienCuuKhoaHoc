package k12tt.luongvany.data.model.notification

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class NotificationData(
    var id: String = "",
    var content: String = "",
    var url: String? = "",
    var publisher: String? = "",
    var image: String? = "",
    var pdfFile: String? ="",
    var notificationType: NotificationTypeData = NotificationTypeData.THONGBAO,
    var target: String = "",
    val _title: String = "",
    var checked: Boolean = false,
){
    @ServerTimestamp
    var timestamp: Date = Timestamp.now().toDate()
        private set

    val title: String
        get() = if(_title.trim() == "") "$publisher thông báo đến $target" else _title

}