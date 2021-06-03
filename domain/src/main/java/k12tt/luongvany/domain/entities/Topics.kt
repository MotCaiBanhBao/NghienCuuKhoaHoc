package k12tt.luongvany.domain.entities

data class Topics(
    var name: String = "",
){
    var topics: MutableList<Map<String, String>> = mutableListOf()
}