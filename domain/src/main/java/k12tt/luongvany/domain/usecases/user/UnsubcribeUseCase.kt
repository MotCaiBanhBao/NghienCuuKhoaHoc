package k12tt.luongvany.domain.usecases.user

import k12tt.luongvany.domain.repositories.UserRepo

open class UnsubcribeUseCase(private val repository: UserRepo){
    suspend fun execute(){
        repository.unSubcribe()
    }
}