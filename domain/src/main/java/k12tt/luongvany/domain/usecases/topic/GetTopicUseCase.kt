package k12tt.luongvany.domain.usecases.topic

import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.TopicRepo
import kotlinx.coroutines.flow.Flow

open class GetTopicUseCase(private val repo: TopicRepo) {
    fun execute(): Flow<List<Topics>>{
        return repo.getTopic()
    }

}