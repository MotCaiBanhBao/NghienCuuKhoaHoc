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
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import k12tt.luongvany.nghiencuukhoahoc.notificationview.NotificationActivity
import java.util.*

private const val CHANNEL_ID = "my_channel"
class MyFirebaseMessagingService : FirebaseMessagingService(){
//    private var broadcaster: LocalBroadcastManager? = null
//    private val processLater = false
//
//    override fun onCreate() {
//        broadcaster = LocalBroadcastManager.getInstance(this)
//    }
//
//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//
//        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        if (/* Check if data needs to be processed by long running job */processLater) {
//            Log.d(TAG, "executing schedule job")
//        } else {
//            // Handle message within 10 seconds
//            Log.d(TAG, "Handle message")
//            Log.d(TAG, "${remoteMessage.notification?.body}")
//            Log.d(TAG, "${remoteMessage.notification?.title}")
//            handleNow(remoteMessage)
//        }
//
////        if (processLater) {
////            Log.d(TAG, "executing schedule job")
////        } else{
////            val extras = Bundle()
////            for ((key, value) in remoteMessage.data) {
////                extras.putString(key, value)
////            }
////            if(extras.containsKey("message") && !extras.getString("message").isNullOrBlank()) {
////                handleNow(extras.getString("message")!!)
////            }
////        }
//    }
//
//    private fun handleNow(remoteMessage: RemoteMessage) {
//        val handler = Handler(Looper.getMainLooper())
//
//        handler.post {
//
//            remoteMessage.notification?.let {
//                val intent = Intent("MyData")
//                intent.putExtra("message", remoteMessage.data["text"])
//                broadcaster?.sendBroadcast(intent)
//            }
//        }
//    ////////////////////////////////////
//        val channelId = "Default"
//
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//
//        val intent = Intent(this, NotificationActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//            PendingIntent.FLAG_ONE_SHOT)
//
//        val builder = NotificationCompat.Builder(applicationContext, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentText(remoteMessage.data["title"])
//                .setContentTitle(remoteMessage.data["message"])
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setOngoing(true)
//                .setContentIntent(pendingIntent)
//                .build()
//
//
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
//            manager.createNotificationChannel(channel)
//        }
//
//
//        manager.notify(0, builder)
//    ////////////////////////////////////
//    }
//
////    private fun handleNow(messageBody: String){
////        val intent = Intent(this, NotificationActivity::class.java)
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
////            PendingIntent.FLAG_ONE_SHOT)
////
////        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
////
////        var notificationBuilder: NotificationCompat.Builder? = null
////        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
////
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            val channel = NotificationChannel(
////                packageName,
////                packageName,
////                NotificationManager.IMPORTANCE_DEFAULT
////            )
////            channel.description = packageName
////            notificationManager.createNotificationChannel(channel)
////            if (notificationBuilder == null) {
////                notificationBuilder = NotificationCompat.Builder(application, packageName)
////            }
////        } else {
////            if (notificationBuilder == null) {
////                notificationBuilder = NotificationCompat.Builder(application,packageName)
////            }
////        }
////
////        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
////            .setContentTitle(getString(R.string.app_name))
////            .setContentText(messageBody)
////            .setAutoCancel(true)
////            .setSound(defaultSoundUri)
////            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////            .setContentIntent(pendingIntent)
////
////
////        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
////    }
//
//    companion object {
//        private const val TAG = "MyFirebaseMessagingS"
//    }

        companion object {
            var sharedPref: SharedPreferences? = null

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

        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)

            val intent = Intent(this, NotificationActivity::class.java)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = Random().nextInt()

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

            notificationManager.notify(notificationID, notification)
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
