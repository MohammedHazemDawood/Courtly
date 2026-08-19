package com.mhd_07.courtly.feature_match.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mhd_07.courtly.core.domain.model.Score
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@Composable
fun TeamSetsRow(
    sets: List<Int>,
    bestOf: Int,
    currentSetIndex: Int,
    currentGameScore: Score,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    isWinner: Boolean
) {
    val dimensions = LocalDimensions.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.xxSmall),
        modifier = modifier
    ) {
        sets.completeTo(bestOf, 0)
            .forEachIndexed { index, i ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall),
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = i.toString())
                        Text(text = "70", modifier = Modifier.visible(false))
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .fillMaxWidth().then(
                                Modifier.height(dimensions.xxSmall).background(
                                    color = if ((index == currentSetIndex && isPlaying)|| isWinner) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                            )
                    )
                }
            }
        AnimatedVisibility(
            visible = isPlaying,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(dimensions.xxSmall),
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = currentGameScore.display, color = MaterialTheme.colorScheme.primary)
                    Text(text = "AD.", modifier = Modifier.visible(false))
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(dimensions.xxSmall)
                )
            }
        }
    }
}

fun <T> List<T>.completeTo(size: Int, default: T): List<T> {
    if (this.size >= size) return this
    return object : AbstractList<T>() {
        override val size: Int = size
        override fun get(index: Int): T =
            this@completeTo.getOrElse(index) { default }
    }
}