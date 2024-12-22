package com.example.fetch.rewards.data

import retrofit2.Response
import retrofit2.http.GET

interface FetchRewardsApiService {
    /**
     * Primary FetchRewards endpoint used to fetch a [ProductItemResponse] list.
     * In a production app, products would be fetched via query and paginated for efficiency.
     */
    @GET("hiring.json")
    suspend fun getProductItems(): Response<List<ProductItemResponse>>
}