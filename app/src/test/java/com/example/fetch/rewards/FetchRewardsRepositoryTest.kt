package com.example.fetch.rewards

import com.example.fetch.rewards.data.FetchRewardsApiService
import com.example.fetch.rewards.data.FetchRewardsRepositoryImpl
import com.example.fetch.rewards.data.Resource
import com.example.fetch.rewards.domain.FetchRewardsRepository
import com.example.fetch.rewards.domain.ProductItem
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class FetchRewardsRepositoryTest {
    /**
     * @see [https://github.com/square/okhttp/tree/master/mockwebserver]
     */
    private val mockWebServer = MockWebServer()
    /**
     * @see [https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/README.md]
     */
    private val dispatcher = StandardTestDispatcher()

    private val fetchRewardsApiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .validateEagerly(true)
        .addConverterFactory(
            Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
        )
        .build()
        .create(FetchRewardsApiService::class.java)

    /**
     * Because this is a unit test and not an instrumented test,
     * there is no need for dependency injection.
     * [FetchRewardsRepository] can simply be instantiated directly by passing
     * the local test versions of its dependencies into the constructor.
     */
    private val fetchRewardsRepository: FetchRewardsRepository =
        FetchRewardsRepositoryImpl(fetchRewardsApiService, dispatcher)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Mocks the API response using the provided request data,
     * executes the call, collects the result, then validates that
     * the given JSON response body scenario returns an empty list.
     *
     * @see mockWebServer
     */
    @Test
    fun testGetEmptyProductItems() = runTest(dispatcher) {
        mockWebServer.setupResponse(body = "[]")
        val resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Success)
        assertTrue((resource as Resource.Success).data.isEmpty())
    }

    /**
     * Mocks the API response using the provided test data file,
     * executes the call, collects the result, then validates that
     * the resulting list size matches the expected value,
     * and that the property values of each item in the list matches the expected value.
     *
     * @see mockWebServer
     */
    @Test
    fun testGetValidProductItems() = runTest(dispatcher) {
        mockWebServer.setupResponse("product_items.json")
        val resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Success)
        val productItems = (resource as Resource.Success).data
        assertEquals(6, productItems.size)

        val expectedProductItems = listOf(
            ProductItem(276, 1, "Item 276"),
            ProductItem(684, 1, "Item 684"),
            ProductItem(906, 2, "Item 906"),
            ProductItem(680, 3, "Item 680"),
            ProductItem(534, 4, "Item 534"),
            ProductItem(808, 4, "Item 808")
        )
        productItems.forEachIndexed { index, item ->
            assertEquals(expectedProductItems[index], item)
        }
    }

    /**
     * Mocks the API response using the provided request data,
     * executes the call, collects the result, then validates that
     * modifying the [ProductItem] list data in the following invalid ways
     * invokes the associated errors or conditions.
     */
    @Test
    fun testGetInvalidProductItems() = runTest(dispatcher) {
        mockWebServer.setupResponse(body = "{}")

        var resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Exception)

        mockWebServer.setupResponse(body = "{\"invalid_key\":[]}")
        resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Exception)

        mockWebServer.setupResponse(responseCode = 404)
        resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Error)
        assertEquals(404, (resource as Resource.Error).code)

        mockWebServer.setupResponse(fileName = "product_items_malformed.json")
        resource = fetchRewardsRepository.getProductItems()
        assertTrue(resource is Resource.Exception)
    }

    /**
     * Extension function of [MockWebServer] that allows the caller
     * to enqueue a [MockResponse] using the optional parameters provided.
     * A string representation of the response body is prioritized over a local JSON resource file name.
     * A 500 response is returned if there is any issue reading or processing the given test data.
     * [ClassLoader] is intentionally used here to get the local resource,
     * so that [FetchRewardsRepositoryTest] remains independent of the need for an Android [Context].
     */
    private fun MockWebServer.setupResponse(fileName: String = "", body: String? = null, responseCode: Int = 200) {
        try {
            val json = body ?: javaClass.classLoader?.getResourceAsStream(fileName)?.bufferedReader().use { it?.readText() } ?: ""
            mockWebServer.enqueue(MockResponse().setBody(json).setResponseCode(responseCode))
        } catch (e: Exception) {
            e.printStackTrace()
            mockWebServer.enqueue(MockResponse().setResponseCode(500))
        }
    }
}