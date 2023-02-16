package com.spark.fastplayer.di

import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.presentation.splash.SplashViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class ViewModelsModule {

    @Provides
    fun providesSplashViewModel(coroutineContextProvider: CoroutineContextProvider) = SplashViewModel(coroutineContextProvider)

    @Provides
    fun providesCoroutineContext() = object: CoroutineContextProvider {}

}
