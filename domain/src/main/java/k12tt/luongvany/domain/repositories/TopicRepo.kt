package k12tt.luongvany.domain.repositories

import k12tt.luongvany.domain.entities.Topics
import kotlinx.coroutines.flow.Flow

interface TopicRepo {
    fun getTopic(): Flow<List<Topics>>
    suspend fun changeTopic(topics: List<Topics>, oldTopics: List<Topics>)
}