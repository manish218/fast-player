package com.spark.fastplayer.presentation.epg.ui

import org.openapitools.client.models.Channel
import org.openapitools.client.models.Program

sealed class BottomSheetDataState {

    class Init(val program: Program = Program()): BottomSheetDataState()

    class ShowProgramInfo(val program: Program): BottomSheetDataState()

    class ShowChannelInfo(val channel: Channel, val onWatchNowClick: (String, String, String) -> Unit, val programId: String): BottomSheetDataState()

}