package com.example.fetch.rewards.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fetch.rewards.R
import com.example.fetch.rewards.data.ProductItem
import com.example.fetch.rewards.ui.theme.FetchRewardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: FetchRewardsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchRewardsTheme {
                ProductListScreen(viewModel)
            }
        }
    }
}

/**
 * Forms the basis for the UI for [MainActivity],
 * using the [viewModel] to create observable state that triggers recomposition,
 * and generating the views declaratively based on current state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(viewModel: FetchRewardsViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

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
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = colorResource(id = android.R.color.white)
                    )
                }
            )
        }
    ) { scaffoldPadding ->
        /**
         * If there is data to display, generate a list view that displays the [ProductItem] list.
         * Otherwise, show a message based on current [FetchRewardsViewModel.ViewState] in the center of the screen.
         */
        when (viewState) {
            is ViewState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Loading",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
            is ViewState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (viewState as ViewState.Error).message,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
            is ViewState.ProductItems -> {
                LazyColumn(Modifier.padding(scaffoldPadding)) {
                    items((viewState as ViewState.ProductItems).productItems) {
                        ProductItemRow(it) {
                            /**
                             * NOTE: In a production app, clicking a row would launch the product details screen,
                             * which would be populated by the [ProductItem] matching the [ProductItem.id]
                             */
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemRow(productItem: ProductItem, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "ID: ${productItem.id}, ListID: ${productItem.listId}, Name: ${productItem.name}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}