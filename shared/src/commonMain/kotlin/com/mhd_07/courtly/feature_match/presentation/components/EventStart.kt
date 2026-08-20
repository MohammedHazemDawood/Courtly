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
import courtly.shared.generated.resources.started
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant


@Composable
fun EventStart(event: Event.Start) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(stringResource(Res.string.started))
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Text(event.createdAt.toLocaleFormat())
    }
}

@Composable
expect fun Instant.toLocaleFormat(): String