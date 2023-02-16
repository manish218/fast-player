package com.spark.fastplayer.presentation.epg

import com.spark.fastplayer.MainCoroutineRule
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.domain.repository.EPGRepository
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.openapitools.client.apis.EpgApi

@ExperimentalCoroutinesApi
class EPGViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val rule = MainCoroutineRule(testDispatcher)

    @MockK
    lateinit var epgApi: EpgApi

    @MockK
    lateinit var coroutineContextProvider: CoroutineContextProvider

    @MockK
    lateinit var repository: EPGRepository

    private lateinit var viewModel: EPGViewModel

    @Before
    fun setUp() {
        viewModel = EPGViewModel(repository, coroutineContextProvider, epgApi)
    }

    @Test
    fun epgViewModel_fetchData_SuccessTest()  {

        viewModel.getEPGData()

    }

}