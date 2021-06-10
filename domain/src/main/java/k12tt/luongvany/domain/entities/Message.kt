package k12tt.luongvany.domain.entities

import java.util.*

data class Message(
    var id: String = "",
    var timestamp: Date = Date(),
    var userName: String,
    var userUid: String,
    var context: String,
    var photoUrl: String
    )
