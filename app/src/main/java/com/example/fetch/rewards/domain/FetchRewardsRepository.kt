package com.example.fetch.rewards.domain

import com.example.fetch.rewards.data.Resource

interface FetchRewardsRepository {
    suspend fun getProductItems(): Resource<List<ProductItem>>
}