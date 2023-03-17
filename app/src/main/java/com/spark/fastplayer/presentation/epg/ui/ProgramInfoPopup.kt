import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spark.fastplayer.R
import com.spark.fastplayer.common.toBroadCastTime
import com.spark.fastplayer.presentation.epg.EPGState
import org.openapitools.client.models.Program

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout(program: Program) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded
    )
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            Column {
                //SheetContent(program)
            }
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Transparent,
        sheetBackgroundColor =  Color.Transparent
    ){

    }
}

@Composable
fun SheetContent(program: Program?) {
    if (program != null) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .background(color = Color.DarkGray),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .height(280.dp)
                    .width(260.dp)
                    .padding(start = 12.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    androidx.compose.material3.Text(
                        text = program.channel?.title.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Text(
                        text = program.scheduleStart?.toBroadCastTime() + " - " + program.scheduleEnd?.toBroadCastTime(),
                        fontSize = 18.sp,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(8.dp)
                    ) {
                        androidx.compose.material3.Text(
                            text = "Live",
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                androidx.compose.material3.Text(
                    text = program.channel?.description.orEmpty(),
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .background(Color.Blue)
                        .padding(8.dp)
                ) {
                    androidx.compose.material3.Text(
                        text = program.channel?.taxonomies?.firstOrNull()?.title.orEmpty(),
                        fontSize = 16.sp,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(200.dp)
                    .padding(top = 44.dp, end = 12.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(program.channel?.url.orEmpty())
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(R.drawable.ic_baseline_connected_tv_24),
                    modifier = Modifier.width(108.dp).height(64.dp),
                    contentScale = ContentScale.Inside,
                )

                androidx.compose.material3.Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = program.channel?.title.orEmpty(),
                    fontSize = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}