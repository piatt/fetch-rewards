package com.example.fetch.rewards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.rewards.data.NoNetworkException
import com.example.fetch.rewards.data.Resource
import com.example.fetch.rewards.domain.FetchRewardsRepository
import com.example.fetch.rewards.domain.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hilt and Android lifecycle powered view model used to
 * facilitate connection between the domain and UI layers,
 * while maintaining appropriate abstraction.
 *
 * @see [https://developer.android.com/training/dependency-injection/hilt-jetpack#viewmodels]
 * @see FetchRewardsRepository
 */
@HiltViewModel
class FetchRewardsViewModel @Inject constructor(
    private val repository: FetchRewardsRepository
) : ViewModel() {
    /**
     * Initialize viewState as [ViewState.Loading] so that interstitial state
     * is visible to user while content is being fetched.
     */
    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)

    val viewState = _viewState.asStateFlow()

    /**
     * Asynchronously fetches a [ProductItem] list from the repository,
     * then wraps the result in a [ViewState] subclass
     * that is dependent on the [Resource] subclass received.
     *
     * @see FetchRewardsRepository.getProductItems
     */
    fun getProductItems() {
        println("TEST: VM getProductItems")
        viewModelScope.launch {
            val newViewState = when (val resource = repository.getProductItems()) {
                is Resource.Success -> ViewState.Content(resource.data)
                is Resource.Error -> ViewState.DefaultError
                is Resource.Exception -> {
                    if (resource.e is NoNetworkException)
                        ViewState.NetworkError
                    else
                        ViewState.DefaultError
                }
            }
            _viewState.emit(newViewState)
        }
    }
}

/**
 * Wrapper class representing common view states which allows the UI layer
 * to apply situational view logic, depending on the subclass emitted.
 */
sealed class ViewState {
    data object Loading : ViewState()
    data object DefaultError : ViewState()
    data object NetworkError : ViewState()
    data class Content(val productItems: List<ProductItem>) : ViewState()
}