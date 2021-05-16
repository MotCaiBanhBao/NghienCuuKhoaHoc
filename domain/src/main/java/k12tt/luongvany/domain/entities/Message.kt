package k12tt.luongvany.domain.entities

import java.util.*

data class Message(val time: Date? = Calendar.getInstance().time,
                   var context: String,
                   var photoUrl: String,
                   var imageUrl: String)
