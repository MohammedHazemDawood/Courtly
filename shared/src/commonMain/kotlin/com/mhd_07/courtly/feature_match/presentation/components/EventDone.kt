package com.mhd_07.courtly.feature_match.presentation.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import com.mhd_07.courtly.feature_match.domain.model.Event
import courtly.shared.generated.resources.Res
import courtly.shared.generated.resources.the_end
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant


@Composable
fun EventDone(event: Event.Done, startTime: Instant?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.xxSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.small)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(stringResource(Res.string.the_end))
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Text("${minutes(event.createdAt, startTime)}'")
    }
}