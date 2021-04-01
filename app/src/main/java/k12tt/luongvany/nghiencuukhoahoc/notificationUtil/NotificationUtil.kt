package k12tt.luongvany.nghiencuukhoahoc.notificationUtil

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

fun createIntentListener(cls: Class<*>, context: Context): PendingIntent {

    val intent = Intent(context, cls)
    return PendingIntent.getActivity(context, 0, intent, 0)

}
