package com.mhd_07.courtly.feature_match_setup.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_setup.domain.model.SetupStep
import com.mhd_07.courtly.feature_match_setup.presentation.components.LocationPage
import com.mhd_07.courtly.feature_match_setup.presentation.components.ModeTypePage
import com.mhd_07.courtly.feature_match_setup.presentation.components.PlayersPage
import com.mhd_07.courtly.feature_match_setup.presentation.components.SystemPage
import com.mhd_07.courtly.feature_match_setup.presentation.components.TeamsNamesPage
import com.mhd_07.courtly.feature_sign.presentation.components.PagerIndicator
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.next
import courtly.shared.generated.resources.setup
import courtly.shared.generated.resources.start_match
import courtly.shared.generated.resources.team_left_players
import courtly.shared.generated.resources.team_left_players_placeholder
import courtly.shared.generated.resources.team_right_players
import courtly.shared.generated.resources.team_right_players_placeholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

@Composable
fun MatchSetupScreen(
    teamLeftName: String,
    teamRightName: String,
    teamLeftPlayers: List<Player>,
    teamRightPlayers: List<Player>,
    searchText: String,
    onSearch: (String) -> Unit,
    searchResults: List<Player>,
    addTeamLeftPlayer: (Player) -> Unit,
    addTeamRightPlayer: (Player) -> Unit,
    removeTeamLeftPlayer: (Player) -> Unit,
    removeTeamRightPlayer: (Player) -> Unit,
    location: String,
    type: MatchType,
    mode: MatchMode,
    bestOf: Int,
    navToGameRecord: () -> Unit,
    navBack: () -> Unit,
    onChangeTeamLeftName: (String) -> Unit,
    onChangeTeamRightName: (String) -> Unit,
    onEditLocation: (String) -> Unit,
    onModeChange: (MatchMode) -> Unit,
    onBestOfChange: (Int) -> Unit,
    onTypeChange: (MatchType) -> Unit,
    startGame: (Side) -> Unit,
    result: RemoteResult?,
) {
    val pages = listOf(
        SetupStep.Teams,
        SetupStep.TeamLeftPlayers,
        SetupStep.TeamRightPlayers,
        SetupStep.Location,
        SetupStep.ModeAndType,
        SetupStep.System
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val nextEnabled =
        when (pagerState.currentPage) {
            0 -> teamRightName.isNotEmpty() && teamLeftName.isNotEmpty()
            1 -> teamLeftPlayers.isNotEmpty()
            2 -> teamRightPlayers.isNotEmpty()
            3 -> location.isNotEmpty()
            else -> true
        }
    val scope = rememberCoroutineScope()
    BackHandler(scope) {
        println("Match Setup Screen: Back pressed, current page is num ${pagerState.currentPage}")
        if (pagerState.currentPage != 0)
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        else
            navBack()
    }


    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(result) {
        if (result is RemoteResult.Error)
            snackbarHostState.showSnackbar(message = getString(result.error.message))
    }

    val dimensions = LocalDimensions.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(dimensions.medium)
                )
            }
        },
        topBar = {
            CourtlyAppBar(
                title = stringResource(Res.string.setup),
                titleColor = MaterialTheme.colorScheme.onBackground,
                backVisible = true,
                onBackClick = {
                    if (pagerState.currentPage != 0)
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    else
                        navBack()
                },
                trailing = {
                    TextButton(navBack) {
                        Text(stringResource(Res.string.cancel))
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
                .imePadding()
                .padding(vertical = dimensions.medium, horizontal = dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.medium)
        ) {
            PagerIndicator(
                modifier = Modifier.fillMaxWidth(0.75f),
                stepsCount = pagerState.pageCount,
                currentStep = pagerState.currentPage
            )
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
//            ) {
//                Text(
//                    text = stringResource(
//                        Res.string.step,
//                        pagerState.currentPage + 1,
//                        pagerState.pageCount
//                    ), style = notesTextStyle, modifier = Modifier.align(Alignment.Start)
//                )
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page) +
                            pagerState.currentPageOffsetFraction

                val absOffset = abs(pageOffset)

                val progress = 1f - absOffset.coerceIn(0f, 1f)

                Box(
                    Modifier.graphicsLayer {
                        alpha = progress

                        translationX =
                            size.width * (1f - progress)
                    }
                ) {
                    when (pages[page]) {
                        SetupStep.Teams -> TeamsNamesPage(
                            teamRightName = teamRightName,
                            teamLeftName = teamLeftName,
                            onTeamLeftNameChange = { onChangeTeamLeftName(it) },
                            onTeamRightNameChange = { onChangeTeamRightName(it) }
                        )

                        SetupStep.TeamLeftPlayers -> PlayersPage(
                            title = stringResource(Res.string.team_left_players),
                            description = stringResource(
                                Res.string.team_left_players_placeholder,
                                teamLeftName
                            ),
                            players = teamLeftPlayers,
                            searchText = searchText,
                            onSearchPlayer = onSearch,
                            searchResults = searchResults.associateWith {
                                !teamRightPlayers.contains(it) && !teamLeftPlayers.contains(
                                    it
                                )
                            },
                            onAddPlayer = { addTeamLeftPlayer(it) },
                            onRemovePlayer = { removeTeamLeftPlayer(it) },
                            isVisible = page == pagerState.currentPage
                        )

                        SetupStep.TeamRightPlayers -> PlayersPage(
                            title = stringResource(Res.string.team_right_players),
                            description = stringResource(
                                Res.string.team_right_players_placeholder,
                                teamRightName
                            ),
                            players = teamRightPlayers,
                            searchText = searchText,
                            onSearchPlayer = onSearch,
                            searchResults = searchResults.associateWith {
                                !teamLeftPlayers.contains(it) && !teamRightPlayers.contains(
                                    it
                                )
                            },
                            onAddPlayer = { addTeamRightPlayer(it) },
                            onRemovePlayer = { removeTeamRightPlayer(it) },
                            isVisible = page == pagerState.currentPage
                        )

                        SetupStep.Location -> LocationPage(
                            location = location,
                            onChangeLocation = onEditLocation
                        )

                        SetupStep.ModeAndType -> ModeTypePage(
                            mode = mode,
                            type = type,
                            onModeChange = onModeChange,
                            onTypeChange = onTypeChange,
                            doubleEnabled = teamLeftPlayers.size >= 2 && teamRightPlayers.size >= 2
                        )

                        SetupStep.System -> SystemPage(
                            system = bestOf,
                            onSystemChange = onBestOfChange
                        )
                    }
                }
            }
//            }
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (pagerState.currentPage != pagerState.pageCount - 1)
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    else {
                        startGame(Side.Team1)
                        navToGameRecord()
                    }
//                nextEnabled = false
                },
                shape = MaterialTheme.shapes.medium,
                enabled = nextEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(if (pagerState.currentPage != pagerState.pageCount - 1) Res.string.next else Res.string.start_match),
                    style = buttonTextStyle,
                    modifier = Modifier.padding(dimensions.xxSmall)
                )
            }
        }
    }
}