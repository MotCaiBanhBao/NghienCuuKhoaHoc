package k12tt.luongvany.data.repository

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.repositories.TopicRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TopicRepositoryImpl (private val fbData: FBData,
                                    private val entityMapper: Mapper<TopicsData, Topics>,
                                    private val dataMapper: Mapper<Topics, TopicsData>
): TopicRepo {
    override fun getTopic(): Flow<List<Topics>> {
        return fbData.getTopic().map{it.map(entityMapper::map)}
    }

    override suspend fun changeTopic(topics: List<Topics>, oldTopics: List<Topics>) {
        return fbData.changeTopic(topics.map(dataMapper::map), oldTopics.map(dataMapper::map))
    }

}