package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.domain.model.MatchMode
import com.mhd_07.courtly.core.domain.model.MatchType
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import com.mhd_07.courtly.feature_match_record.presentation.screen.components.OptionSelector
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.freestyle_description
import courtly.shared.generated.resources.mode
import courtly.shared.generated.resources.mode_type
import courtly.shared.generated.resources.professional_description
import courtly.shared.generated.resources.type
import courtly.shared.generated.resources.type_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModeTypePage(
    mode: MatchMode,
    type: MatchType,
    onModeChange: (MatchMode) -> Unit,
    onTypeChange: (MatchType) -> Unit
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        Text(text = stringResource(Res.string.mode_type), style = titleTextStyle)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Column(modifier = Modifier.fillMaxWidth()){
                Text(text = stringResource(Res.string.mode))
                Text(
                    text = stringResource(if (mode == MatchMode.FreeStyle) Res.string.freestyle_description else Res.string.professional_description),
                    style = notesTextStyle
                )
            }
            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                options = MatchMode.entries.map { stringResource(it.display) },
                selectedOptionIndex = MatchMode.entries.indexOf(mode),
                onOptionSelected = { onModeChange(MatchMode.entries[it]) }
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Column(modifier = Modifier.fillMaxWidth()){
                Text(text = stringResource(Res.string.type))
                Text(text = stringResource(Res.string.type_description), style = notesTextStyle)
            }
            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                options = MatchType.entries.map { stringResource(it.display) },
                selectedOptionIndex = MatchType.entries.indexOf(type),
                onOptionSelected = { onTypeChange(MatchType.entries[it]) }
            )
        }
    }
}