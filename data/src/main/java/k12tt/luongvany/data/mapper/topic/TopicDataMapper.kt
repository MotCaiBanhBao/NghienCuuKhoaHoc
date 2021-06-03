package k12tt.luongvany.data.mapper.topic

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.model.topic.TopicsData
import k12tt.luongvany.domain.entities.Topics

class TopicDataMapper: Mapper<Topics, TopicsData> {
    override fun map(source: Topics): TopicsData {
        return TopicsData(
            source.name
        ).apply {
            topics = source.topics
        }
    }
}