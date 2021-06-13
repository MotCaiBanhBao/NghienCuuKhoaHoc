package k12tt.luongvany.presentation.binding.notification

import android.annotation.SuppressLint
import android.os.Build
import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.NotificationType
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.ofInstant
import java.time.ZoneId
import java.util.*

object NotificationConverter {
    @SuppressLint("SimpleDateFormat")
    fun fromData(notification: Notification): NotificationBinding {
        return NotificationBinding().apply {
            id = notification.id
            title = notification.title
            content = notification.content
            url = notification.url
            image = notification.image.toString()
            publisher = notification.publisher
            notificationType = NotificationTypeBinding.values()[notification.notificationType.ordinal]
            checked = notification.checked
            target = notification.target
            timestamp = notification.timestamp.let {
                SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(it).toString()
            }
        }
    }

    fun toData(notification: NotificationBinding): Notification {
        return Notification(
            id = notification.id,
            content = notification.content,
            _url = notification.url,
            image = notification.image,
            checked = notification.checked,
            publisher = notification.publisher,
            target = notification.target,
            notificationType = NotificationType.values()[notification.notificationType.ordinal]
        )
    }
}