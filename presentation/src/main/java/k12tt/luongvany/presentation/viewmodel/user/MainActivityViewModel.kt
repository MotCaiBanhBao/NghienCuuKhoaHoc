package k12tt.luongvany.presentation.viewmodel.user

import androidx.lifecycle.*
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.usecases.topic.GetTopicUseCase
import k12tt.luongvany.domain.usecases.user.ReSubcribeUseCase
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(
    private val reSubcribeUseCase: ReSubcribeUseCase,
) : ViewModel(), LifecycleObserver {
    private val initUserEvent = SingleLiveEvent<ViewState<Unit>>()
    fun state(): LiveData<ViewState<Unit>> = initUserEvent

    fun reSub() {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        reSubcribeUseCase.execute()
                    }
                } catch (e: Exception) {
                }
            }
    }
}