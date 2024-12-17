package com.example.fetch.rewards.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FetchRewardsApiService {
    /**
     * Primary FetchRewards endpoint used to fetch a [ProductItemResponse] list.
     * In a production app, products would be fetched via query and paginated for efficiency.
     */
    @GET("hiring.json")
    suspend fun getProductItems(): Response<List<ProductItemResponse>>

    /**
     * NOTE: This endpoint does not currently exist,
     * but is here to show a future state where a [ProductItemResponse]
     * could be fetched using [ProductItem.id] to display product details.
     */
    @GET("products/{id}")
    suspend fun getProductItem(@Path("id") itemId: String): Response<ProductItemResponse?>
}