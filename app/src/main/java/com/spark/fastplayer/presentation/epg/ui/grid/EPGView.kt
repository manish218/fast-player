package com.spark.fastplayer.presentation.epg.ui.grid

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spark.fastplayer.presentation.epg.EPGState
import com.spark.fastplayer.presentation.epg.ui.item.ChannelLogoView
import com.spark.fastplayer.presentation.epg.ui.EPGCardItemSurface
import com.spark.fastplayer.presentation.epg.ui.item.ProgramCardView
import kotlinx.coroutines.launch
import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program
import org.openapitools.client.models.Taxonomy

@Composable
fun FeedEPGData(
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    epgState: EPGState
) {
    when (epgState) {
        is EPGState.FetchSuccess -> {
            val epgRowCollection = remember { epgState.map }
            val taxonomyCollection = remember { epgState.taxonomies }
            RenderEPGGrid(
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
private fun RenderEPGGrid(
    epgRowCollection: List<Pair<Taxonomy?, List<EpgRow>>>,
    filters: List<Taxonomy?>,
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    EPGCardItemSurface(modifier = modifier.fillMaxSize()) {
        RenderEPGRowsCollections(epgRowCollection, filters, onProgramClick)
    }
}

@Composable
private fun RenderEPGRowsCollections(
    epgRow: List<Pair<Taxonomy?, List<EpgRow>>>,
    taxonomies: List<Taxonomy?>,
    onProgramClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(modifier) {
        LazyColumn(state = listState) {
            item {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars.add(WindowInsets(top = 10.dp))
                    )
                )
                EpgTaxonomyCollection(taxonomyList = taxonomies, onTaxonomySelected = {
                    scope.launch {
                        listState.scrollToItem(getSelectedTaxonomyIndex(it, epgRow))
                    }
                })
            }
        }

        LazyColumn(state = listState) {
            itemsIndexed(epgRow) { _, list ->
                if (list.first!=null) {
                    Text(
                        text = list.second.firstOrNull()?.programs?.firstOrNull()?.taxonomies?.firstOrNull()?.title.orEmpty(),
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
                }
                list.second.forEach {
                    EpgProgramsCollection(it.programs.orEmpty(), onProgramClick)
                }
            }
        }
    }
}

@Composable
fun EpgProgramsCollection(
    programList: List<Program>,
    onProgramClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        if (programList.isNotEmpty()) {
            ChannelLogoView(
                imageUrl = programList.firstOrNull()?.channel?.images?.firstOrNull()?.url.orEmpty(),
                contentDescription = "",
                modifier = Modifier
                    .width(116.dp)
                    .height(72.dp)
                    .padding(start = 12.dp, end = 6.dp)
            )
        }
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(end = 12.dp)
        ) {
            itemsIndexed(programList) { index, program ->
                ProgramCardView(program, onProgramClicked)
            }
        }
    }
}

fun getSelectedTaxonomyIndex(taxonomyId: String, epgRow: List<Pair<Taxonomy?, List<EpgRow>>>): Int {
    return epgRow.indexOfFirst {
        it.second.firstOrNull()?.programs?.firstOrNull()?.taxonomies?.firstOrNull()?.taxonomyId == taxonomyId
    }
}
