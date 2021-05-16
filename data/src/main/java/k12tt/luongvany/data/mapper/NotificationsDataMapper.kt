package k12tt.luongvany.data.mapper

import k12tt.luongvany.data.model.NotificationData
import k12tt.luongvany.data.model.NotificationTypeData
import k12tt.luongvany.data.model.PublisherData
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType
import k12tt.luongvany.domain.entities.Publisher

class NotificationsDataMapper : Mapper<Notification, NotificationData>{
    override fun map(source: Notification): NotificationData {
        return NotificationData(id = source.id,
            content = source.content,
            url = source.url,
            publisher = source.publisher,
            image = source.image,
            notificationType = mapNotificationType(source.notificationType),
            like = source.like,
            target = source.target
        )
    }

    private fun mapNotificationType(notificationType: NotificationType): NotificationTypeData{
        return when (notificationType) {
            NotificationType.THONGBAO -> NotificationTypeData.THONGBAO
            NotificationType.THOIKHOABIEU -> NotificationTypeData.THOIKHOABIEU
        }
    }
}