package k12tt.luongvany.nghiencuukhoahoc.utils

//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.ImageDecoder
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//
//fun Uri.toBitmap(context: Context): Bitmap?{
//    var bitmap: Bitmap?
//    try {
//            if(Build.VERSION.SDK_INT < 28) {
//                bitmap = MediaStore.Images.Media.getBitmap(
//                    context.contentResolver, this
//                )
//            } else {
//                val source = ImageDecoder.createSource(context.contentResolver, this)
//                bitmap = ImageDecoder.decodeBitmap(source)
//        }
//    } catch (e: Exception) {
//        bitmap = null
//        e.printStackTrace()
//    }
//    return bitmap
//}