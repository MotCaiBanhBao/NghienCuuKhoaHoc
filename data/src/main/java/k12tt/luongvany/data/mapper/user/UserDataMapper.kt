package k12tt.luongvany.data.mapper.user

import k12tt.luongvany.data.mapper.Mapper
import k12tt.luongvany.data.mapper.topic.TopicDataMapper
import k12tt.luongvany.data.model.user.UserData
import k12tt.luongvany.domain.entities.User

class UserDataMapper: Mapper<User, UserData> {
    override fun map(source: User): UserData {
        return UserData(
            uid = source.uid,
            name =  source.name,
            language = source.language,
            emailAdress = source.emailAdress,
            isAdmin = source.isAdmin,
        )
    }
}