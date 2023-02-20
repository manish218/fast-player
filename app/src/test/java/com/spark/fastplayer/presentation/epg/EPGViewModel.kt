package com.spark.fastplayer.presentation.epg

import app.cash.turbine.test
import com.spark.fastplayer.MainCoroutineRule
import com.spark.fastplayer.TestCoroutineContextProvider
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.domain.repository.EPGRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okio.IOException
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program

@ExperimentalCoroutinesApi
class EPGViewModelTest {

    @get:Rule
    val rule = MainCoroutineRule()

    @MockK
    lateinit var epgApi: EpgApi

    private lateinit var coroutineContextProvider: TestCoroutineContextProvider

    @MockK
    lateinit var repository: EPGRepository

    private lateinit var viewModel: EPGViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coroutineContextProvider = TestCoroutineContextProvider()
        viewModel = EPGViewModel(repository, coroutineContextProvider, epgApi)
    }

    @Test
    fun epgViewModel_fetchData_SuccessTest()  {
        runTest {
            val p1 = mockk<Program>()
            val p2 = mockk<Program>()
            val epgData = listOf(EpgRow(listOf(p1, p2)))
            coEvery { repository.getEPGData(epgApi) } returns epgData

            viewModel.getEPGData()

            viewModel.epgState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is EPGState.FetchSuccess)
            }
        }
    }
}
