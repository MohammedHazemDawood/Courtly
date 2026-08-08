package com.mhd_07.courtly.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions


@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    title: String,
    vararg items: @Composable () -> Unit
) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.xSmall)
    ) {
        if (title.isNotEmpty())
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        Column(
            modifier = Modifier.fillMaxSize().border(
                dimensions.xxSmall,
                MaterialTheme.colorScheme.surface,
                MaterialTheme.shapes.extraSmall
            ),
            verticalArrangement = Arrangement.spacedBy(dimensions.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier)
            items.forEach {
                it()
                if (it != items.last())
                    HorizontalDivider(
                        thickness = dimensions.xxSmall,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(horizontal = dimensions.small)
                    )
            }
            Spacer(modifier = Modifier)
        }
    }
}

@Composable
fun SettingsGroupItem(
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    title: String,
    textStyle: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    trailingIcon: ImageVector? = null,
    trailingIconContentDescription: String? = null,
    action: () -> Unit = {}
) {
    val dimensions = LocalDimensions.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = dimensions.small)
            .clickable(onClick = action),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.xSmall)
        ) {
            if (leadingIcon != null)
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = leadingIconContentDescription,
                    tint = if (color == Color.Unspecified) MaterialTheme.colorScheme.onBackground else color
                )
            Text(text = title, style = textStyle, color = color)
        }
        if (trailingIcon != null)
            Icon(
                imageVector = trailingIcon,
                contentDescription = trailingIconContentDescription,
                tint = if (color == Color.Unspecified) MaterialTheme.colorScheme.onBackground else color
            )
    }
}
