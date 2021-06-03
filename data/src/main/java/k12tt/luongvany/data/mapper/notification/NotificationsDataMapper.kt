package k12tt.luongvany.data.mapper.notification

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.notification.NotificationTypeData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType

class NotificationsDataMapper : Mapper<Notification, NotificationData> {
    override fun map(source: Notification): NotificationData {
        return NotificationData(
            id = source.id,
            content = source.content,
            url = source.url,
            publisher = source.publisher,
            image = source.image,
            notificationType = mapNotificationType(source.notificationType),
            checked = source.checked,
            target = source.target
        )
    }

    private fun mapNotificationType(notificationType: NotificationType): NotificationTypeData {
        return when (notificationType) {
            NotificationType.THONGBAO -> NotificationTypeData.THONGBAO
            NotificationType.THOIKHOABIEU -> NotificationTypeData.THOIKHOABIEU
            else -> NotificationTypeData.THOIKHOABIEU
        }
    }
}