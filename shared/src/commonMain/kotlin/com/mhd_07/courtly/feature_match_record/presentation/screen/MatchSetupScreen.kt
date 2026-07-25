package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_record.domain.model.SetupStep
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchIntent
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchViewModel
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ChevronDown
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchSetupScreen(
    state: Match,
    navToGameRecord: () -> Unit,
    navBack: () -> Unit,
    onChangeName: (Side, String) -> Unit,
    onEditLocation: (String) -> Unit,
    onModeChange: (MatchMode) -> Unit,
    onBestOfChange: (Int) -> Unit,
    onTypeChange: (MatchType) -> Unit,
    startGame: (Side) -> Unit
) {
    LaunchedEffect(state) {
        println("Match Setup Screen: $state")
    }
//    LaunchedEffect(state){
//        println("Match Setup Screen: ${state}")
//    }
    val pages = listOf(
        SetupStep.TeamLeft,
        SetupStep.TeamRight,
        SetupStep.Location,
        SetupStep.Settings
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val nextEnabled by remember(pagerState.currentPage, state) {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> state.teamLeft.name.isNotEmpty()
                1 -> state.teamRight.name.isNotEmpty()
                2 -> state.location.isNotEmpty()
                else -> true
            }
        }
    }
    val scope = rememberCoroutineScope()
    BackHandler(scope) {
        println("Match Setup Screen: Back pressed, current page is num ${pagerState.currentPage}")
        if (pagerState.currentPage != 0)
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        else
            navBack()
    }

    val teamLeftFocus = remember { FocusRequester() }
    val teamRightFocus = remember { FocusRequester() }
    val locationFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(pagerState.currentPage) {
        when (pages[pagerState.currentPage]) {
            SetupStep.TeamLeft -> teamLeftFocus.requestFocus()
            SetupStep.TeamRight -> teamRightFocus.requestFocus()
            SetupStep.Location -> locationFocus.requestFocus()
            SetupStep.Settings -> focusManager.clearFocus()
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = "Setup Match", //TODO: Change to stringResource
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
            )
        }
    ) {
        val dimensions = LocalDimensions.current
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
                .padding(WindowInsets.ime.asPaddingValues())
                .padding(vertical = dimensions.medium, horizontal = dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.medium)
        ) {
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (pages[page]) {
                    SetupStep.TeamLeft -> {
                        SingleTextPage(
                            modifier = Modifier.fillMaxSize(),
                            title = "Team Left Name", //TODO: Change to stringResource
                            label = "Enter Team Left Name",//TODO: Change to stringResource
                            value = state.teamLeft.name,
                            onChange = { side -> onChangeName(Side.TeamLeft, side) },
                            next = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            focusRequester = teamLeftFocus
                        )
                    }

                    SetupStep.TeamRight -> {
                        SingleTextPage(
                            modifier = Modifier.fillMaxSize(),
                            title = "Team Right Name", //TODO: Change to stringResource
                            label = "Enter Team Right Name",//TODO: Change to stringResource
                            value = state.teamRight.name,
                            onChange = { side -> onChangeName(Side.TeamRight, side) },
                            next = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            focusRequester = teamRightFocus
                        )
                    }

                    SetupStep.Location -> {
                        SingleTextPage(
                            modifier = Modifier.fillMaxSize(),
                            title = "Location", //TODO: Change to stringResource
                            label = "Enter Location",//TODO: Change to stringResource
                            value = state.location,
                            onChange = onEditLocation,
                            next = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            focusRequester = locationFocus
                        )
                    }

                    SetupStep.Settings -> {
//            nextEnabled = true
                        GameSettingsPage(
                            modifier = Modifier.fillMaxSize(),
                            mode = state.mode,
                            onModeChange = onModeChange,
                            bestOf = state.bestOf,
                            onBestOfChange = onBestOfChange,
                            type = state.type,
                            onTypeChange = onTypeChange
                        )
                    }
                }
            }
            Button(onClick = {
                if (pagerState.currentPage != pagerState.pageCount - 1)
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                else {
                    startGame(Side.TeamLeft)
                    navToGameRecord()
                }
//                nextEnabled = false
            }, enabled = nextEnabled) {
                Text(if (pagerState.currentPage != pagerState.pageCount - 1) "Next" else "Start Game")
            }
        }
    }
}

