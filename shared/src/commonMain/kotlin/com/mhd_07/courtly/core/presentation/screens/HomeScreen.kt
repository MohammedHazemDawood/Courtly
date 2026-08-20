package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.CourtlyTheme
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.presentation.components.PlayerAvatar
import com.mhd_07.courtly.feature_match.presentation.components.PlayerNamesText
import com.mhd_07.courtly.feature_match.presentation.components.TeamSetsRow
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.add_square_outline
import courtly.shared.generated.resources.app_name
import courtly.shared.generated.resources.empty_feed
import courtly.shared.generated.resources.finished
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.new_game
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.upcoming
import courtly.shared.generated.resources.user_bold
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    navToGameSetup: () -> Unit,
    navToProfileScreen: () -> Unit,
    userPFP: String?,
    matches: List<Match>,
    navToMatch: (String) -> Unit,
    loadNext: () -> Unit,
    refresh: () -> Unit,
    result: RemoteResult?
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = stringResource(Res.string.app_name),
                actions = arrayOf(
                    ActionIcon(
                        icon = painterResource(Res.drawable.add_square_outline),
                        contentDescription = stringResource(Res.string.new_game),
                        action = navToGameSetup
                    )
                ),
                startingIcon = userPFP,
                placeHolder = painterResource(Res.drawable.user_bold),
                startingDescription = stringResource(Res.string.profile),
                onStartingIconClick = navToProfileScreen
            )
        }
    ) { innerPadding ->
        val dimensions = LocalDimensions.current
        val lazyState = rememberLazyListState()

        val shouldLoadNext by remember {
            derivedStateOf {
                val layoutInfo = lazyState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

                totalItems > 0 && lastVisibleIndex >= totalItems - 3
            }
        }

        LaunchedEffect(shouldLoadNext) {
            if (shouldLoadNext) {
                loadNext()
            }
        }

        PullToRefreshBox(
            isRefreshing = result is RemoteResult.Loading,
            onRefresh = refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (matches.isEmpty() && result !is RemoteResult.Loading) {
                EmptyMatchesView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensions.small),
                    state = lazyState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.small)
                ) {
                    items(matches, key = { it.id }) { match ->
                        MatchCard(
                            match = match,
                            onClick = { navToMatch(match.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchCard(
    match: Match,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.small),
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
//            if (match.status == MatchStatus.Live) {
            Box(
                modifier = Modifier.align(Alignment.End).background(
                    if (match.status == MatchStatus.Live) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.shapes.medium
                )
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min)
                        .padding(horizontal = dimensions.xSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
                ) {
                    if (match.status == MatchStatus.Live)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .aspectRatio(1f)
                                .background(
                                    MaterialTheme.colorScheme.onSurface,
                                    CircleShape
                                )
                        )
                    Text(
                        text = stringResource(
                            when (match.status) {
                                MatchStatus.Live -> Res.string.live
                                MatchStatus.Coming -> Res.string.upcoming
                                MatchStatus.Finished -> Res.string.finished
                            }
                        )
                    )
                }
//                }
            }

            // Team 1
            MatchTeamRow(
                teamName = match.team1.name,
                players = match.team1.players,
                matchType = match.rules.type,
                sets = match.sets.map { it.team1Games },
                bestOf = match.rules.bestOf,
                currentSetIndex = match.currentSetIndex,
                currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team1Score
                    ?: Score.Zero,
                isPlaying = match.winner == null,
                isWinner = match.winner == Side.Team1
            )

            // Team 2
            MatchTeamRow(
                teamName = match.team2.name,
                players = match.team2.players,
                matchType = match.rules.type,
                sets = match.sets.map { it.team2Games },
                bestOf = match.rules.bestOf,
                currentSetIndex = match.currentSetIndex,
                currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team2Score
                    ?: Score.Zero,
                isPlaying = match.winner == null,
                isWinner = match.winner == Side.Team2
            )
        }
    }
}

@Composable
private fun MatchTeamRow(
    teamName: String,
    players: List<Player>,
    matchType: MatchType,
    sets: List<Int>,
    bestOf: Int,
    currentSetIndex: Int,
    currentGameScore: Score,
    isPlaying: Boolean,
    isWinner: Boolean
) {
    val dimensions = LocalDimensions.current
    val activePlayers = remember(players) { players.filter { !it.bench } }
    val p1 = activePlayers.getOrNull(0)
    val p2 = activePlayers.getOrNull(1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xxSmall),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .height(dimensions.xxLarge)
                    .aspectRatio(if (matchType == MatchType.Single || p2 == null) 1f else 1.5f)
            ) {
                p1?.let {
                    PlayerAvatar(
                        name = it.name,
                        avatar = "${it.avatar}?v=${it.avatarVersion}",
                        modifier = Modifier.align(Alignment.CenterStart),
                        contentPadding = dimensions.small,
                        borderColor = MaterialTheme.colorScheme.surface
                    )
                }
                if (matchType == MatchType.Double) {
                    p2?.let {
                        PlayerAvatar(
                            name = it.name,
                            avatar = "${it.avatar}?v=${it.avatarVersion}",
                            modifier = Modifier.align(Alignment.CenterEnd),
                            contentPadding = dimensions.small,
                            borderColor = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }
            Column {
                Text(text = teamName, maxLines = 1)
                PlayerNamesText(
                    p1 = p1?.name,
                    p2 = p2?.name,
                    style = notesTextStyle
                )
            }
        }
        TeamSetsRow(
            modifier = Modifier.padding(start = dimensions.xSmall),
            sets = sets,
            bestOf = bestOf,
            currentSetIndex = currentSetIndex,
            currentGameScore = currentGameScore,
            isPlaying = isPlaying,
            isWinner = isWinner
        )
    }
}

@Composable
private fun EmptyMatchesView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.empty_feed),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        HomeScreen(
            navToGameSetup = {},
            navToProfileScreen = {},
            userPFP = null,
            matches = emptyList(),
            navToMatch = {},
            loadNext = {},
            refresh = {},
            result = RemoteResult.Success
        )
    }
}