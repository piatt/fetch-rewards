package com.example.fetch.rewards.data

import com.example.fetch.rewards.domain.FetchRewardsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class FetchRewardsRepositoryImpl @Inject constructor(
    private val apiService: FetchRewardsApiService,
    private val dispatcher: CoroutineDispatcher
) : FetchRewardsRepository {
    private var productItemsMap = mutableMapOf<Long, ProductItem>()

    override suspend fun getProductItems(refresh: Boolean): Resource<List<ProductItem>> {
        try {
            val response = withContext(dispatcher) {
                apiService.getProductItems()
            }
            return if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.map { it.toProductItem() }.toList())
            } else {
                Resource.Error(response.code(), response.message())
            }
        } catch (e: HttpException) {
            return Resource.Error(e.code(), e.message())
        } catch (e: Throwable) {
            return Resource.Exception(e)
        }
    }
}