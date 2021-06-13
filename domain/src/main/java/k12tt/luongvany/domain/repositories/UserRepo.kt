package k12tt.luongvany.domain.repositories

import k12tt.luongvany.domain.entities.Notification
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getUserTopics(): Flow<List<Topics>>
    fun loadNotifications(): Flow<List<Notification>>
    suspend fun reSubcribe()
    suspend fun unSubcribe()
    fun getUser(userId: String): Flow<User?>
}