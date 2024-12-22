package com.example.fetch.rewards.data

import com.example.fetch.rewards.domain.FetchRewardsRepository
import com.example.fetch.rewards.domain.ProductItem
import com.example.fetch.rewards.domain.toProductItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Data-access class that encapsulates all API access, provides accessors to users,
 * and uses dependency injection to allow for easy component swapping and testing.
 */
class FetchRewardsRepositoryImpl @Inject constructor(
    private val apiService: FetchRewardsApiService,
    private val dispatcher: CoroutineDispatcher
) : FetchRewardsRepository {
    /**
     * Fetches a [ProductItem] list from the API using the provided
     * coroutine dispatcher, then wraps the result in a [Resource] subclass
     * that is dependent on the success or failure of the call.
     *
     * @see FetchRewardsApiService.getProductItems
     */
    override suspend fun getProductItems(): Resource<List<ProductItem>> {
        return try {
            val response = withContext(dispatcher) {
                apiService.getProductItems()
            }
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(
                    response.body()!!.map { it.toProductItem() }.toList()
                )
            } else {
                Resource.Error(response.code(), response.message())
            }
        } catch (e: HttpException) {
            Resource.Error(e.code(), e.message())
        } catch (e: NoNetworkException) {
            Resource.Exception(e)
        } catch (e: Throwable) {
            Resource.Exception(e)
        }
    }
}