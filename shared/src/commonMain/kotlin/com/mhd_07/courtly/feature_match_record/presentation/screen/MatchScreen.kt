package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_record.presentation.component.Court
import com.mhd_07.courtly.feature_match_record.presentation.model.MatchState
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.undo
import courtly.shared.generated.resources.undo_left_outline
import courtly.shared.generated.resources.undo_right_outline
import org.jetbrains.compose.resources.painterResource


import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchScreen(
    state: MatchState,
    undoAvailable: Boolean,
    redoAvailable: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onPoint: (Side) -> Unit,
    navBack: () -> Unit
) {
    var quitDialog by remember { mutableStateOf(false) }
    val direction = LocalLayoutDirection.current
    val dimensions = LocalDimensions.current
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
                        painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.undo_left_outline else Res.drawable.undo_right_outline),
                        contentDescription = stringResource(Res.string.undo),
                        action = onUndo,
                        enabled = undoAvailable
                    ),
                    ActionIcon(
                        painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.undo_right_outline else Res.drawable.undo_left_outline),
                        contentDescription = stringResource(Res.string.redo),
                        action = onRedo,
                        enabled = redoAvailable
                    )
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(
                horizontal = LocalDimensions.current.medium,
                vertical = LocalDimensions.current.small
            ), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.medium)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    TeamSection(
                        modifier = Modifier.weight(1f),
                        teamName = state.teamLeft.name,
                        teamSets = (state.prevSets.map { it.first } + state.currentSet.first)
                            .toSets(state.bestOf)
                    )
//                    VerticalDivider(color = Color.Gray, modifier = Modifier.weight(0.1f))
                    Row(
//                        modifier = Modifier.weight(0.75f),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.prevSets.map { it.first }
                                .count { it == state.mode.matchPerSet }.toString(),
                            style = titleTextStyle
                        )
                        Text(text = "-", style = titleTextStyle)
                        Text(
                            text = state.prevSets.map { it.second }
                                .count { it == state.mode.matchPerSet }.toString(),
                            style = titleTextStyle
                        )
                    }
//                    VerticalDivider(color = Color.Gray, modifier = Modifier.weight(0.1f))
                    TeamSection(
                        modifier = Modifier.weight(1f),
                        teamName = state.teamRight.name,
                        teamSets = (state.prevSets.map { it.second } + state.currentSet.second)
                            .toSets(state.bestOf)
                    )
                }
            }

            Court(
                modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2f),
                fill = MaterialTheme.colorScheme.primary,
                stroke = MaterialTheme.colorScheme.onBackground,
                side = state.ballTeam,
                hCourtSide = state.ballHalf,
                win = state.winner != null
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreCard(
                    modifier = Modifier.weight(1f),
                    teamName = state.teamLeft.name,
                    teamScore = state.currentScore.first.display,
                    onClick = { onPoint(Side.TeamLeft) }
                )
                ScoreCard(
                    modifier = Modifier.weight(1f),
                    teamName = state.teamRight.name,
                    teamScore = state.currentScore.second.display,
                    onClick = { onPoint(Side.TeamRight) }
                )
            }
        }
    }
}

@Composable
fun ScoreCard(
    modifier: Modifier = Modifier,
    teamName: String,
    teamScore: String,
    onClick: () -> Unit
) {
    val dimensions = LocalDimensions.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(text = teamName)
            Text(text = teamScore, style = titleTextStyle)
        }
    }
}

@Composable
fun TeamSection(modifier: Modifier = Modifier, teamName: String, teamSets: List<Int>) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        Text(text = teamName)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                dimensions.xSmall,
                Alignment.CenterHorizontally
            ),
//            maxLines = 1
        ) {
            items(teamSets) { set ->
//                var size by remember { mutableStateOf(androidx.compose.ui.unit.IntSize.Zero) }
                Box(
                    modifier = Modifier.border(
                        width = dimensions.xxSmall,
                        color = Color.Gray,
                        shape = MaterialTheme.shapes.extraSmall
                    ).layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val maxDimension = maxOf(placeable.width, placeable.height)

                        layout(maxDimension, maxDimension) {
                            val x = (maxDimension - placeable.width) / 2
                            val y = (maxDimension - placeable.height) / 2
                            placeable.placeRelative(x, y)
                        }
                    }/*.size(with(LocalDensity.current){ maxOf(size.width, size.height).toDp() }).aspectRatio(1f)*/,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = set.toString(),
                        style = notesTextStyle.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(dimensions.xxSmall)
                    )
                }
            }
        }
    }
}


/* {
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

*//*
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
    List(bestOf) {
        this.getOrElse(it) { 0 }
    }