package k12tt.luongvany.data_fb.api

import k12tt.luongvany.data_fb.constants.Constants.Companion.CONTENT_TYPE
import k12tt.luongvany.data_fb.constants.Constants.Companion.SERVER_KEY
import k12tt.luongvany.data_fb.entities.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI{

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE" )
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}