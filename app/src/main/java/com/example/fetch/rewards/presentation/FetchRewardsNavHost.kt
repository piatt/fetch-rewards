package com.example.fetch.rewards.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute

/**
 * Creates and manages a navigation graph consisting of two screens,
 * a list and a detail screen, with the list screen being the default start destination.
 */
@Composable
fun FetchRewardsNavHost(navController: NavHostController) {
    NavHost(navController, ProductList) {
        composable<ProductList> {
            ProductListScreen(
                viewModel = hiltViewModel<FetchRewardsViewModel>(),
                onProductItemClick = {
                    navController.navigate(ProductDetail(it))
                }
            )
        }
        composable<ProductDetail> {
            ProductDetailScreen(
                id = it.toRoute<ProductDetail>().id,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}