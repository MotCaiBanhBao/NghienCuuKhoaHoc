package k12tt.luongvany.domain.usecases.notification

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.repositories.NotificationRepo

open class PushNotification(private val repository: NotificationRepo) {
    suspend fun execute(params: Notification){
        return if(notificationIsValid(params)){
            repository.pushNotification(params)
        }else{
            throw IllegalArgumentException("Notification is invalid")
        }
    }
    private fun notificationIsValid(notification: Notification): Boolean{
        return(notification.content.isNotBlank())
    }
}