package com.example.fetch.rewards.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fetch.rewards.R
import com.example.fetch.rewards.domain.ProductItem

/**
 * Forms the basis for the UI for [MainActivity],
 * using the [viewModel] to create observable state that triggers recomposition,
 * and generating the views declaratively based on current state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: FetchRewardsViewModel,
    onProductItemClick: (id: Long) -> Unit
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    /**
     * Ensures that the view model is only called once to get product list content,
     * preventing unnecessary calls on configuration changes and recompositions.
     */
    var productItemsLoaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!productItemsLoaded) {
            productItemsLoaded = true
            viewModel.getProductItems()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                title = {
                    Text(text = stringResource(R.string.app_name))
                }
            )
        }
    ) { innerPadding ->
        /**
         * If there is data to display, generate a list view that displays the [ProductItem] list.
         * Otherwise, show a message based on current [FetchRewardsViewModel.ViewState] in the center of the screen.
         */
        /**
         * If there is data to display, generate a list view that displays the [ProductItem] list.
         * Otherwise, show a message based on current [FetchRewardsViewModel.ViewState] in the center of the screen.
         */
        when (viewState) {
            is ViewState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ViewState.DefaultError -> {
                CenterAlignedMessage(stringResource(R.string.default_error_message))
            }
            is ViewState.NetworkError -> {
                CenterAlignedMessage(stringResource(R.string.network_error_message))
            }
            is ViewState.Content -> {
                LazyColumn(Modifier.padding(innerPadding)) {
                    items((viewState as ViewState.Content).productItems) {
                        ProductItemRow(it, onProductItemClick)
                    }
                }
            }
        }
    }
}