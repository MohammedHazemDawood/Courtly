package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_record.domain.model.SetupStep
import com.mhd_07.courtly.feature_sign.presentation.components.PagerIndicator
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.next
import courtly.shared.generated.resources.setup
import courtly.shared.generated.resources.start_match
import courtly.shared.generated.resources.step
import courtly.shared.generated.resources.team_left_players
import courtly.shared.generated.resources.team_left_players_placeholder
import courtly.shared.generated.resources.team_right_players
import courtly.shared.generated.resources.team_right_players_placeholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun MatchSetupScreen(
    teamLeftName: String,
    teamRightName: String,
    teamLeftPlayers: List<Player>,
    teamRightPlayers: List<Player>,
    searchText: String,
    onSearch: (String) -> Unit,
    searchResults: List<Player>,
    addPlayer: (Player, Side) -> Unit,
    removePlayer: (Player, Side) -> Unit,
    location: String,
    type: MatchType,
    mode: MatchMode,
    bestOf: Int,
    navToGameRecord: () -> Unit,
    navBack: () -> Unit,
    onChangeName: (Side, String) -> Unit,
    onEditLocation: (String) -> Unit,
    onModeChange: (MatchMode) -> Unit,
    onBestOfChange: (Int) -> Unit,
    onTypeChange: (MatchType) -> Unit,
    startGame: (Side) -> Unit
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
        val dimensions = LocalDimensions.current
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
                .imePadding()
                .padding(vertical = dimensions.medium, horizontal = dimensions.medium),
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

                val absOffset = kotlin.math.abs(pageOffset)

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
                            onTeamRightNameChange = { onChangeName(Side.TeamRight, it) },
                            onTeamLeftNameChange = { onChangeName(Side.TeamLeft, it) }
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
                            onAddPlayer = { addPlayer(it, Side.TeamLeft) },
                            onRemovePlayer = { removePlayer(it, Side.TeamLeft) },
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
                            onAddPlayer = { addPlayer(it, Side.TeamRight) },
                            onRemovePlayer = { removePlayer(it, Side.TeamRight) },
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
                            onTypeChange = onTypeChange
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
                        startGame(Side.TeamLeft)
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