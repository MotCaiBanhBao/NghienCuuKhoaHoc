package k12tt.luongvany.presentation

import androidx.lifecycle.*
import k12tt.luongvany.domain.usecases.notification.ViewNotificationDetailUseCase
import k12tt.luongvany.presentation.binding.NotificationBinding
import k12tt.luongvany.presentation.binding.NotificationConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class NotificationDetailsViewModel(private val notificationId: String,
                                   private val viewNotificationDetailsUseCase: ViewNotificationDetailUseCase
                                   ): ViewModel() {

    private val state: MutableLiveData<ViewState<NotificationBinding>> = MutableLiveData()
    val notification = Transformations.map(state) { it.data }

    init {
        loadNotification()
    }

    fun getState(): LiveData<ViewState<NotificationBinding>> = state

    fun loadNotification() {
        viewModelScope.launch {
            state.postValue(ViewState(ViewState.Status.LOADING))
            try {
                viewNotificationDetailsUseCase.execute(notificationId)
                    .flowOn(Dispatchers.IO)
                    .collect { notification ->
                        if (notification != null) {
                            val notificationBinding = NotificationConverter.fromData(notification)
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