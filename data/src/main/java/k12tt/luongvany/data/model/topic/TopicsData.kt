package k12tt.luongvany.data.model.topic

data class TopicsData(var name: String = "")
{
     var topics: MutableList<Map<String, String>> = arrayListOf()
}