package com.example.fetch.rewards.di

import android.content.Context
import com.example.fetch.rewards.data.NetworkConnectivityInterceptor
import com.example.fetch.rewards.data.FetchRewardsApiService
import com.example.fetch.rewards.data.FetchRewardsRepositoryImpl
import com.example.fetch.rewards.domain.FetchRewardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module that provides dependencies scoped to the application.
 *
 * @see [https://developer.android.com/training/dependency-injection/hilt-android]
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * NOTE: In a production app, this value would be based on environment,
     * and would be made available alongside other environment variables via a provider.
     */
    private const val API_BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

    /**
     * Provides a singleton Retrofit instance for use by API services within the app.
     *
     * @see NetworkConnectivityInterceptor
     * @see [https://github.com/square/retrofit/tree/trunk/retrofit-converters/kotlinx-serialization]
     */
    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(NetworkConnectivityInterceptor(context))
            .build()
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .validateEagerly(true)
            .addConverterFactory(
                Json.asConverterFactory(MediaType.get("application/json; charset=UTF8"))
            )
            .build()
    }

    @Provides
    fun provideFetchRewardsApiService(retrofit: Retrofit) : FetchRewardsApiService {
        return retrofit.create(FetchRewardsApiService::class.java)
    }

    @Provides
    fun provideFetchRewardsRepository(
        apiService: FetchRewardsApiService,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ) : FetchRewardsRepository {
        return FetchRewardsRepositoryImpl(apiService, dispatcher)
    }

    /**
     * The annotations and providers below allow the desired qualifier to be used
     * inside of an injectable constructor to tell Hilt which specific dependency to inject.
     *
     * @see [https://developer.android.com/training/dependency-injection/hilt-android#multiple-bindings]
     */

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}