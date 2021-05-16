package k12tt.luongvany.nghiencuukhoahoc.common

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.util.*
import android.text.format.DateFormat
import androidx.core.content.FileProvider

class FilePicker(val context: Context) {
    fun createTempFile(): File {
        val fileName = DateFormat.format("ddMMyyy_hhmmss", Date()).toString()
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$fileName.jpg")
    }

    fun uriFromFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "k12tt.luongvany.nghiencuukhoahoc.fileprovider",
            file)
    }
}
