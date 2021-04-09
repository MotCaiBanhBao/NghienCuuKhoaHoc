package k12tt.luongvany.nghiencuukhoahoc.database

import android.net.LinkAddress
import java.util.*

data class Notification(val id: UUID = UUID.randomUUID(),
                        var title: String,
                        var context: String,
                        var link: LinkAddress)