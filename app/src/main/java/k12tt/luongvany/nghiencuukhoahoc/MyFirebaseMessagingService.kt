package k12tt.luongvany.nghiencuukhoahoc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import k12tt.luongvany.data.model.notification.NotificationData
import java.util.*

private const val CHANNEL_ID = "my_channel"
class MyFirebaseMessagingService : FirebaseMessagingService(){
        companion object {
            private var sharedPref: SharedPreferences? = null

            var token: String?
                get() {
                    return sharedPref?.getString("token", "")
                }
                set(value) {
                    sharedPref?.edit()?.putString("token", value)?.apply()
                }
        }

        override fun onNewToken(newToken: String) {
            super.onNewToken(newToken)
            token = newToken
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)

            val intent = Intent(this, MainActivity::class.java)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = Random().nextInt()
            val fbAuth = FirebaseAuth.getInstance()
            val fireStore = FirebaseFirestore.getInstance()
            val db = fireStore
            val collection = db.collection("users").document(fbAuth.currentUser.uid).collection("notifications")
            collection.document(message.data["id"].toString()).set(NotificationData(content = message.data["message"]?:"Null").apply {
                title = message.data["title"].toString()
                id = message.data["id"].toString()
                target = message.data["target"].toString()
                publisher = message.data["publisher"].toString()
            })
            Log.d("TEST", message.notification?.tag.toString())
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(message.data["id"].toString(), 1, notification)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(notificationManager: NotificationManager) {
            val channelName = "channelName"
            val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
                description = "My channel description"
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(channel)
        }
}

