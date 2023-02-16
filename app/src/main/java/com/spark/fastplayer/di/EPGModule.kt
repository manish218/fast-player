package com.spark.fastplayer.di

import com.spark.fastplayer.data.repository.EPGRepositoryImpl
import com.spark.fastplayer.domain.repository.EPGRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.EpgApi

@Module
@InstallIn(SingletonComponent::class)
class EPGModule {

    @Provides
    fun providesHttpClient() : OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun providesEPGRepository(): EPGRepository = EPGRepositoryImpl()

    @Provides
    fun providesEPGApi(baseUrl: String, okHttpClient: OkHttpClient) = EpgApi(baseUrl, okHttpClient)

    @Provides
    fun providesBaseUrl() = "https://0itfjctli1.execute-api.eu-west-2.amazonaws.com/prod-1"
}