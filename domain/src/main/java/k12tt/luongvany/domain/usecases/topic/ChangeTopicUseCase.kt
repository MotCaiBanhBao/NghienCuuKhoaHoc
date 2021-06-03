package k12tt.luongvany.domain.usecases.topic

import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.TopicRepo

open class ChangeTopicUseCase(private val repo: TopicRepo) {
    suspend fun execute(topics: List<Topics>, oldTopics: List<Topics>){
        return repo.changeTopic(topics, oldTopics)
    }
}