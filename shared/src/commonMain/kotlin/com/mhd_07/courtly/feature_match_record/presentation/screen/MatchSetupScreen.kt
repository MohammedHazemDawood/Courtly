package com.mhd_07.courtly.feature_match_record.presentation.screen

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
import com.mhd_07.courtly.core.domain.model.Match
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.buttonTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.fieldsTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.core.util.BackHandler
import com.mhd_07.courtly.feature_match_record.domain.model.SetupStep
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.best_of
import courtly.shared.generated.resources.best_of_3
import courtly.shared.generated.resources.best_of_3_description
import courtly.shared.generated.resources.best_of_5
import courtly.shared.generated.resources.best_of_5_description
import courtly.shared.generated.resources.drop
import courtly.shared.generated.resources.freestyle_description
import courtly.shared.generated.resources.location
import courtly.shared.generated.resources.location_placeholder
import courtly.shared.generated.resources.mode
import courtly.shared.generated.resources.next
import courtly.shared.generated.resources.professional_description
import courtly.shared.generated.resources.setup
import courtly.shared.generated.resources.start_match
import courtly.shared.generated.resources.system
import courtly.shared.generated.resources.team_left_name
import courtly.shared.generated.resources.team_left_name_placeholder
import courtly.shared.generated.resources.team_right_name
import courtly.shared.generated.resources.team_right_name_placeholder
import courtly.shared.generated.resources.type
import courtly.shared.generated.resources.type_description
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ChevronDown
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

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
                            title = stringResource(Res.string.team_left_name),
                            label = stringResource(Res.string.team_left_name_placeholder),
                            value = state.teamLeft.name,
                            onChange = { name -> onChangeName(Side.TeamLeft, name) },
                            next = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            focusRequester = teamLeftFocus
                        )
                    }

                    SetupStep.TeamRight -> {
                        SingleTextPage(
                            modifier = Modifier.fillMaxSize(),
                            title = stringResource(Res.string.team_right_name),
                            label = stringResource(Res.string.team_right_name_placeholder),
                            value = state.teamRight.name,
                            onChange = { side -> onChangeName(Side.TeamRight, side) },
                            next = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                            focusRequester = teamRightFocus
                        )
                    }

                    SetupStep.Location -> {
                        SingleTextPage(
                            modifier = Modifier.fillMaxSize(),
                            title = stringResource(Res.string.location),
                            label = stringResource(Res.string.location_placeholder),
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
            }, enabled = nextEnabled, modifier = Modifier.fillMaxWidth()) {
                Text(
                    stringResource(if (pagerState.currentPage != pagerState.pageCount - 1) Res.string.next else Res.string.start_match),
                    style = buttonTextStyle
                )
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
    next: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xSmall)
    ) {
        Text(
            title,
            style = titleTextStyle
        )
        BasicTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                if (value.isNotEmpty()) {
                    next()
                }
            }),
            maxLines = 2,
            textStyle = fieldsTextStyle.copy(color = MaterialTheme.colorScheme.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (value.isEmpty())
                        Text(
                            text = label,
                            style = fieldsTextStyle.copy(color = Color.Gray)
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
        textStyle = fieldsTextStyle.copy(
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
                            contentDescription = stringResource(Res.string.drop),
                        )

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
        Text(stringResource(Res.string.mode), style = titleTextStyle)
        OptionSelector(
            modifier = Modifier.fillMaxWidth(),
            onSelected = { onSelected(MatchMode.entries[it]) },
            selected = stringResource(selected.display),
            options = MatchMode.entries.map { stringResource(it.display) }
        )
        Text(
            stringResource(if (selected == MatchMode.Professional) Res.string.professional_description else Res.string.freestyle_description),
            style = notesTextStyle
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
        Text(stringResource(Res.string.type), style = titleTextStyle)
        OptionSelector(
            modifier = Modifier.fillMaxWidth(),
            onSelected = { onSelected(MatchType.entries[it]) },
            selected = stringResource(selected.display),
            options = MatchType.entries.map { stringResource(it.display) }
        )
        Text(
            stringResource(Res.string.type_description),
            style = notesTextStyle
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
            stringResource(Res.string.system),
            style = titleTextStyle
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(stringResource(Res.string.best_of), style = fieldsTextStyle)
            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                selected = selected.toString(),
                options = listOf(
                    stringResource(Res.string.best_of_3),
                    stringResource(Res.string.best_of_5)
                ),
                onSelected = { onSelected(if (it == 0) 3 else 5) },
                textAlign = TextAlign.Center
            )
        }
        Text(
            stringResource(if (selected == 3) Res.string.best_of_3_description else Res.string.best_of_5_description),
            style = notesTextStyle
        )

    }
}