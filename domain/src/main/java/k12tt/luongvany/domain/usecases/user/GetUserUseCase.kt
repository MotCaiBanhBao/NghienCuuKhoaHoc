package k12tt.luongvany.domain.usecases.user

import k12tt.luongvany.domain.entities.User
import k12tt.luongvany.domain.repositories.UserRepo
import kotlinx.coroutines.flow.Flow

open class GetUserUseCase(private val repo: UserRepo) {
    fun execute(userId: String): Flow<User?>{
        return repo.getUser(userId)
    }
}