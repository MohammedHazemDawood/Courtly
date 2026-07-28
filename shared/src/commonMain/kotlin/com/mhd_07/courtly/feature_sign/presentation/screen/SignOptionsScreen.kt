package com.mhd_07.courtly.feature_sign.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.filled.BrandFacebook
import dev.seyfarth.tablericons.filled.BrandGoogle

@Composable
fun SignOptionsScreen(googleSignIn: () -> Unit, navToEmailSign: () -> Unit) {
    val dimensions = LocalDimensions.current
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = dimensions.large, horizontal = dimensions.xSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //TODO: Add App Icon Later
        Text(text = "Welcome To Courtly", style = MaterialTheme.typography.titleLarge)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensions.xSmall,
                    Alignment.CenterHorizontally
                )
            ) {
                IconButton(
                    modifier = Modifier./*size(dimensions.large).*/border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape
                    ),
                    onClick = { googleSignIn() },
//                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = TablerIcons.Filled.BrandGoogle,
                        contentDescription = "Sign With Google"
                    )//TODO: use string resource instead
                }
                IconButton(
                    modifier = Modifier./*size(dimensions.large).*/border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape
                    ),
                    onClick = { },
//                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = TablerIcons.Filled.BrandFacebook,
                        contentDescription = "Sign With Facebook"
                    )//TODO: use string resource instead
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(0.66f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = dimensions.xSmall)
                )//Todo: use string resource instead
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            Button(onClick = navToEmailSign, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Sign With Email")//Todo: use string resource instead
            }
        }
    }
}