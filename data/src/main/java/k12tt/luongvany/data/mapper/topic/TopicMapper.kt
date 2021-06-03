package k12tt.luongvany.data.mapper.topic

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.domain.entities.Topics

class TopicMapper: Mapper<TopicsData, Topics> {
    override fun map(source: TopicsData): Topics {
        return Topics(
            source.name
        ).apply {
            topics = source.topics
        }
    }
}