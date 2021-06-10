package k12tt.luongvany.data_fb.entities

data class NotificationDataFCM(
    val title: String,
    val message: String,
    val id: String, val publisher: String, val target: String)