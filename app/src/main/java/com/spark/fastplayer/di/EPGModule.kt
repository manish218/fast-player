package com.spark.fastplayer.di


import android.content.Context
import com.spark.fastplayer.BuildConfig
import com.spark.fastplayer.data.pefs.DataStoreManager
import com.spark.fastplayer.data.repository.EPGRepositoryImpl
import com.spark.fastplayer.domain.repoisitory.EPGRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.apis.PlaybackInfoApi
import javax.inject.Named

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
    fun providesEPGRepository(epgApi: EpgApi, playbackInfoApi: PlaybackInfoApi): EPGRepository =
        EPGRepositoryImpl(epgApi, playbackInfoApi)

    @Provides
    fun providesPlaybackInfoAPI(
        @Named(BASE_URL) baseUrl: String,
        okHttpClient: OkHttpClient,
        @Named(X_API_KEY) apiKey: String
    ) = PlaybackInfoApi(baseUrl, okHttpClient, apiKey)

    @Provides
    fun providesEPGApi(
        @Named(BASE_URL) baseUrl: String,
        okHttpClient: OkHttpClient,
        @Named(X_API_KEY) apiKey: String
    ) = EpgApi(baseUrl, okHttpClient, apiKey)

    @Provides
    fun providesDataStoreManager(@ApplicationContext context: Context) = DataStoreManager(context)

    @Provides
    @Named(BASE_URL)
    fun providesBaseUrl() = BuildConfig.API_URL

    @Provides
    @Named(X_API_KEY)
    fun providesAPIKEY() = BuildConfig.X_API_KEY


    companion object {
        private const val X_API_KEY = "x-api-key"
        private const val BASE_URL = "base-url"
    }
}