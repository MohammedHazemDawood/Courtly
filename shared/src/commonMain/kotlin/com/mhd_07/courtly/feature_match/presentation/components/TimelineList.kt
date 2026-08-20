package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match.domain.model.Event
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.started
import courtly.shared.generated.resources.the_end
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun TimelineList(timeline: List<Event>, startTime: Instant?, team1Name: String, team2Name: String) {
    val dimensions = LocalDimensions.current
    FlowRow(modifier = Modifier.fillMaxSize(), maxItemsInEachRow = 1) {
        timeline.reversed().forEachIndexed { index, event ->
            when (event) {
                is Event.Start -> EventStart(event)

                is Event.Team1Point -> EventPoint(
                    startTime = startTime,
                    event = event,
                    team1Name = team1Name,
                )

                is Event.Team2Point -> EventPoint(
                    startTime = startTime,
                    event = event,
                    team2Name = team2Name
                )

                is Event.Team1GameWin -> EventGameWin(
                    event = event,
                    team1Name = team1Name,
                    startTime = startTime
                )

                is Event.Team2GameWin -> EventGameWin(
                    event = event,
                    team2Name = team2Name,
                    startTime = startTime
                )

                is Event.Team1SetWin -> EventSetWin(
                    event = event,
                    team1Name = team1Name,
                    startTime = startTime
                )

                is Event.Team2SetWin -> EventSetWin(
                    event = event,
                    team2Name = team2Name,
                    startTime = startTime
                )


                is Event.Team1Won -> {
                    return@forEachIndexed
                }

                is Event.Team2Won -> {
                    return@forEachIndexed
                }

                is Event.Done -> EventDone(event, startTime)
            }
            if (index != timeline.lastIndex)
                Box(
                    modifier = Modifier.padding(start = dimensions.small).width(dimensions.xSmall)
                        .height(dimensions.small), contentAlignment = Alignment.Center
                ) {
                    if(event !is Event.Done && timeline[index + 1] !is Event.Done)
                    VerticalDivider(color = MaterialTheme.colorScheme.primary)
                }
        }
    }
}