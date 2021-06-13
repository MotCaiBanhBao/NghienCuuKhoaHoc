package k12tt.luongvany.data.model.user

data class UserData (
    var uid: String? = "",
    var name: String? = "",
    var avatarUri: String? = "",
    var emailAdress: String? = "",
    var language: String? = "en",
    var isAdmin: Boolean? = false
)