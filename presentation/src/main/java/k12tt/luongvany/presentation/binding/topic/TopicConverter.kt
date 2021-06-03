package k12tt.luongvany.presentation.binding.topic

import k12tt.luongvany.domain.entities.Topics

object TopicConverter {
    fun fromData(topic: Topics): TopicsBinding {
        return TopicsBinding().apply {
            nameTopic = topic.name
            listTopic = topic.topics
        }
    }

    fun toData(topicBinding: TopicsBinding): Topics {
        return Topics(
            name = topicBinding.nameTopic
        ).apply {
            topics.addAll(topicBinding.listTopic)
        }
    }
}