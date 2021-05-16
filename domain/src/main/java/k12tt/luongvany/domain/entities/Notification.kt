package k12tt.luongvany.domain.entities

import java.util.*


data class Notification(
    var id: String = "",
    var content: String = "",
    var url: String? = "",
    var publisher: String?,
    var image: String? = "",
    var pdfFile: String? ="",
    var notificationType: NotificationType = NotificationType.THONGBAO,
    var target: String = "",
    var like: Int = 0,
    var timestamp: Date = Date()
){

    val title: String
        get() = "$publisher thông báo đến lớp $target"
}