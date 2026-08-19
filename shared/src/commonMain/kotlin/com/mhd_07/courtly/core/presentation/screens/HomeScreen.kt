package com.mhd_07.courtly.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
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
import courtly.shared.generated.resources.empty_feed
import courtly.shared.generated.resources.feed
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.new_game
import courtly.shared.generated.resources.profile
import courtly.shared.generated.resources.my_games
import courtly.shared.generated.resources.user_bold
import kotlinx.coroutines.launch


import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    navToGameSetup: () -> Unit,
    navToProfileScreen: () -> Unit,
    userPFP: String?,
    matches: List<Match>,
    navToMatch: (String) -> Unit,
    loadNext : () -> Unit,
    refresh : () -> Unit,
    result : RemoteResult?
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CourtlyAppBar(
            title = "Courtly",
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
    }) {
        val dimensions = LocalDimensions.current
        val lazyState = rememberLazyListState()
        val shouldLoadNext by remember {
            derivedStateOf {
                val info = lazyState.layoutInfo
                val lastVisibleItem = info.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
                val visibleItems = info.visibleItemsInfo

                lastVisibleItem.index >= visibleItems.lastIndex - 3
            }
        }
        LaunchedEffect(shouldLoadNext){
            if (shouldLoadNext)
                loadNext()
        }
        PullToRefreshBox(isRefreshing = result is RemoteResult.Loading, onRefresh = refresh){
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = dimensions.small)
                    .padding(it),
                state = lazyState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
                items(matches, key = { it.id }) { match ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = {
                            navToMatch(match.id)
                        }) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                            verticalArrangement = Arrangement.spacedBy(dimensions.small)
                        ) {
                            if (match.status == MatchStatus.Live)
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(text = stringResource(Res.string.live)) },
                                    icon = {
                                        Box(
                                            modifier = Modifier.fillMaxHeight()
                                                .background(
                                                    MaterialTheme.colorScheme.onSurface,
                                                    CircleShape
                                                )
                                        )
                                    },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        dimensions.xxSmall
                                    ),
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    val team1ActivePlayers =
                                        match.team1.players.filter { !it.bench }

                                    Box(
                                        modifier = Modifier/*height(dimensions.xxLarge).*/.height(
                                            dimensions.xxLarge
                                        )
                                            .aspectRatio(
                                                if (match.rules.type == MatchType.Single || team1ActivePlayers.getOrNull(
                                                        1
                                                    ) == null
                                                ) 1f else 1.5f
                                            )
                                    ) {
                                        team1ActivePlayers.getOrNull(0)?.let {
                                            PlayerAvatar(
                                                name = it.name,
                                                avatar = it.avatar + "?v=" + it.avatarVersion,
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                contentPadding = dimensions.small,
                                                borderColor = MaterialTheme.colorScheme.surface
                                            )
                                        }
                                        team1ActivePlayers.getOrNull(1)?.let {
                                            PlayerAvatar(
                                                name = it.name,
                                                avatar = it.avatar + "?v=" + it.avatarVersion,
                                                modifier = Modifier.align(Alignment.CenterEnd),
                                                contentPadding = dimensions.small,
                                                borderColor = MaterialTheme.colorScheme.surface
                                            )
                                        }
                                    }
                                    Column {
                                        Text(text = match.team1.name, maxLines = 1)
                                        PlayerNamesText(
                                            p1 = team1ActivePlayers.getOrNull(0)?.name,
                                            p2 = team1ActivePlayers.getOrNull(1)?.name,
                                            style = notesTextStyle
                                        )
                                    }
                                }
                                TeamSetsRow(
                                    modifier = Modifier.padding(start = dimensions.xSmall),
                                    sets = match.sets.map { it.team1Games },
                                    bestOf = match.rules.bestOf,
                                    currentSetIndex = match.currentSetIndex,
                                    currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team1Score
                                        ?: Score.Zero,
                                    isPlaying = match.winner == null,
                                    isWinner = match.winner == Side.Team1
                                )
                            }

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
                                    val team2ActivePlayers =
                                        match.team2.players.filter { !it.bench }
                                    Box(
                                        modifier = Modifier.height(dimensions.xxLarge)
                                            .aspectRatio(
                                                if (match.rules.type == MatchType.Single || team2ActivePlayers.getOrNull(
                                                        1
                                                    ) == null
                                                ) 1f else 1.5f
                                            )
                                    ) {
                                        team2ActivePlayers.getOrNull(0)?.let {
                                            PlayerAvatar(
                                                name = it.name,
                                                avatar = it.avatar + "?v=" + it.avatarVersion,
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                contentPadding = dimensions.small,
                                                borderColor = MaterialTheme.colorScheme.surface
                                            )
                                        }
                                        if (match.rules.type == MatchType.Double)
                                            team2ActivePlayers.getOrNull(1)?.let {
                                                PlayerAvatar(
                                                    name = it.name,
                                                    avatar = it.avatar + "?v=" + it.avatarVersion,
                                                    modifier = Modifier.align(Alignment.CenterEnd),
                                                    contentPadding = dimensions.small,
                                                    borderColor = MaterialTheme.colorScheme.surface
                                                )
                                            }
                                    }
                                    Column {
                                        Text(text = match.team2.name, maxLines = 1)
                                        PlayerNamesText(
                                            p1 = team2ActivePlayers.getOrNull(0)?.name,
                                            p2 = team2ActivePlayers.getOrNull(1)?.name,
                                            style = notesTextStyle
                                        )
                                    }
                                }
                                TeamSetsRow(
                                    modifier = Modifier.padding(start = dimensions.xSmall),
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
                }

            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CourtlyTheme(darkTheme = true) {
        HomeScreen(navToGameSetup = {}, navToProfileScreen = {}, userPFP = null, matches = emptyList(), navToMatch = {}, loadNext = {}, refresh = {}, result = RemoteResult.Success)
    }
}