package k12tt.luongvany.presentation.viewmodel.notification

import android.util.Log
import androidx.lifecycle.*
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.usecases.notification.PushNotificationUseCase
import k12tt.luongvany.domain.usecases.topic.GetTopicUseCase
import k12tt.luongvany.presentation.TwoDataViewState
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationConverter
import k12tt.luongvany.presentation.binding.notification.NotificationTypeBinding
import k12tt.luongvany.presentation.binding.topic.TopicConverter
import k12tt.luongvany.presentation.binding.topic.TopicsBinding
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationFormViewModel(
    private val saveNotificationUseCaseUseCase: PushNotificationUseCase,
    private val topicsViewModel: GetTopicUseCase
) : ViewModel(), LifecycleObserver {

    private val saveNotifiactionEvent = SingleLiveEvent<ViewState<Unit>>()
    private val notification = MutableLiveData<NotificationBinding>(NotificationBinding())

    fun state(): LiveData<ViewState<Unit>> = saveNotifiactionEvent
    fun notification(): LiveData<NotificationBinding> = notification

    private val stateData = MutableLiveData<ViewState<List<TopicsBinding>>>()
    fun stateData(): LiveData<ViewState<List<TopicsBinding>>> = stateData

    fun saveNotification(topics: List<Topics>) {
        notification.value?.let {
            saveNotifiactionEvent.postValue(ViewState(ViewState.Status.LOADING))
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        saveNotificationUseCaseUseCase.execute(NotificationConverter.toData(it), topics)
                    }
                    saveNotifiactionEvent.postValue(ViewState(ViewState.Status.SUCCESS))
                } catch (e: Exception) {
                    saveNotifiactionEvent.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        } ?: saveNotifiactionEvent.postValue(
            ViewState(
                ViewState.Status.ERROR,
                error = IllegalArgumentException("Notification is null")
            )
        )
    }

    fun onTypeChanged(notificationType: NotificationTypeBinding, isChecked: Boolean) {
        if (isChecked) {
            notification.value?.notificationType = notificationType
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun loadTopics() {
        if (stateData.value == null) {
            viewModelScope.launch {
                stateData.postValue(ViewState(ViewState.Status.LOADING))
                try {
                    topicsViewModel.execute()
                        .flowOn(Dispatchers.IO)
                        .collect{topics ->
                            topicsViewModel.execute()
                                .flowOn(Dispatchers.IO)
                                .collect{topics ->
                                    val userTopicsBinding = topics.map { TopicConverter.fromData(it)}
                                    stateData.postValue(ViewState<List<TopicsBinding>>(ViewState.Status.SUCCESS, userTopicsBinding))
                                }
                        }
                } catch (e: Exception) {
                    stateData.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        }
    }
}