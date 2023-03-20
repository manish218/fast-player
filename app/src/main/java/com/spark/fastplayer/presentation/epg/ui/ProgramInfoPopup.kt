import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spark.fastplayer.common.getStreamType
import com.spark.fastplayer.common.toBroadCastTime
import com.spark.fastplayer.presentation.epg.StreamType
import com.spark.fastplayer.presentation.epg.ui.BottomSheetDataState
import org.openapitools.client.models.Program

@Composable
fun BottomSheetLayout(bottomSheetDataState: BottomSheetDataState) {
    when (bottomSheetDataState) {
        is BottomSheetDataState.Load -> SheetContent(program = bottomSheetDataState.program)
        is BottomSheetDataState.Init -> SheetContent(program = bottomSheetDataState.program)
    }
}

@Composable
fun SheetContent(program: Program) {
    val streamTye = program.scheduleStart.getStreamType(program.scheduleEnd)
    Column(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(program.channel?.images?.firstOrNull()?.url.orEmpty())
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .width(108.dp)
                    .height(64.dp),
                contentScale = ContentScale.Fit,
            )

            Box(
                modifier = Modifier
                    .border(width = 0.dp, color = Color.White, shape = RoundedCornerShape(5.dp))
                    .background(if (streamTye is StreamType.Live) Color.Red else Color.Blue)
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                androidx.compose.material3.Text(
                    text = if (streamTye is StreamType.Live) "Live" else "Upcoming",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                )

            }
        }

        Text(
            text = program.title.orEmpty(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = program.description.orEmpty(),
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = program.scheduleStart?.toBroadCastTime() + " - " + program.scheduleEnd?.toBroadCastTime(),
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .border(width = 0.dp, color = Color.White, shape = RoundedCornerShape(4.dp))
                .background(Color.Black)
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
}

