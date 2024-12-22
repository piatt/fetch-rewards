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
                val rawProductItemList = response.body()!!.map { it.toProductItem() }.toList()
                Resource.Success(getAdjustedProductItems(rawProductItemList))
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

    /**
     * Mutates the given [items] list to satisfy the following requirements:
     * 1. Display all the items grouped by "listId"
     * 2. Sort the results first by "listId" then by "name" when displaying.
     * 3. Filter out any items where "name" is blank or null.
     */
    private fun getAdjustedProductItems(items: List<ProductItem>): List<ProductItem> {
        return items
            .filter { it.name.isNotBlank() }
            .sortedBy { it.name }
            .groupBy { it.listId }
            .toSortedMap()
            .values.flatten()
    }
}