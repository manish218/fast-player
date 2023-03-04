package com.spark.fastplayer.di


import com.spark.fastplayer.data.EPGRepositoryImpl
import com.spark.fastplayer.domain.EPGRepository
import com.spark.fastplayer.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.apis.PlaybackinfoApi

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
    fun providesPlaybackInfoAPI(baseUrl: String, okHttpClient: OkHttpClient) = PlaybackinfoApi(baseUrl, okHttpClient)

    @Provides
    fun providesEPGApi(baseUrl: String, okHttpClient: OkHttpClient) = EpgApi(baseUrl, okHttpClient)

    @Provides
    fun providesBaseUrl() = BuildConfig.API_URL
}