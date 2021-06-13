package k12tt.luongvany.presentation.binding.user

import k12tt.luongvany.domain.entities.User

object UserConverter {
    fun fromData(user: User): UserBinding {
        return UserBinding().apply {
            nameUser = user.name.toString()
            uid = user.uid.toString()
            emailAdress = user.emailAdress.toString()
            language = user.language.toString()
            isAdmin = user.isAdmin == true
        }
    }

    fun toData(userBinding: UserBinding): User {
        return User(
            name = userBinding.nameUser,
            uid = userBinding.uid,
            emailAdress = userBinding.emailAdress,
            language = userBinding.language,
            isAdmin = userBinding.isAdmin
        )
    }
}