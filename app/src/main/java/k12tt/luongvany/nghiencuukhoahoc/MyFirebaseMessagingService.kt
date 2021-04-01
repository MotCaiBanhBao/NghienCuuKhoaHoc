package k12tt.luongvany.nghiencuukhoahoc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import k12tt.luongvany.nghiencuukhoahoc.notificationUtil.createIntentListener

class MyFirebaseMessagingService : FirebaseMessagingService(){
    private var broadcaster: LocalBroadcastManager? = null
    private val processLater = false

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        if (/* Check if data needs to be processed by long running job */processLater) {
            Log.d(TAG, "executing schedule job")
        } else {
            // Handle message within 10 seconds
            Log.d(TAG, "Handle message")
            Log.d(TAG, "${remoteMessage.notification?.body}")
            Log.d(TAG, "${remoteMessage.notification?.title}")
            handleNow(remoteMessage)
        }
    }

    private fun handleNow(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())

        handler.post {

            remoteMessage.notification?.let {
                val intent = Intent("MyData")
                intent.putExtra("message", remoteMessage.data["text"])
                broadcaster?.sendBroadcast(intent)
            }
        }
    ////////////////////////////////////
        val channelId = "Default"

        val pendingIntent =  createIntentListener(MainActivity::class.java, applicationContext)

        val builder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(remoteMessage.notification?.body).setContentTitle(remoteMessage.notification?.title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder)
    ////////////////////////////////////
    }


    companion object {
        private const val TAG = "MyFirebaseMessagingS"
    }
}
