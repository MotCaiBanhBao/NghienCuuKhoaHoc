package k12tt.luongvany.data.mapper

import k12tt.luongvany.data.model.NotificationData
import k12tt.luongvany.data.model.NotificationTypeData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType

class NotificationMapper: Mapper<NotificationData, Notification> {
    override fun map(source: NotificationData): Notification {
        return Notification(
            id = source.id,
            content = source.content,
            url = source.url,
            publisher = source.publisher,
            image = source.image,
            notificationType = mapNotificationType(source.notificationType),
            like = source.like,
            target = source.target,
            pdfFile = source.pdfFile,
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