package com.spark.fastplayer.presentation.epg.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spark.fastplayer.common.random
import org.openapitools.client.models.Taxonomy

@Composable
fun EpgTaxonomyCollection(
    taxonomyList: List<Taxonomy?>,
    onTaxonomySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues( end = 12.dp)
    ) {
        itemsIndexed(taxonomyList) { _, taxonomy ->
            TaxonomyLabel(
                txName = taxonomy?.title.orEmpty(),
                modifier = modifier
                    .height(48.dp)
                    .padding(start = 0.dp)
                    .wrapContentWidth()
                    .clickable {
                        onTaxonomySelected.invoke(taxonomy?.taxonomyId.orEmpty())
                    },
            )
        }
    }
}

@Composable
fun TaxonomyLabel(
    txName: String,
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
) {

    EPGCardItemSurface(
        color = Color.Companion.random(),
        elevation = elevation,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = txName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