@Composable
fun SingleTextPage(
    modifier: Modifier,
    title: String,
    label: String,
    value: String,
    onChange: (String) -> Unit,
    next: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                println("value = '$value'")
                if (value.isNotEmpty()) {
                    next()
                }
            }),
            maxLines = 2,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (value.isEmpty())
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        )
                    it()
                }
            }
        )
    }
}


@Composable
fun GameSettingsPage(
    modifier: Modifier,
    mode: MatchMode,
    onModeChange: (MatchMode) -> Unit,
    bestOf: Int,
    onBestOfChange: (Int) -> Unit,
    type: MatchType,
    onTypeChange: (MatchType) -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.SpaceBetween) {
        ModeSelectionField(
            modifier = Modifier.fillMaxWidth(),
            onSelected = onModeChange,
            selected = mode
        )
        BestOfSelectionField(
            modifier = Modifier.fillMaxWidth(),
            onSelected = onBestOfChange,
            selected = bestOf
        )
        TypeSelectionField(
            modifier = Modifier.fillMaxWidth(),
            onSelected = onTypeChange,
            selected = type
        )
    }
}


@Composable
fun OptionSelector(
    modifier: Modifier,
    onSelected: (Int) -> Unit,
    selected: String,
    options: List<String>,
    textAlign: TextAlign = TextAlign.Start
) {
    var dropped by remember { mutableStateOf(false) }
    BasicTextField(
        value = selected,
        onValueChange = {},
        readOnly = true,
        modifier = modifier,
        maxLines = 2,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = textAlign
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = {
            Column(
                modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    it()
                    IconButton(onClick = { dropped = !dropped }) {
                        Icon(
                            imageVector = TablerIcons.Outlined.ChevronDown,
                            contentDescription = "Dropdown"
                        ) //TODO: Change to stringResource

                        DropdownMenu(
//                        modifier = Modifier.fillMaxWidth(),
                            expanded = dropped,
                            onDismissRequest = { dropped = !dropped }) {
                            options.forEachIndexed { index, string ->
                                DropdownMenuItem(
                                    text = { Text(string) },
                                    onClick = {
                                        onSelected(index)
                                        dropped = false
                                    }
                                )
                            }
                        }
                    }
                }
                HorizontalDivider()
            }
//                if (dropped)
        }
    )
}

@Composable
fun ModeSelectionField(
    modifier: Modifier,
    selected: MatchMode,
    onSelected: (MatchMode) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        Text("Mode", style = MaterialTheme.typography.titleMedium) //TODO: Change to stringResource
        OptionSelector(
            modifier = Modifier.fillMaxWidth(),
            onSelected = { onSelected(MatchMode.entries[it]) },
            selected = selected.display,
            options = MatchMode.entries.map { it.display }
        )
        Text(
            if (selected == MatchMode.Professional) "Playing Like Competitions where set is a 6 games" else "Playing Like Community Games where set is a 1 games", //TODO: Change to stringResource
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun TypeSelectionField(
    modifier: Modifier,
    onSelected: (MatchType) -> Unit,
    selected: MatchType
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        Text("Type", style = MaterialTheme.typography.titleMedium) //TODO: Change to stringResource
        OptionSelector(
            modifier = Modifier.fillMaxWidth(),
            onSelected = { onSelected(MatchType.entries[it]) },
            selected = selected.display,
            options = MatchType.entries.map { it.display }
        )
        Text(
            "Play with 4 person or 2", //TODO: Change to stringResource
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun BestOfSelectionField(
    modifier: Modifier,
    onSelected: (Int) -> Unit,
    selected: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        val dimensions = LocalDimensions.current
        Text(
            "System",
            style = MaterialTheme.typography.titleMedium
        ) //TODO: Change to stringResource
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text("Best Of")
            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                selected = selected.toString(),
                options = listOf("Best Of 3", "Best Of 5"),
                onSelected = { onSelected(if (it == 0) 3 else 5) },
                textAlign = TextAlign.Center
            )
        }
        Text(
            "Win Game After 2 or 3 matches", //TODO: Change to stringResource
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )

    }
}