package k12tt.luongvany.data.mapper.user

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.mapper.topic.TopicMapper
import k12tt.luongvany.data.model.user.UserData
import k12tt.luongvany.domain.entities.User

class UserMapper: Mapper<UserData, User> {
    override fun map(source: UserData): User {
        return User(
            uid = source.uid,
            name =  source.name,
            language = source.language,
            emailAdress = source.emailAdress,
            isAdmin = source.isAdmin,
        )
    }
}
