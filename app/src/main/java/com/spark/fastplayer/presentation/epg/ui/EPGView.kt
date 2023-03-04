package com.spark.fastplayer.presentation.epg.ui

import android.os.Build
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spark.fastplayer.data.repository.TaxonomyPrograms
import com.spark.fastplayer.presentation.epg.EPGState
import org.openapitools.client.models.EpgRow

@Composable
fun Feed(
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    epgState: EPGState
) {
    when (epgState) {
      /*  is EPGState.FetchSuccess -> {
            val epgRowCollection = remember { epgState.list }
            val taxonomyCollection = remember { epgState.taxonomies }
             Feed(
                 epgRowCollection,
                 taxonomyCollection,
                 onProgramClick,
                 modifier
             )
        }*/

        is EPGState.FetchSuccessSortedData -> {
            val epgRowCollection = remember { epgState.map }
            val taxonomyCollection = remember { epgState.taxonomies }
            Feed(
                epgRowCollection,
                taxonomyCollection,
                onProgramClick,
                modifier
            )
        }

        else -> {}
    }
}


@Composable
private fun Feed(
    snackCollections: List<Pair<String?, List<EpgRow>>>,
    filters: List<String>,
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    JetChannelSurface(modifier = modifier.fillMaxSize()) {
        Box {
            RenderEPGRowsCollections(snackCollections, filters, onProgramClick)
            // DestinationBar()
        }
    }
}

@Composable
private fun RenderEPGRowsCollections(
    epgRow: List<Pair<String?, List<EpgRow>>>,
    filters: List<String>,
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filtersVisible by rememberSaveable { mutableStateOf(false) }
    Box(modifier) {
        LazyColumn {

            item {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                    )
                )
            }
            val list = epgRow.toList()
            itemsIndexed(list) { index, list ->
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                    )
                )
                Text(
                   text = list.second[0].programs?.firstOrNull()?.taxonomies?.firstOrNull()?.title.orEmpty(),
                    //text = "New Category",
                    modifier = modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                list.second.forEach {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        EpgRowsCollection(it.programs!!, onProgramClick)
                    }
                }
            }
        }
    }
}
