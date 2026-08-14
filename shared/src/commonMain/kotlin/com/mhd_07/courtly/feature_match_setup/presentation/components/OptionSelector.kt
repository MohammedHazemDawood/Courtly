package com.mhd_07.courtly.feature_match_setup.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@Composable
fun OptionSelector(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOptionIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.small)
    ) {
        options.forEachIndexed { index, option ->
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(
                    dimensions.xxSmall,
                    color = if (selectedOptionIndex == index) MaterialTheme.colorScheme.primary else Color.Gray
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedOptionIndex == index) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.5f
                    )
                    else Color.Unspecified,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = { onOptionSelected(index) }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(dimensions.small),
                    contentAlignment = Alignment.Center
                ) { Text(text = option) }
            }
        }
    }
}