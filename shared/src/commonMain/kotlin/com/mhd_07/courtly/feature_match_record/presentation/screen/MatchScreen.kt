package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match_record.presentation.component.CurrentGamePoints
import com.mhd_07.courtly.feature_match_record.presentation.component.Sets
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchRecordViewModel
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.undo
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowBackUp
import dev.seyfarth.tablericons.outlined.ArrowForwardUp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchScreen(
    viewModel: MatchRecordViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val undoAvailable = viewModel.isUndoAvailable.collectAsStateWithLifecycle().value
    val redoAvailable = viewModel.isRedoAvailable.collectAsStateWithLifecycle().value

    MatchScreenContent(
        state = state.value,
        undoAvailable = undoAvailable,
        redoAvailable = redoAvailable,
        onUndo = { viewModel.handleIntent(MatchIntent.Undo) },
        onRedo = { viewModel.handleIntent(MatchIntent.Redo) },
        onPoint = { viewModel.handleIntent(MatchIntent.Point(it)) }
    )
}

@Composable
fun MatchScreenContent(
    state: Match,
    undoAvailable: Boolean,
    redoAvailable: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onPoint: (Side) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = state.status.display,
                dotVisible = state.status == MatchStatus.Live,
                titleColor = if (state.status == MatchStatus.Live) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                backVisible = true,
                onBackClick = { },
                actions = arrayOf(
                    ActionIcon(
                        TablerIcons.Outlined.ArrowBackUp,
                        contentDescription = stringResource(Res.string.undo),
                        action = onUndo,
                        enabled = undoAvailable
                    ),
                    ActionIcon(
                        TablerIcons.Outlined.ArrowForwardUp,
                        contentDescription = stringResource(Res.string.redo),
                        action = onRedo,
                        enabled = redoAvailable
                    )
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val dimension = LocalDimensions.current
            Spacer(modifier = Modifier.padding(dimension.small))
            Sets(
                modifier = Modifier.fillMaxWidth(),
                teamLeft = state.teamLeft,
                teamRight = state.teamRight,
                bestOf = state.bestOf,
                finished = state.status == MatchStatus.Finished
            )
            Spacer(modifier = Modifier.padding(dimension.medium))
            CurrentGamePoints(
                modifier = Modifier.fillMaxWidth(0.4f),
                teamLeftScore = state.teamLeft.currentScore,
                teamRightScore = state.teamRight.currentScore,
                onPoint = onPoint
            )
        }
    }
}

@Preview
@Composable
fun MatchScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        MatchScreenContent(
            state = Match.initial.copy(
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

fun List<Boolean>.toSets(bestOf: Int): List<Boolean> =
    this + List((bestOf - size).coerceAtLeast(0)) { false }