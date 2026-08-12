package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.team_left_name
import courtly.shared.generated.resources.team_left_name_placeholder
import courtly.shared.generated.resources.team_right_name
import courtly.shared.generated.resources.team_right_name_placeholder
import courtly.shared.generated.resources.teams_names
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeamsNamesPage(
    teamRightName: String,
    teamLeftName: String,
    onTeamRightNameChange: (String) -> Unit,
    onTeamLeftNameChange: (String) -> Unit
) {
    val dimensions = LocalDimensions.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        Text(text = stringResource(Res.string.teams_names), style = titleTextStyle)
//        Box(
//            modifier = Modifier.fillMaxWidth().border(
//                dimensions.xxSmall, Color.Gray,
//                MaterialTheme.shapes.medium
//            ),
//        ) {
        Column(
//                modifier = Modifier.padding(dimensions.small),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(text = stringResource(Res.string.team_left_name))
            OutlinedTextField(
                value = teamLeftName,
                onValueChange = onTeamLeftNameChange,
                placeholder = { Text(text = stringResource(Res.string.team_left_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
            )
        }
//        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text("vs", color = Color.Gray)
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Gray)
        }
//        Box(
//            modifier = Modifier.fillMaxWidth().border(
//                dimensions.xxSmall, Color.Gray,
//                MaterialTheme.shapes.medium
//            ),
//        ) {
        Column(
//                modifier = Modifier.padding(dimensions.small),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(text = stringResource(Res.string.team_right_name))
            OutlinedTextField(
                value = teamRightName,
                onValueChange = onTeamRightNameChange,
                placeholder = { Text(text = stringResource(Res.string.team_right_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }
//        }
    }
}