package k12tt.luongvany.presentation

class TwoDataViewState<D>(
    val status: Status,
    val error: Throwable? = null
) {
    var userData: D? = null
    var defaultData: D? = null
    enum class Status {
        LOADING, SUCCESS, ERROR
    }
}
