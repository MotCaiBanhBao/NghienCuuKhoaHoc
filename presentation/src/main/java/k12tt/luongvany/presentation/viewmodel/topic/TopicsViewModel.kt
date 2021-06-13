package k12tt.luongvany.presentation.viewmodel.topic

import android.util.Log
import androidx.lifecycle.*
import k12tt.luongvany.domain.entities.Topics
import k12tt.luongvany.domain.usecases.topic.ChangeTopicUseCase
import k12tt.luongvany.domain.usecases.topic.GetTopicUseCase
import k12tt.luongvany.domain.usecases.user.GetUserTopics
import k12tt.luongvany.presentation.TwoDataViewState
import k12tt.luongvany.presentation.ViewState
import k12tt.luongvany.presentation.binding.topic.TopicsBinding
import k12tt.luongvany.presentation.binding.topic.TopicConverter
import k12tt.luongvany.presentation.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TopicsViewModel(private val topicsViewModel: GetTopicUseCase,
                      private val topicsUseCase: GetUserTopics,
                      private val changeTopicUseCase: ChangeTopicUseCase
): ViewModel(), LifecycleObserver {
    private lateinit var topics: MutableLiveData<List<Topics>>
    private val state = MutableLiveData<TwoDataViewState<List<TopicsBinding>>>()
    private val changeTopicEvent = SingleLiveEvent<ViewState<Unit>>()
    fun changeTopicState(): LiveData<ViewState<Unit>> = changeTopicEvent
    fun state(): LiveData<TwoDataViewState<List<TopicsBinding>>> = state

    fun isCheck(index: Int): Boolean{
        val topic = when(index){
            1 -> "KhoaKyThuat"
            2 -> "KhoaKinhTe"
            else -> "KhoaSuPham"
        }

        state().value?.userData?.map {
            if (it.nameTopic == topic)
                return true
        }
        return false
    }

    fun changeTopic(topics: List<Topics>, oldTopic: List<TopicsBinding>?){
        changeTopicEvent.postValue(ViewState(ViewState.Status.LOADING))
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    if (oldTopic != null) {
                        changeTopicUseCase.execute(topics, oldTopic.map {
                            TopicConverter.toData(it)
                        })
                    }
                }
                changeTopicEvent.postValue(ViewState(ViewState.Status.SUCCESS))
            } catch (e: Exception) {
                changeTopicEvent.postValue(ViewState(ViewState.Status.ERROR, error = e))
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun loadTopics() {
            Log.d("TEST", "TopicsRunging")
            viewModelScope.launch {
                state.postValue(TwoDataViewState(TwoDataViewState.Status.LOADING))
                try {
                    topicsViewModel.execute()
                        .flowOn(Dispatchers.IO)
                        .collect{topics ->
                            val topicsBinding = topics.map {TopicConverter.fromData(it)}
                            topicsUseCase.execute()
                                .flowOn(Dispatchers.IO)
                                .collect{topics ->
                                    val userTopicsBinding = topics.map {TopicConverter.fromData(it)}
                                    Log.d("TEST", userTopicsBinding.toString())
                                    state.postValue(TwoDataViewState<List<TopicsBinding>>(TwoDataViewState.Status.SUCCESS).apply {
                                        userData = userTopicsBinding
                                        defaultData = topicsBinding
                                    })
                                }
                        }
                } catch (e: Exception) {
                    state.postValue(TwoDataViewState(TwoDataViewState.Status.ERROR, error = e))
                }
            }
    }

}