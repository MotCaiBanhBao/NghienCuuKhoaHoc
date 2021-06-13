package k12tt.luongvany.presentation.viewmodel.notification

import android.util.Log
import androidx.lifecycle.*
import k12tt.luongvany.domain.entities.User
import k12tt.luongvany.domain.usecases.message.GetListMessageUseCase
import k12tt.luongvany.domain.usecases.message.PostMessageUseCase
import k12tt.luongvany.domain.usecases.notification.ViewNotificationDetailUseCase
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.binding.message.MessageBinding
import k12tt.luongvany.presentation.binding.message.MessageConverter
import k12tt.luongvany.presentation.binding.notification.NotificationBinding
import k12tt.luongvany.presentation.binding.notification.NotificationConverter
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class NotificationDetailsViewModel(private val notificationId: String,
                                   private val viewNotificationDetailsUseCase: ViewNotificationDetailUseCase,
                                   private val messageListUseCase: GetListMessageUseCase,
                                   private val messagePushUseCase: PostMessageUseCase
                                   ): ViewModel() {

    private val state: MutableLiveData<ViewState<NotificationBinding>> = MutableLiveData()
    private val saveMessageEvent = SingleLiveEvent<ViewState<Unit>>()

    private val messageState = MutableLiveData<ViewState<List<MessageBinding>>>()

    fun getMessageState(): LiveData<ViewState<List<MessageBinding>>> = messageState

    val messageValue = MutableLiveData<MessageBinding>(MessageBinding())
    val notification = Transformations.map(state) { it.data }

    init {

        loadNotification()
    }
    fun getState(): LiveData<ViewState<NotificationBinding>> = state

    fun pushMessage(user: User) {
        messageValue.value?.let {
            it.userUidMessage = user.uid.toString()
            it.userNameMessage = user.name.toString()
            it.photoUrlMessage = user.avatarUri.toString()
            saveMessageEvent.postValue(ViewState(ViewState.Status.LOADING))
            Log.d("TEST", it.photoUrlMessage)
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        messagePushUseCase.execute(MessageConverter.toData(it), notificationId)
                    }
                    saveMessageEvent.postValue(ViewState(ViewState.Status.SUCCESS))
                } catch (e: Exception) {
                    saveMessageEvent.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        } ?: saveMessageEvent.postValue(
            ViewState(
                ViewState.Status.ERROR,
                error = IllegalArgumentException("Nothing")
            )
        )
    }

    private fun loadMessage(id: String) {
        viewModelScope.launch {
            messageState.postValue(ViewState(ViewState.Status.LOADING))
            try {
                messageListUseCase.execute(id)
                    .flowOn(Dispatchers.IO)
                    .collect { messages ->
                        val messagesBinding = messages.map { message -> MessageConverter.fromData(message) }
                        messageState.postValue(ViewState(ViewState.Status.SUCCESS, messagesBinding))
                    }
            } catch (e: Exception) {
                messageState.postValue(ViewState(ViewState.Status.ERROR, error = e))
            }
        }
    }

    private fun loadNotification() {
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
        loadMessage(notificationId)
    }
}