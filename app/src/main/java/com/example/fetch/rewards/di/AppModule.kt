package com.example.fetch.rewards.di

import com.example.fetch.rewards.data.FetchRewardsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
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
     * Provides a singleton instance of [FetchRewardsApiService]
     * to any area of the app that wishes to inject it.
     *
     * @see [https://square.github.io/retrofit/]
     */
    @Singleton
    @Provides
    fun provideFetchRewardsApiService() : FetchRewardsApiService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .validateEagerly(true)
//            .addConverterFactory(
//                Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
//            )
            .build()
            .create(FetchRewardsApiService::class.java)
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