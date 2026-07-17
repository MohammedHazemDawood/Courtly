package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mhd_07.courtly.core.domain.model.MatchStatus
import com.mhd_07.courtly.core.presentation.components.ActionIcon
import com.mhd_07.courtly.core.presentation.components.CourtlyAppBar
import com.mhd_07.courtly.feature_match_record.presentation.viewmodel.MatchRecordViewModel
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.live
import courtly.shared.generated.resources.redo
import courtly.shared.generated.resources.undo
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.ArrowBack
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun MatchScreen() {
    val viewModel = koinViewModel<MatchRecordViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val undoAvailable = viewModel.isUndoAvailable.collectAsStateWithLifecycle().value
    val redoAvailable = viewModel.isRedoAvailable.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CourtlyAppBar(
                title = state.value.status.display,
                dotVisible = state.value.status == MatchStatus.Live,
                titleColor = if (state.value.status == MatchStatus.Live) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                backVisible = true,
                onBackClick = { },
                actions = arrayOf(
                    ActionIcon(
                        TablerIcons.Outlined.ArrowBack,
                        contentDescription = stringResource(Res.string.undo),
                        action = {},
                        enabled = undoAvailable
                    ),
                    ActionIcon(
                        TablerIcons.Outlined.ArrowBack,
                        contentDescription = stringResource(Res.string.redo),
                        action = {},
                        enabled = redoAvailable
                    )
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}