package k12tt.luongvany.presentation

import androidx.lifecycle.*
import k12tt.luongvany.domain.usecases.notification.PushNotification
import k12tt.luongvany.presentation.binding.NotificationBinding
import k12tt.luongvany.presentation.binding.NotificationConverter
import k12tt.luongvany.presentation.binding.NotificationTypeBinding
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationFormViewModel(
    private val saveBookUseCase: PushNotification
) : ViewModel(), LifecycleObserver {

//    var tempImageFile: File? = null
//        set(value) {
//            deleteTempPhoto()
//            field = value
//            shouldDeleteImage = true
//        }
//   val publishers = listOf(
//        Publisher("1", "Novatec"),
//        Publisher("2", "Outra")
//    )

    private var shouldDeleteImage: Boolean = true

    private val saveNotifiactionEvent = SingleLiveEvent<ViewState<Unit>>()
    private val notification = MutableLiveData<NotificationBinding>(NotificationBinding())

    fun state(): LiveData<ViewState<Unit>> = saveNotifiactionEvent
    fun notification(): LiveData<NotificationBinding> = notification

    fun saveNotification() {
        notification.value?.let {
            saveNotifiactionEvent.postValue(ViewState(ViewState.Status.LOADING))
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        saveBookUseCase.execute(NotificationConverter.toData(it))
                    }
                    shouldDeleteImage = false
                    saveNotifiactionEvent.postValue(ViewState(ViewState.Status.SUCCESS))
                } catch (e: Exception) {
                    saveNotifiactionEvent.postValue(ViewState(ViewState.Status.ERROR, error = e))
                }
            }
        } ?: saveNotifiactionEvent.postValue(ViewState(ViewState.Status.ERROR,
            error = IllegalArgumentException("Notification is null")))
    }

    /**
     * This method should be called to edit book
     */

    fun onMediaTypeChanged(notificationType: NotificationTypeBinding, isChecked: Boolean) {
        if (isChecked) {
            notification.value?.notificationType = notificationType
        }
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    private fun deleteTempPhoto() {
//        if (shouldDeleteImage) {
//            tempImageFile?.let {
//                if (it.exists())
//                    viewModelScope.launch(Dispatchers.IO) {
//                        it.delete()
//                    }
//            }
//        }
//    }
}
