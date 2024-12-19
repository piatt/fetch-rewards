package com.example.fetch.rewards.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.rewards.data.ProductItem
import com.example.fetch.rewards.data.Resource
import com.example.fetch.rewards.domain.FetchRewardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Hilt and Android lifecycle powered view model used to
 * facilitate connection between the data layer and the UI layer,
 * while maintaining appropriate abstraction.
 *
 * @see [https://developer.android.com/training/dependency-injection/hilt-jetpack#viewmodels]
 * @see FetchRewardsRepository
 */
@HiltViewModel
class FetchRewardsViewModel @Inject constructor(
    private val repository: FetchRewardsRepository
) : ViewModel() {
    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)

    val viewState: StateFlow<ViewState> get() = _viewState

    fun getProductItems() {
        viewModelScope.launch {
            val newViewState = when (val resource = repository.getProductItems()) {
                is Resource.Success -> ViewState.ProductItems(resource.data)
                is Resource.Error -> ViewState.Error("Error: ${resource.message}")
                is Resource.Exception -> ViewState.Error("Error: ${resource.e.message}")
            }
            _viewState.emit(newViewState)
        }
    }
}

sealed class ViewState {
    object Loading : ViewState()
    data class Error(val message: String) : ViewState()
    data class ProductItems(val productItems: List<ProductItem>) : ViewState()
}