package k12tt.luongvany.domain.entities

data class User(
    var uid: String? = "",
    var avatarUri: String? = "",
    var name: String? = "",
    var emailAdress: String? = "",
    var language: String? = "en",
    var isAdmin: Boolean? = false,
)
