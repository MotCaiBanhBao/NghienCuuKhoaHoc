package k12tt.luongvany.presentation

import android.content.Intent
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.*
import k12tt.luongvany.domain.usecases.notification.ListNotificationsUseCase
import k12tt.luongvany.presentation.binding.NotificationBinding
import k12tt.luongvany.presentation.binding.NotificationConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NotificationListViewModel (
    private val loadNotificationsUseCase: ListNotificationsUseCase,
) : ViewModel(), LifecycleObserver {

    private val state = MutableLiveData<ViewState<List<NotificationBinding>>>()

    fun state(): LiveData<ViewState<List<NotificationBinding>>> = state

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadNotifications() {
        if (state.value == null) {
            viewModelScope.launch {
                state.postValue(ViewState(ViewState.Status.LOADING))
                try {
                    loadNotificationsUseCase.execute()
                        .flowOn(Dispatchers.IO)
                        .collect{notifications ->
                            val notificationsBinding = notifications.map { notification -> NotificationConverter.fromData(notification) }
                            state.postValue(ViewState(ViewState.Status.SUCCESS, notificationsBinding))
                        }
                } catch (e: Exception) {
                    state.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        }
    }

}
