package k12tt.luongvany.presentation.viewmodel.user

import androidx.lifecycle.*
import k12tt.luongvany.domain.usecases.notification.PushNotificationUseCase
import k12tt.luongvany.domain.usecases.notification.ViewNotificationDetailUseCase
import k12tt.luongvany.domain.usecases.user.GetUserUseCase
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationConverter
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding
import k12tt.luongvany.presentation.binding.user.UserBinding
import k12tt.luongvany.presentation.binding.user.UserConverter
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailViewModel (private val userId: String,
                           private val viewNotificationDetailsUseCase: GetUserUseCase
): ViewModel() {

    private val state: MutableLiveData<ViewState<UserBinding>> = MutableLiveData()
    val notification = Transformations.map(state) { it.data }

    init {
        loadUser()
    }
    fun getState(): LiveData<ViewState<UserBinding>> = state

    fun loadUser() {
        viewModelScope.launch {
            state.postValue(ViewState(ViewState.Status.LOADING))
            try {
                viewNotificationDetailsUseCase.execute(userId)
                    .flowOn(Dispatchers.IO)
                    .collect { userInfo ->
                        if (userInfo != null) {
                            val notificationBinding = UserConverter.fromData(userInfo)
                            state.postValue(ViewState(ViewState.Status.SUCCESS, notificationBinding))
                        } else {
                            state.postValue(
                                ViewState(
                                    ViewState.Status.ERROR,
                                    error = RuntimeException("Notification not found")
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                state.postValue(ViewState(ViewState.Status.ERROR, error = e))
            }
        }
    }
}