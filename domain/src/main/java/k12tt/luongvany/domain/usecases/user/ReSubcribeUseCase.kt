package k12tt.luongvany.domain.usecases.user

import k12tt.luongvany.domain.repositories.UserRepo

open class ReSubcribeUseCase(private val repository: UserRepo){
    suspend fun execute(){
        repository.reSubcribe()
    }
}