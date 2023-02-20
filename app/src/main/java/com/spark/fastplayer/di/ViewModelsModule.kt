package com.spark.fastplayer.di

import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.domain.repository.EPGRepository
import com.spark.fastplayer.presentation.epg.EPGViewModel
import com.spark.fastplayer.presentation.splash.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.openapitools.client.apis.EpgApi

@Module
@InstallIn(ActivityRetainedComponent::class)
class ViewModelsModule {

    @Provides
    fun providesSplashViewModel(coroutineContextProvider: CoroutineContextProvider) = SplashViewModel(coroutineContextProvider)

    @Provides
    fun providesCoroutineContext() = object: CoroutineContextProvider {}

    @Provides
    fun providesEPGViewModel(epgRepository: EPGRepository,
                             coroutineContextProvider: CoroutineContextProvider,
                             epgApi: EpgApi) = EPGViewModel(epgRepository, coroutineContextProvider,epgApi)

}
