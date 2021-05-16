package k12tt.luongvany.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import k12tt.luongvany.domain.entities.NotificationType
import java.util.*


data class NotificationData (
    var id: String = "",
    var content: String = "",
    var url: String? = "",
    var publisher: String? = "",
    var image: String? = "",
    var pdfFile: String? ="",
    var notificationType: NotificationTypeData = NotificationTypeData.THONGBAO,
    var target: String = "",
    var like: Int = 0,
){
    @ServerTimestamp
    var timestamp: Date = Timestamp.now().toDate()
        private set

    val title: String
        get() = "$publisher thông báo đến lớp $target"

}