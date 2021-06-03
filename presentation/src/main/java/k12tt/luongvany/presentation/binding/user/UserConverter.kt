package k12tt.luongvany.presentation.binding.user

import k12tt.luongvany.domain.entities.User

object UserConverter {
    fun fromData(user: User): UserBinding {
        return UserBinding().apply {
            nameUser = user.name
            uid = user.uid
            emailAdress = user.emailAdress
            language = user.language
            isAdmin = user.isAdmin
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