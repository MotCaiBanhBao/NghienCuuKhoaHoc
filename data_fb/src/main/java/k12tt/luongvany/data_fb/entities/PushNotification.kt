package k12tt.luongvany.data_fb.entities

data class PushNotification(
    val data: NotificationDataFCM,
    val to: String
)