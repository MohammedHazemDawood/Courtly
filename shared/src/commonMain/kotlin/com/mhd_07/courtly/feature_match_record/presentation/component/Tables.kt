package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.domain.model.TimelineAction
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.players
import courtly.shared.generated.resources.timeline
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun Tables(
    modifier: Modifier,
    timeline: List<TimelineAction>,
    teamLeft: Team,
    teamRight: Team,
    startingTime: Instant
) {
    val dimension = LocalDimensions.current
    val tabs = listOf(
        stringResource(Res.string.timeline),
        stringResource(Res.string.players),
//        "Comments"
    )
    val tabContents = listOf<@Composable () -> Unit>(
        {
            TimeLine(
                timeline,
                teamLeft = teamLeft,
                startingTime = startingTime,
                teamRight = teamRight
            )
        },
        {
            Players(
                teamLeft = teamLeft,
                teamRight = teamRight,
            )
        },
//        { Comments() }
    )
    var selectedTab by remember { mutableStateOf(0) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimension.xSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth().clip(
                MaterialTheme.shapes.small.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            ),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    title = title,
                    onClick = { selectedTab = index })
            }
        }
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = MaterialTheme.shapes.small.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        ) {
            tabContents[selectedTab].invoke()
        }
    }
}

@Composable
fun Tab(title: String, onClick: () -> Unit) {
    val dimension = LocalDimensions.current
    Box(
        modifier = Modifier
//            .padding(vertical = dimension.xSmall)
            .wrapContentSize()
            .clickable(onClick = onClick)
    ) {
        Text(text = title, modifier = Modifier.padding(dimension.xSmall))
    }
}


@Composable
fun TimeLine(
    timeline: List<TimelineAction>,
    startingTime: Instant,
    teamLeft: Team,
    teamRight: Team
) {
//    val dimension = LocalDimensions.current
    val lazyListState = rememberLazyListState()
    LaunchedEffect(timeline) {
        lazyListState.animateScrollToItem(0)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = lazyListState,
    ) {
        items(timeline.reversed()) { action ->
            when (action) {
                is TimelineAction.Point -> Point(
                    action = action,
                    teamLeft = teamLeft,
                    teamRight = teamRight,
                    startingTime = startingTime
                )

                is TimelineAction.Sub -> Substitution(
                    action = action,
                    teamLeft = teamLeft,
                    teamRight = teamRight,
                    startingTime = startingTime
                )

                is TimelineAction.Transfer -> TODO()
                is TimelineAction.WinGame -> WinGame(
                    action = action,
                    teamLeft = teamLeft,
                    teamRight = teamRight,
                    startingTime = startingTime
                )

                is TimelineAction.WinMatch -> WinMatch(
                    action = action,
                    teamLeft = teamLeft,
                    teamRight = teamRight,
                    startingTime = startingTime
                )

                is TimelineAction.WinSet -> {//TODO()
                }
            }
        }
    }
}


@Preview
@Composable
fun TablesPreview() {
    CourtlyTheme(darkTheme = true) {
        Tables(
            modifier = Modifier.fillMaxSize(),
            timeline = emptyList(),
           /* players = listOf(
                Player(name = "Alice", avatar = null, handle = "alice", bench = false),
                Player(name = "Bob", avatar = null, handle = "bo vsb", bench = false),
                Player(name = "Eve", avatar = null, handle = "eve", bench = true)
            ),*/
            teamLeft = Team.initial.copy(
                players = listOf(
                    Player(name = "Alice", avatar = null, handle = "alice", bench = false),
                    Player(name = "Bob", avatar = null, handle = "sbob", bench = false),
                )
            ),
            teamRight = Team.initial.copy(
                players = listOf(
                    Player(name = "Charlie", avatar = null, handle = "charlie", bench = false),
                    Player(name = "Dana", avatar = null, handle = "dana", bench = true)
                )
            ),
            startingTime = Clock.System.now()
        )
    }
}
