package k12tt.luongvany.nghiencuukhoahoc.database

//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//
//class Notification private constructor(){
//
//    var notificationReference: DatabaseReference
//    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//
//    private object Holder{
//        val INSTANCE = Notification()
//    }
//
//    companion object{
//        val instance: Notification by lazy{
//            Holder.INSTANCE
//        }
//    }
//
//    init {
//        notificationReference =  database.getReference("Notification")
//    }
//
//    fun pushNotification(data: NotificationData){
//        notificationReference.child(data.id.toString()).setValue(data)
//    }
//
//}