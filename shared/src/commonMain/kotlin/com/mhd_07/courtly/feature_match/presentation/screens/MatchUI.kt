package com.mhd_07.courtly.feature_match.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Player
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.model.RemoteResult
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.popTransform
import com.mhd_07.courtly.core.presentation.ui.theme.predictiveTransform
import com.mhd_07.courtly.core.presentation.ui.theme.pushTransform
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match.domain.model.Match
import com.mhd_07.courtly.feature_match.domain.model.MatchIntent
import com.mhd_07.courtly.feature_match.presentation.components.Court
import com.mhd_07.courtly.feature_match.presentation.components.EnsureBackDialog
import com.mhd_07.courtly.feature_match.presentation.components.PlayerAvatar
import com.mhd_07.courtly.feature_match.presentation.components.PlayerNamesText
import com.mhd_07.courtly.feature_match.presentation.components.PlayerSelectDialog
import com.mhd_07.courtly.feature_match.presentation.components.TeamSetsRow
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchControllerViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchPreviewViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchRecordViewmodel
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.finished
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.undo
import courtly.shared.generated.resources.undo_left_outline
import courtly.shared.generated.resources.undo_right_outline
import courtly.shared.generated.resources.upcoming
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchUI(
    id: String?,
    navBack: () -> Unit,
) {
    val controllerViewmodel = koinViewModel<MatchControllerViewmodel> {
        parametersOf(id)
    }
    val isMine by controllerViewmodel.isMine.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Graphs.Match.Record::class, Graphs.Match.Record.serializer())
                subclass(Graphs.Match.Preview::class, Graphs.Match.Preview.serializer())
            }
        }
    }, Graphs.Match.Preview)

    LaunchedEffect(isMine) {
        if (isMine)
            backStack.add(Graphs.Match.Record)
        else
            backStack.add(Graphs.Match.Preview)
    }

    NavDisplay(
        backStack,
        transitionSpec = { pushTransform },
        popTransitionSpec = { popTransform },
        predictivePopTransitionSpec = { predictiveTransform },
        entryProvider = entryProvider {
            entry<Graphs.Match.Record> {
                val viewmodel = koinViewModel<MatchRecordViewmodel> {
                    parametersOf(id)
                }
                val state by viewmodel.state.collectAsStateWithLifecycle()
                MatchScreen(
                    match = state.match,
                    isUndoAvailable = state.undoEnabled,
                    isRedoAvailable = state.redoEnabled,
                    isMine = true,
                    onUndo = { viewmodel.handleIntent(MatchIntent.Undo) },
                    onRedo = { viewmodel.handleIntent(MatchIntent.Redo) },
                    onPointTeam1 = { viewmodel.handleIntent(MatchIntent.Team1Point(it)) },
                    onPointTeam2 = { viewmodel.handleIntent(MatchIntent.Team2Point(it)) },
                    result = state.result,
                    navBack = navBack,
                    finishMatch = {}
                )
            }
            entry<Graphs.Match.Preview> {
                val viewmodel = koinViewModel<MatchPreviewViewmodel> {
                    parametersOf(id)
                }
                val state by viewmodel.state.collectAsStateWithLifecycle()
                MatchScreen(
                    match = state,
                    isUndoAvailable = false,
                    isRedoAvailable = false,
                    isMine = false,
                    onUndo = {},
                    onRedo = {},
                    onPointTeam1 = {},
                    onPointTeam2 = {},
                    result = null,
                    navBack = navBack,
                    finishMatch = {}
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    match: Match,
    isUndoAvailable: Boolean,
    isRedoAvailable: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onPointTeam1: (Player) -> Unit,
    onPointTeam2: (Player) -> Unit,
    result: RemoteResult?,
    navBack: () -> Unit,
    isMine: Boolean,
    finishMatch : () -> Unit
) {
    println("id: ${match.id}")

    val scaffoldState = rememberBottomSheetScaffoldState()

    val direction = LocalLayoutDirection.current
    val dimensions = LocalDimensions.current

    val tabs = listOf("Stats", "Players", "Timeline")
    var selectedTab by remember { mutableStateOf(0) }

    var quitRequested by remember { mutableStateOf(false) }
    var pointScored by remember { mutableStateOf<Side?>(null) }

    val scope = rememberCoroutineScope()

    BackHandler(scope) {
        quitRequested = !quitRequested
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(result) {
        if (result is RemoteResult.Error) {
            snackbarHostState.showSnackbar(getString(result.error.message))
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = stringResource(
                    when (match.status) {
                        MatchStatus.Coming -> Res.string.upcoming
                        MatchStatus.Live -> Res.string.live
                        MatchStatus.Finished -> Res.string.finished
                    }
                ),
                dotVisible = match.status == MatchStatus.Live,
                backVisible = true,
                titleColor = if (match.status != MatchStatus.Live) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.error,
                onBackClick = { quitRequested = !quitRequested },
                actions = if (isMine) arrayOf(
                    ActionIcon(
                        painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.undo_left_outline else Res.drawable.undo_right_outline),
                        contentDescription = stringResource(Res.string.undo),
                        action = onUndo,
                        enabled = isUndoAvailable
                    ),
                    ActionIcon(
                        painterResource(if (direction == LayoutDirection.Ltr) Res.drawable.undo_right_outline else Res.drawable.undo_left_outline),
                        contentDescription = stringResource(Res.string.redo),
                        action = onRedo,
                        enabled = isRedoAvailable
                    )
                ) else emptyArray()
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        scaffoldState = scaffoldState,
        containerColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.fastForEachIndexed { i, tab ->
                        Tab(
                            selected = selectedTab == i,
                            onClick = { selectedTab = i },
                            text = { Text(tab) })
                    }
                }
            }
        }
    ) { innerPadding ->

        EnsureBackDialog(
            visible = quitRequested,
            onDismiss = { quitRequested = false },
            onConfirm = {
                finishMatch()
                navBack
            }
        )

        match.team1.players.filter { !it.bench }.let {
            PlayerSelectDialog(
                visible = pointScored == Side.Team1,
                p1 = it.getOrNull(0),
                p2 = it.getOrNull(1),
                select = { player ->
                    onPointTeam1(player)
                    pointScored = null
                },
                cancel = {
                    pointScored = null
                }
            )
        }

        match.team2.players.filter { !it.bench }.let {
            PlayerSelectDialog(
                visible = pointScored == Side.Team2,
                p1 = it.getOrNull(0),
                p2 = it.getOrNull(1),
                select = { player ->
                    onPointTeam2(player)
                    pointScored = null
                },
                cancel = {
                    pointScored = null
                }
            )
        }



        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween//spacedBy(dimensions.medium)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.small)
            ) {
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
                        val team1ActivePlayers = match.team1.players.filter { !it.bench }

                        Box(
                            modifier = Modifier/*height(dimensions.xxLarge).*/.height(dimensions.xxLarge)
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
                                    contentPadding = dimensions.small
                                )
                            }
                            team1ActivePlayers.getOrNull(1)?.let {
                                PlayerAvatar(
                                    name = it.name,
                                    avatar = it.avatar + "?v=" + it.avatarVersion,
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    contentPadding = dimensions.small
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
                        sets = match.sets.map { it.team1Games },
                        bestOf = match.rules.bestOf,
                        currentSetIndex = match.currentSetIndex,
                        currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team1Score
                            ?: Score.Zero,
                        isPlaying = match.winner == null
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
                        val team2ActivePlayers = match.team2.players.filter { !it.bench }
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
                                    contentPadding = dimensions.small
                                )
                            }
                            if (match.rules.type == MatchType.Double)
                                team2ActivePlayers.getOrNull(1)?.let {
                                    PlayerAvatar(
                                        name = it.name,
                                        avatar = it.avatar + "?v=" + it.avatarVersion,
                                        modifier = Modifier.align(Alignment.CenterEnd),
                                        contentPadding = dimensions.small
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
                        sets = match.sets.map { it.team2Games },
                        bestOf = match.rules.bestOf,
                        currentSetIndex = match.currentSetIndex,
                        currentGameScore = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team2Score
                            ?: Score.Zero,
                        isPlaying = match.winner == null//TODO CHANGE TO is LIVE
                    )
                }
            }

            Court(
                modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2f),
                fill = MaterialTheme.colorScheme.primary,
                stroke = MaterialTheme.colorScheme.onBackground,
                hCourtSide = match.currentCourtSide,
                side = match.currentServeSide,
                win = match.winner != null
            )

            if (isMine)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = {
                            if (match.status == MatchStatus.Live)
                                pointScored = Side.Team1
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(dimensions.small)
                        ) {
                            Text(text = match.team1.name, maxLines = 1)
                            Text(
                                text = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team1Score?.display
                                    ?: "0", maxLines = 1
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = {
                            if (match.status == MatchStatus.Live)
                                pointScored = Side.Team2
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(dimensions.small),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(dimensions.small)
                        ) {
                            Text(text = match.team2.name, maxLines = 1)
                            Text(
                                text = match.sets.getOrNull(match.currentSetIndex)?.currentGame?.team2Score?.display
                                    ?: "0", maxLines = 1
                            )
                        }
                    }
                }
        }
    }
}