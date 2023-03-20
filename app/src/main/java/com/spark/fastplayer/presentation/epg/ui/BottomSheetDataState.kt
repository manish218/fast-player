package com.spark.fastplayer.presentation.epg.ui

import org.openapitools.client.models.Program

sealed class BottomSheetDataState {

    class Init(val program: Program = Program()): BottomSheetDataState()

    class Load(val program: Program): BottomSheetDataState()

}