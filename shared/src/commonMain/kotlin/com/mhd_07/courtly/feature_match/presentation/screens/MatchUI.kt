package com.mhd_07.courtly.feature_match.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.zIndex
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
import com.mhd_07.courtly.feature_match.domain.model.MatchState
import com.mhd_07.courtly.feature_match.presentation.components.Court
import com.mhd_07.courtly.feature_match.presentation.components.EnsureDialog
import com.mhd_07.courtly.feature_match.presentation.components.PlayerAvatar
import com.mhd_07.courtly.feature_match.presentation.components.PlayerNamesText
import com.mhd_07.courtly.feature_match.presentation.components.PlayerSelectDialog
import com.mhd_07.courtly.feature_match.presentation.components.Players
import com.mhd_07.courtly.feature_match.presentation.components.Stats
import com.mhd_07.courtly.feature_match.presentation.components.TeamSetsRow
import com.mhd_07.courtly.feature_match.presentation.components.TimelineList
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchControllerViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchPreviewViewmodel
import com.mhd_07.courtly.feature_match.presentation.viewmodel.MatchRecordViewmodel
import com.mhd_07.courtly.feature_nav.presentation.data.Graphs
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.cancel
import courtly.shared.generated.resources.ensure_quit
import courtly.shared.generated.resources.finished
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.players
import courtly.shared.generated.resources.quit
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.stats
import courtly.shared.generated.resources.timeline
import courtly.shared.generated.resources.undo
import courtly.shared.generated.resources.undo_left_outline
import courtly.shared.generated.resources.undo_right_outline
import courtly.shared.generated.resources.upcoming
import io.github.vinceglb.confettikit.compose.ConfettiKit
import io.github.vinceglb.confettikit.core.Angle
import io.github.vinceglb.confettikit.core.Party
import io.github.vinceglb.confettikit.core.Position
import io.github.vinceglb.confettikit.core.Spread
import io.github.vinceglb.confettikit.core.emitter.Emitter
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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
                    finishMatch = { viewmodel.handleIntent(MatchIntent.FinishMatch) }
                )
            }
            entry<Graphs.Match.Preview> {
                val viewmodel = koinViewModel<MatchPreviewViewmodel> {
                    parametersOf(id)
                }
                val state by viewmodel.state.collectAsStateWithLifecycle()
                LaunchedEffect(state.status, state.doneAt, isMine, state) {
                    if (state == Match.initial) return@LaunchedEffect
                    val doneAt = state.doneAt


                    val isExpired =
                        doneAt?.let { da -> Clock.System.now() - da > 5.minutes } ?: false
                    println("is expired: $isExpired | doneAt: $doneAt")

                    if (isMine && !isExpired) {
                        backStack.clear()
                        backStack.add(Graphs.Match.Record)
                    } else {
                        backStack.clear()
                        backStack.add(Graphs.Match.Preview)
                    }
                }
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
    finishMatch: () -> Unit
) {
    println("id: ${match.id}")

    val direction = LocalLayoutDirection.current
    val dimensions = LocalDimensions.current

    val tabs = listOf(
        stringResource(Res.string.timeline),
        stringResource(Res.string.stats),
        stringResource(Res.string.players)
    )//TODO

    var quitRequested by remember { mutableStateOf(false) }
    var pointScored by remember { mutableStateOf<Side?>(null) }

    val scope = rememberCoroutineScope()

    BackHandler(scope) {
        if (!isMine || (isMine && match.status == MatchStatus.Finished))
            navBack()
        else if (match.status != MatchStatus.Finished && isMine)
            quitRequested = !quitRequested
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(result) {
        if (result is RemoteResult.Error) {
            snackbarHostState.showSnackbar(getString(result.error.message))
        }
    }

    Scaffold(
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
                onBackClick = {
                    if (!isMine || (isMine && match.status == MatchStatus.Finished))
                        navBack()
                    else if (match.status != MatchStatus.Finished && isMine)
                        quitRequested = !quitRequested
                },
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
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .padding(horizontal = dimensions.small)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.medium)
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
                        horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall),
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
            AnimatedVisibility(
                visible = match.status == MatchStatus.Live,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Court(
                    modifier = Modifier.fillMaxWidth(0.5f).aspectRatio(2f),
                    fill = MaterialTheme.colorScheme.primary,
                    stroke = MaterialTheme.colorScheme.onBackground,
                    hCourtSide = match.currentCourtSide,
                    side = match.currentServeSide,
                    win = match.winner != null
                )
            }

            AnimatedVisibility(
                visible = isMine && match.status != MatchStatus.Finished,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
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
                                if (match.rules.type == MatchType.Double) pointScored = Side.Team1
                                else onPointTeam1(match.team1.players.first { !it.bench })
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
                                if (match.rules.type == MatchType.Double) pointScored = Side.Team2
                                else onPointTeam2(match.team2.players.first { !it.bench })
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

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
            ) {
                val pagerState = rememberPagerState { tabs.size }
                PrimaryScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    divider = {},
                    edgePadding = dimensions.default
                ) {
                    tabs.fastForEachIndexed { i, tab ->
                        Tab(
                            selected = pagerState.currentPage == i,
                            onClick = { scope.launch { pagerState.animateScrollToPage(i) } },
                            text = { Text(tab, maxLines = 1) },
                        )
                    }
                }
                HorizontalPager(pagerState, Modifier.fillMaxSize()) {
                    when (it) {
                        0 -> TimelineList(
                            timeline = match.timeLine,
                            startTime = match.startedAt,
                            team1Name = match.team1.name,
                            team2Name = match.team2.name
                        )

                        1 -> Stats(
                            match.timeLine,
                            match.team1.players + match.team2.players,
                            team1Name = match.team1.name,
                            team2Name = match.team2.name
                        )

                        2 -> Players(team1Players = match.team1.players, team2Players = match.team2.players, onSubOrTransfer = {}, mine = isMine)
                    }
                }
            }

        }


        if (match.status == MatchStatus.Finished && match.winner != null)
            ConfettiKit(
                modifier = Modifier.fillMaxSize(),
                parties = rain(),
            )

        EnsureDialog(
            visible = quitRequested && isMine,
            onDismiss = { quitRequested = false },
            cancelText = stringResource(Res.string.cancel),

            onConfirm = {
                finishMatch()
                navBack()
            },
            confirmText = "Finish This Match",

            title = stringResource(Res.string.quit),
            description = stringResource(Res.string.ensure_quit),

            additionalActionText = "Suspend this match until you get back",
            additionalAction = {
                navBack()
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
    }
}

fun rain(): List<Party> {
    return listOf(
        Party(
            speed = 0f,
            maxSpeed = 15f,
            damping = 0.9f,
            angle = Angle.BOTTOM,
            spread = Spread.ROUND,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 3.5.seconds).perSecond(100),
            position = Position.Relative(0.0, 0.0).between(Position.Relative(1.0, 0.0))
        )
    )
}