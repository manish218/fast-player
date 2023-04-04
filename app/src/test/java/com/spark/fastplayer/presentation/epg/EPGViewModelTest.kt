package com.spark.fastplayer.presentation.epg

import app.cash.turbine.test
import com.spark.fastplayer.MainCoroutineRule
import com.spark.fastplayer.TestCoroutineContextProvider
import com.spark.fastplayer.common.isExpired
import com.spark.fastplayer.data.pefs.DataStoreManager
import com.spark.fastplayer.data.pefs.DataStoreManager
import com.spark.fastplayer.domain.repoisitory.EPGRepository
import com.spark.fastplayer.presentation.player.PlaybackState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.openapitools.client.models.*
import java.time.OffsetDateTime

@ExperimentalCoroutinesApi
class EPGViewModelTest {

    @get:Rule
    val rule = MainCoroutineRule(StandardTestDispatcher())

    private lateinit var coroutineContextProvider: TestCoroutineContextProvider

    @MockK
    lateinit var repository: EPGRepository

    @MockK
    lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coroutineContextProvider = TestCoroutineContextProvider()
        coEvery { dataStoreManager.saveTaxonomyId(any()) } returns Unit
        coEvery { dataStoreManager.saveChannelId(any()) }returns Unit
    }

    @Test
    fun epgViewModel_epgFetchData_play_first_epg_program_Test()  {
        runTest {
            val channel = Channel(
                channelid = "chId",
                taxonomies = listOf(Taxonomy(taxonomyId = "testId1"))
            )
            val p1 = Program(channel = channel)
            val p2 = Program(channel = channel.copy(taxonomies = emptyList()))
            val channelPlaybackInfo = ChannelPlaybackInfo(channel = channel)

            val epgData = listOf(EpgRow(listOf(p1, p2)))
            coEvery { dataStoreManager.getChannelId } returns flow { emit("") }
            coEvery { dataStoreManager.getTaxonomyId } returns  flow { emit("") }
            coEvery { repository.getEPGData() } returns epgData
            coEvery { repository.getChannelStreamInfo("chId") } returns channelPlaybackInfo
            val viewModel = EPGViewModel(repository, coroutineContextProvider, dataStoreManager)

            advanceUntilIdle()
            viewModel.epgState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is EPGState.FetchSuccess)
            }
            viewModel.playbackState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is PlaybackState.PlaybackSuccess)
            }
        }
    }

    @Test
    fun epgViewModel_epgFetchData_resume_last_watched_channel_Test()  {
        runTest {
            val channel = Channel(
                title = "MTV",
                channelid = "chId",
                description = "This is a music channel",
                images = emptyList())

            val p1 = Program(channel = channel, taxonomies = listOf(Taxonomy(taxonomyId = "testId1")))
            val p2 = Program(channel = channel.copy(taxonomies = emptyList()))
            val channelStreamInfo = ChannelStreamInfo(streamUrl = "streamURL")
            val epgData = listOf(EpgRow(listOf(p1, p2)))
            coEvery { repository.getEPGData() } returns epgData
            coEvery { repository.getChannelStreamInfo("chId") } returns ChannelPlaybackInfo(channel= channel, streamInfo = channelStreamInfo)
            coEvery { dataStoreManager.getChannelId } returns flow { emit("chId") }
            coEvery { dataStoreManager.getTaxonomyId } returns  flow { emit("testId1") }
            val viewModel = EPGViewModel(repository, coroutineContextProvider, dataStoreManager)

            viewModel.playbackState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is PlaybackState.PlaybackSuccess)
                Assert.assertEquals(channelStreamInfo.streamUrl, (state as PlaybackState.PlaybackSuccess).metData.streamUrl)
                Assert.assertEquals(channel.description, state.metData.description)
                Assert.assertEquals(channel.title, state.metData.title)
            }
        }
    }

    @Test
    fun epgViewModel_epgFetchData_playback_play_last_watched_channel_does_not_exist_Test()  {
        runTest {
            val channel = Channel(
                title = "MTV",
                channelid = "chId",
                description = "This is a music channel",
                images = emptyList()
            )

            val p1 = Program(channel = channel, taxonomies = listOf(Taxonomy(taxonomyId = "testId1")))
            val p2 = Program(channel = channel.copy(taxonomies = listOf(Taxonomy(taxonomyId = "testId2"))))
            val channelStreamInfo = ChannelStreamInfo(streamUrl = "streamURL")
            val epgData = listOf(EpgRow(listOf(p1, p2)))
            coEvery { repository.getEPGData() } returns epgData
            coEvery { repository.getChannelStreamInfo("chId") } returns ChannelPlaybackInfo(channel= channel, streamInfo = channelStreamInfo)
            coEvery { dataStoreManager.getChannelId } returns flow { emit("invalidChannelId") }
            coEvery { dataStoreManager.getTaxonomyId } returns  flow { emit("testId1") }
            val viewModel = EPGViewModel(repository, coroutineContextProvider, dataStoreManager)

            viewModel.playbackState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is PlaybackState.PlaybackSuccess)
                Assert.assertEquals(channelStreamInfo.streamUrl, (state as PlaybackState.PlaybackSuccess).metData.streamUrl)
                Assert.assertEquals(channel.description, state.metData.description)
                Assert.assertEquals(channel.title, state.metData.title)
            }
        }
    }

    @Test
    fun epgViewModel_epgFetchData_remove_expired_programs_Test()  {
        runTest {
            val channel = Channel(
                channelid = "chId",
                taxonomies = listOf(Taxonomy(taxonomyId = "testId1"))
            )
            val p1 = Program(channel = channel, scheduleEnd = OffsetDateTime.parse("2019-08-31T15:20:30+08:00"))
            val p2 = Program(channel = channel, scheduleEnd = OffsetDateTime.parse("2027-08-31T15:20:30+08:00"))
            val channelPlaybackInfo = ChannelPlaybackInfo(channel = channel)

            val epgData = listOf(EpgRow(listOf(p1, p2)))
            coEvery { dataStoreManager.getChannelId } returns flow { emit("") }
            coEvery { dataStoreManager.getTaxonomyId } returns  flow { emit("") }
            coEvery { repository.getEPGData() } returns epgData
            coEvery { repository.getChannelStreamInfo("chId") } returns channelPlaybackInfo
            val viewModel = EPGViewModel(repository, coroutineContextProvider, dataStoreManager)

            viewModel.sanitizeEPGData()

            viewModel.epgState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is EPGState.FetchSuccess)
                Assert.assertEquals(1, (state as EPGState.FetchSuccess).map.first().second.first().programs!!.size)
            }
        }
    }

}
