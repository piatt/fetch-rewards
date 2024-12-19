package com.example.fetch.rewards.domain

import com.example.fetch.rewards.data.ProductItem
import com.example.fetch.rewards.data.Resource

interface FetchRewardsRepository {
    suspend fun getProductItems(refresh: Boolean = false): Resource<List<ProductItem>>
}