package k12tt.luongvany.data_fb.util

import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data_fb.entities.NotificationDataFCM
import k12tt.luongvany.data_fb.entities.NotificationTag
import k12tt.luongvany.data_fb.entities.PushNotification

fun NotificationData.toPushNotification(topic: String): PushNotification {
    val notificationFCM = NotificationDataFCM(
        title,
        content,
        id
    )
    return PushNotification(notificationFCM, NotificationTag(id), topic)
}
