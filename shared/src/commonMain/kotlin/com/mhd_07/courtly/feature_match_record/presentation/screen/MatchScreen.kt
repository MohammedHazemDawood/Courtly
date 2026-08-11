package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_record.presentation.component.Court
import com.mhd_07.courtly.feature_match_record.presentation.component.CurrentGamePoints
import com.mhd_07.courtly.feature_match_record.presentation.component.Sets
import com.mhd_07.courtly.feature_match_record.presentation.component.Tables
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.ensure_quit
import courtly.shared.generated.resources.quit
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.undo
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowBackUp
import dev.seyfarth.tablericons.outlined.ArrowForwardUp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchScreen(
    state: Match,
    undoAvailable: Boolean,
    redoAvailable: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onPoint: (Side) -> Unit,
    navBack: () -> Unit
) {
    var quitDialog by remember { mutableStateOf(false) }
    val direction = LocalLayoutDirection.current
    BackHandler {
        quitDialog = true
//        navBack()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = state.status.display,
                dotVisible = state.status == MatchStatus.Live,
                titleColor = if (state.status == MatchStatus.Live) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                backVisible = true,
                onBackClick = { quitDialog = true },
                actions = arrayOf(
                    ActionIcon(
                        if (direction == LayoutDirection.Ltr) TablerIcons.Outlined.ArrowBackUp else TablerIcons.Outlined.ArrowForwardUp,
                        contentDescription = stringResource(Res.string.undo),
                        action = onUndo,
                        enabled = undoAvailable
                    ),
                    ActionIcon(
                        if (direction == LayoutDirection.Ltr) TablerIcons.Outlined.ArrowForwardUp else TablerIcons.Outlined.ArrowBackUp,
                        contentDescription = stringResource(Res.string.redo),
                        action = onRedo,
                        enabled = redoAvailable
                    )
                )
            )
        }
    ) {
        if (quitDialog)
            AlertDialog(
                modifier = Modifier.fillMaxWidth(0.8f),
                onDismissRequest = { quitDialog = false },
                title = {
                    Text(
                        text = stringResource(Res.string.ensure_quit),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { navBack() },
//                        enabled = state.status == MatchStatus.Finished
                    ) {
                        Text(
                            text = stringResource(Res.string.quit),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { quitDialog = false }) {
                        Text(
                            text = stringResource(Res.string.cancel),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            )
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val dimension = LocalDimensions.current
            Spacer(modifier = Modifier.padding(dimension.xSmall))
            Sets(
                modifier = Modifier.fillMaxWidth(),
                leftName = state.teamLeft.name,
                rightName = state.teamRight.name,
                bestOf = state.bestOf,
                finished = state.status == MatchStatus.Finished,
                winner = state.winner,
                currentSet = state.currentSet,
                prevSets = state.prevSets
            )
            Spacer(modifier = Modifier.padding(dimension.small))
            CurrentGamePoints(
                modifier = Modifier.fillMaxWidth(0.4f),
                teamLeftScore = state.currentScore.first,
                teamRightScore = state.currentScore.second,
                onPoint = onPoint
            )
            Spacer(modifier = Modifier.padding(dimension.small))
            Court(
                modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2f),
                fill = MaterialTheme.colorScheme.primary,
                stroke = MaterialTheme.colorScheme.onBackground,
                side = state.ballTeam,
                hCourtSide = state.ballHalf,
                win = state.winner != null
            )
            Spacer(modifier = Modifier.padding(dimension.medium))
            Tables(
                modifier = Modifier.fillMaxWidth().padding(dimension.small),
                timeline = state.timeline,
//                players = state.teamLeft.players + state.teamRight.players,
                teamLeft = state.teamLeft,
                teamRight = state.teamRight,
                startingTime = state.dateTime
            )
        }
    }
}

/*
@Preview
@Composable
fun MatchScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        MatchScreenContent(
            state = Match.dummy.copy(
                status = MatchStatus.Live,
                teamLeft = Team.initial.copy(name = "Barca"),
                teamRight = Team.initial.copy(name = "Atliti"),
                bestOf = 5
            ),
            undoAvailable = false,
            redoAvailable = false,
            onUndo = {},
            onRedo = {},
            onPoint = {}
        )
    }
}
*/

fun List<Int>.toSets(bestOf: Int): List<Int> =
    this + List((bestOf - size).coerceAtLeast(0)) { 0 }