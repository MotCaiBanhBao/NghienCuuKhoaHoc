package k12tt.luongvany.data_fb

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import k12tt.luongvany.data.model.message.MessageData
import k12tt.luongvany.data.model.notification.NotificationData
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.data.model.user.UserData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.data_fb.entities.PushNotification
import k12tt.luongvany.data_fb.util.toPushNotification
import k12tt.luongvany.domain.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


internal class FBDataImpl : FBData{
    private val fbAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun loadNotifications(): Flow<List<NotificationData>> {
        return callbackFlow {
            val subscription = fireStore.collection(NOTIFICATION_KEY).orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{ snapshot, e ->
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

    override fun loadMessage(notificationId: String): Flow<List<MessageData>> {
        return callbackFlow {
            val subscription = fireStore.collection("notifications").document(notificationId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timestamp(123L, 0)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val notifications = snapshot.map { document ->
                        document.toObject(MessageData::class.java)
                    }
                    offer(notifications)
                } else {
                    offer(emptyList<MessageData>())
                }
            }
            awaitClose {
                subscription.remove()
            }
        }
    }

    override suspend fun pushMessage(message: MessageData, notificationId: String) {
        return suspendCoroutine { continuation ->
            val currentUser = fbAuth.currentUser
            if (currentUser == null){
                continuation.resumeWithException(RuntimeException("Unauthorized used."))
            }else{
                val db = fireStore
                val collection = db.collection("notifications").document(notificationId).collection("messages")
                    collection.add(message)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { e -> continuation.resumeWithException(e) }
            }
        }
    }

    override fun getUserNotification(): Flow<List<NotificationData>> {
        return callbackFlow {

            val currentUser = fbAuth.currentUser
            val subscription = fireStore.collection(NOTIFICATION_USER).document(currentUser.uid).collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener{ snapshot, e ->
                    if(e != null){
                        Timestamp(123L, 0)
                        return@addSnapshotListener
                    }
                    if(snapshot != null && !snapshot.isEmpty){
                        val notifications = snapshot.map {document ->
                            Log.d("Test", document.toString())
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

    override suspend fun changeTopic(topics: List<TopicsData>, oldTopics: List<TopicsData>) {
        return suspendCoroutine { continuation ->
            val currentUser = fbAuth.currentUser
            if (currentUser == null){
                continuation.resumeWithException(RuntimeException("Unauthorized used."))
            }else{
                val db = fireStore
                val collection = db.collection(NOTIFICATION_USER).document(currentUser.uid).collection(USER_TOPIC)
                for(topicsItem in topics){
                    collection.document(topicsItem.name).delete()
                    for(item in topicsItem.topics){
                        collection.document(topicsItem.name).set(item, SetOptions.merge())
                    }
                }
                unSub(oldTopics)
                subcribeTopic(topics)
                continuation.resume(Unit)
            }
        }
    }


    private fun unSub(topics: List<TopicsData>) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val firebaseMassaging = FirebaseMessaging.getInstance()
            for (topic in topics){
                for (item in topic.topics)
                    firebaseMassaging.unsubscribeFromTopic(item.values.toString().removePrefix("[").removeSuffix("]"))
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }

    private fun subcribeTopic(topics: List<TopicsData>) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val firebaseMassaging = FirebaseMessaging.getInstance()
            for (topic in topics){
                for(item in topic.topics){
                    firebaseMassaging.subscribeToTopic(item.values.toString().removePrefix("[").removeSuffix("]"))
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun pushNotification(notification: NotificationData, topics: List<TopicsData>) {
        return suspendCoroutine { continuation ->
            val currentUser = fbAuth.currentUser

            if (currentUser == null){
                continuation.resumeWithException(RuntimeException("Unauthorized used."))
            }else{
                val db = fireStore
                val collection = db.collection(NOTIFICATION_KEY)
                for (topic in topics){
                    for (item in topic.topics){
                        notification.target += item.keys.toString().removePrefix("[").removeSuffix("]") + " "
                    }
                }
                Log.d("TEST", notification.target)
                val pushTask = if(notification.id.isBlank()){
                    collection.add(notification).continueWithTask{ task ->
                        val doc = task.result
                        notification.id = doc?.id ?: UUID.randomUUID().toString()
                        doc?.update(mapOf(USER_ID_KEY to currentUser.uid, ID_KEY to notification.id))
                    }
                }else{
                  collection.document(notification.id).set(notification, SetOptions.merge())
                }

                pushTask
                    .continueWith{ task ->
                    if(task.isSuccessful){
                        for(topic in topics){
                            for (item in topic.topics){
                                sendNotification(notification.toPushNotification(item.values.toString().removePrefix("[").removeSuffix("]")))
                            }
                        }
                    } else{
                        continuation.resumeWithException(RuntimeException("Fail to put notification"))
                    }
                    }
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { e -> continuation.resumeWithException(e) }


                }
            }
        }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                val jObjError = JSONObject(response.body()!!.string())
                Log.d(TAG, "Response: $jObjError")
            }else{
                Log.e(TAG, "Lỗi ${response.errorBody()}")
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
    }

    override suspend fun initUser(topics: List<TopicsData>) {
        return suspendCoroutine { continuation ->
            val db = fireStore
            val currentUser = fbAuth.currentUser
            if(currentUser == null){
                continuation.resumeWithException(RuntimeException("Đăng nhập lỗi"))
            }else{
                val userData = User(currentUser.uid, currentUser.displayName, currentUser.email, fbAuth.languageCode, false)
                db.collection(NOTIFICATION_USER).document(userData.uid).set(userData).addOnFailureListener{ e->
                    continuation.resumeWithException(RuntimeException("Can create user", e))
                }.addOnSuccessListener {
                    val path = db.collection(NOTIFICATION_USER).document(userData.uid).collection(USER_TOPIC)
                    for(topic in topics){
                        path.document(topic.name).set(topic.topics)
                    }
                }
            }

        }
    }

    override fun getUser(userId: String): Flow<UserData?> {
        return callbackFlow {
            val subscription = fireStore.collection(USER_ID_KEY)
                .document(userId)
                .addSnapshotListener{ snapshot, e ->
                    if (e != null){
                        close(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val userData = snapshot.toObject(UserData::class.java)
                        userData?.let {
                            offer(it)
                        }
                    }else{
                        offer(null)
                    }
                }
            awaitClose{subscription.remove()}
        }
    }

    override fun getTopic(): Flow<List<TopicsData>> {
        return callbackFlow {
            val subscription = fireStore.collection(USER_TOPIC).addSnapshotListener{ snapshot, e ->
                if(e != null){
                    Timestamp(123L, 0)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty){
                    val topics = snapshot.map { document ->
                        TopicsData(document.id).apply {
                            for (item in document.data){
                                topics.add(mapOf(item.key to item.value.toString()))

                            }
                        }
                    }
                    offer(topics)
                }
                else{
                    offer(emptyList<TopicsData>())
                }
            }
            awaitClose{
                subscription.remove()
            }
        }
    }

    override fun getUserTopics(): Flow<List<TopicsData>> {
        return callbackFlow {
            val currentUser = fbAuth.currentUser
            val subscription = fireStore.collection(NOTIFICATION_USER).document(currentUser.uid).collection(USER_TOPIC).addSnapshotListener(){ snapshot, e ->
                if(e != null){
                    Timestamp(123L, 0)
                    return@addSnapshotListener
                }
                if(snapshot != null && !snapshot.isEmpty){
                    val topics = snapshot.map { document ->
                        TopicsData(document.id).apply {
                            for (item in document.data){
                                topics.add(mapOf(item.key to item.value as String))
                            }
                        }
                    }
                    offer(topics)
                }
                else{
                    offer(emptyList<TopicsData>())
                }
            }
            awaitClose{
                subscription.remove()
            }
        }
    }

    override suspend fun reSubcribe() {
        val currentUser = fbAuth.currentUser
        fireStore.collection(NOTIFICATION_USER).document(currentUser.uid).collection(USER_TOPIC).addSnapshotListener(){ snapshot, e ->
            if(e != null){
                Timestamp(123L, 0)
                return@addSnapshotListener
            }
            if(snapshot != null && !snapshot.isEmpty){
                val topics = snapshot.map { document ->
                    TopicsData(document.id).apply {
                        for (item in document.data){
                            topics.add(mapOf(item.key to item.value as String))
                        }
                    }
                }
                subcribeTopic(topics)
            }
        }

    }

    companion object {
        const val NOTIFICATION_KEY = "notifications"
        const val NOTIFICATION_USER = "users"
        const val USER_TOPIC = "topics"
        const val USER_ID_KEY = "userId"
        const val ID_KEY = "id"
        const val TAG = "NOTIFICATION"
        const val TOPIC = "/topics/myTopic2"
    }
}