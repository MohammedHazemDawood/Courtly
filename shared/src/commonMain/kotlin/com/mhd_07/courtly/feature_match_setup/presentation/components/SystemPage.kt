package com.mhd_07.courtly.feature_match_setup.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.best_of
import courtly.shared.generated.resources.best_of_3_description
import courtly.shared.generated.resources.best_of_5_description
import courtly.shared.generated.resources.system
import courtly.shared.generated.resources.system_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun SystemPage(
    system: Int,
    onSystemChange: (Int) -> Unit
) {
    val options = listOf(3, 5)
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(Res.string.system), style = titleTextStyle)
            Text(text = stringResource(Res.string.system_description), style = notesTextStyle)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(Res.string.system))
                Text(
                    text = stringResource(if (system == 3) Res.string.best_of_3_description else Res.string.best_of_5_description),
                    style = notesTextStyle
                )
            }
            OptionSelector(
                modifier = Modifier.fillMaxWidth(),
                options = options.map { stringResource(Res.string.best_of, it) },
                selectedOptionIndex = options.indexOf(system),
                onOptionSelected = { onSystemChange(options[it]) }
            )
        }
    }
}