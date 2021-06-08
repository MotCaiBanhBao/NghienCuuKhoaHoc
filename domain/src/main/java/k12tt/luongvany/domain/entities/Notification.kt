package k12tt.luongvany.domain.entities

import java.util.*


data class Notification(
    var id: String = "",
    var content: String = "",
    val _url: String? = "",
    val _title: String = "",
    var publisher: String?,
    var image: String? = "",
    var notificationType: NotificationType = NotificationType.THONGBAO,
    var target: String = "",
    var checked: Boolean = false,
    var timestamp: Date = Date()
){
    val title: String
        get() = if(_title.trim() == "") "$publisher thông báo đến $target" else _title
    val url: String
        get() = if (_url?.isEmpty() == true) "" else "Xem chi tiết tại: $_url"

}