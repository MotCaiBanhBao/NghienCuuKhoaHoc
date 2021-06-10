package k12tt.luongvany.data.model.notification

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*


data class NotificationData(
    var id: String = "",
    var content: String = "",
    var _url: String? = "",
    var publisher: String? = "",
    var image: String? = "",
    var pdfFile: String? ="",
    var notificationType: NotificationTypeData = NotificationTypeData.THONGBAO,
    var target: String = "",
    var checked: Boolean = false,
){
    @ServerTimestamp
    var timestamp: Date = Timestamp.now().toDate()
        private set

    var title: String = ""
        get() = if(field == "") "$publisher thông báo đến $target" else field


}