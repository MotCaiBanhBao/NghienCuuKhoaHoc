package k12tt.luongvany.data.model.message

import java.util.*

data class MessageData(
    var id: String = "",
    var timestamp: Date = Date(),
    var userName: String = "",
    var userUid: String = "",
    var context: String = "",
    var photoUrl: String = "",
)
