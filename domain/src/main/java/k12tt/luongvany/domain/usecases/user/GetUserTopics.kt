package k12tt.luongvany.domain.usecases.user

import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.UserRepo
import kotlinx.coroutines.flow.Flow

open class GetUserTopics (val repo: UserRepo) {
    fun execute(): Flow<List<Topics>>{
        return repo.getUserTopics()
    }
}