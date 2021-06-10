package k12tt.luongvany.data.mapper.notification

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.notification.NotificationTypeData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType

class NotificationMapper: Mapper<NotificationData, Notification> {
    override fun map(source: NotificationData): Notification {
        return Notification(
            id = source.id,
            content = source.content,
            _url = source._url,
            publisher = source.publisher,
            image = source.image,
            notificationType = mapNotificationType(source.notificationType),
            checked = source.checked,
            target = source.target,
            timestamp = source.timestamp
        )
    }

    private fun mapNotificationType(notificationType: NotificationTypeData): NotificationType {
        return when (notificationType) {
            NotificationTypeData.THONGBAO -> NotificationType.THONGBAO
            NotificationTypeData.THOIKHOABIEU -> NotificationType.THOIKHOABIEU
        }
    }
}