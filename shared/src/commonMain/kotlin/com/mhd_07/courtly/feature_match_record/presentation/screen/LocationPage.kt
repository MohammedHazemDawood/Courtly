package com.mhd_07.courtly.feature_match_record.presentation.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.core.presentation.ui.theme.notesTextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.titleTextStyle
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.location
import courtly.shared.generated.resources.location_placeholder
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.Map
import org.jetbrains.compose.resources.stringResource

@Composable
fun LocationPage(
    location: String,
    onChangeLocation: (String) -> Unit
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        Column(modifier = Modifier.fillMaxWidth()/*, verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall)*/){
            Text(text = stringResource(Res.string.location), style = titleTextStyle)
            Text(text = stringResource(Res.string.location_placeholder), style = notesTextStyle)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            Text(text = stringResource(Res.string.location))
            OutlinedTextField(
                value = location,
                onValueChange = onChangeLocation,
                placeholder = { Text(stringResource(Res.string.location_placeholder)) },
                leadingIcon = { Icon(imageVector = TablerIcons.Outlined.Map, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}