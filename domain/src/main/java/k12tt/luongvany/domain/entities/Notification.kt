package k12tt.luongvany.domain.entities

import java.util.*


data class Notification(
    var id: String = "",
    var content: String = "",
    var _url: String? = "",
    var publisher: String?,
    var image: String? = "",
    var notificationType: NotificationType = NotificationType.THONGBAO,
    var target: String = "",
    var checked: Boolean = false,
    var timestamp: Date = Date()
){
    val title: String
        get() = "$publisher thông báo đến $target"
    val url: String
        get() = if (_url?.isEmpty() == true) "" else "Xem chi tiết tại: $_url"

}