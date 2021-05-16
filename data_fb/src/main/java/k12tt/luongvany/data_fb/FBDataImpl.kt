package k12tt.luongvany.data_fb

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import k12tt.luongvany.data.model.NotificationData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.data_fb.entities.PushNotification
import k12tt.luongvany.data_fb.util.toPushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


internal class FBDataImpl : FBData{
    private val fbAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference.child(NOTIFICATION_KEY)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadNotifications(): Flow<List<NotificationData>> {

        return callbackFlow {
            val subscription = fireStore.collection(NOTIFICATION_KEY).orderBy("timestamp")
                .addSnapshotListener{ snapshot, e ->
                    if(e != null){
                        Timestamp(123L, 0)
                        return@addSnapshotListener
                    }
                    if(snapshot != null && !snapshot.isEmpty){
                        val notifications = snapshot.map {document ->
                            document.toObject(NotificationData::class.java)
                        }
                        offer(notifications)
                    }

                    else{
                        offer(emptyList<NotificationData>())
                    }

                }
            awaitClose{
                subscription.remove()
            }
        }
    }

    override fun loadNotification(notificationId: String): Flow<NotificationData?> {
        return callbackFlow {
            val subscription = fireStore.collection(NOTIFICATION_KEY)
                .document(notificationId)
                .addSnapshotListener{ snapshot, e ->
                    if (e != null){
                        close(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val notification = snapshot.toObject(NotificationData::class.java)
                        notification?.let {
                        offer(it)
                    }
                    }else{
                        offer(null)
                    }
                }
            awaitClose{subscription.remove()}
        }
    }

    override suspend fun pushNotification(notification: NotificationData) {
        return suspendCoroutine { continuation ->
            val currentUser = fbAuth.currentUser

            if (currentUser == null){
                continuation.resumeWithException(RuntimeException("Unauthorized used."))
            }else{
                val db = fireStore
                val collection = db.collection(NOTIFICATION_KEY)
                val pushTask = if(notification.id.isBlank()){
                    collection.add(notification).continueWithTask{ task ->
                        val doc = task.result
                        notification.id = doc?.id ?: UUID.randomUUID().toString()
                        doc?.update(mapOf(USER_ID_KEY to currentUser.uid, ID_KEY to notification.id))
                    }
                }else{
                  collection.document(notification.id).set(notification, SetOptions.merge())
                }

                pushTask.continueWith{ task ->
                    if(task.isSuccessful){
                        sendNotification(notification.toPushNotification(TOPIC))
                    }
                    else{
                        continuation.resumeWithException(RuntimeException("Fail to put notification"))
                    }
                }
            }
        }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG, "Response: $response")
            }else{
                Log.e(TAG, "Lỗi" + response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }

    private fun uploadPhoto(notification: NotificationData): Task<Uri> {
        notification.image?.let { compressPhoto(it) }
        val storageRef = storageRef.child(notification.id)
        return storageRef.putFile(Uri.parse(notification.image))
            .continueWithTask { uploadTask ->
                uploadTask.result?.storage?.downloadUrl
            }
    }

    private fun compressPhoto(path: String) {
        val imgFile = File(path.substringAfter("file://"))
        val bos = ByteArrayOutputStream()
        val bmp = BitmapFactory.decodeFile(imgFile.absolutePath)
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos)
        val fos = FileOutputStream(imgFile)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
    }

    private fun uploadFile(notification: NotificationData) {
        uploadPhoto(notification).continueWithTask { urlTask ->
            File(notification.image).delete()
            notification.image = urlTask.result.toString()
            fireStore.collection(NOTIFICATION_KEY)
                .document(notification.id)
                .update(IMAGE_URL_KEY, notification.image)
        }.addOnFailureListener {
            throw RuntimeException("Fail to upload.")
        }
    }

    companion object {
        const val NOTIFICATION_KEY = "notifications"
        const val USER_ID_KEY = "userId"
        const val ID_KEY = "id"
        const val IMAGE_URL_KEY = "image"
        const val TAG = "NOTIFICATION"
        const val TOPIC = "/topics/myTopic2"
    }
}