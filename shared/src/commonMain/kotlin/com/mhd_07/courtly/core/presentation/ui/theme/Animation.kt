package com.mhd_07.courtly.core.presentation.ui.theme

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset

val pushTransform = fadeIn(tween(500, easing = EaseOut)) + slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(500, easing = EaseOut)
) togetherWith slideOutHorizontally(
    targetOffsetX = { -it / 4 },
    animationSpec = tween(500, easing = EaseOut)
) + fadeOut(tween(500, easing = EaseOut))

val popTransform = fadeIn(tween(500, easing = EaseOut)) + slideInHorizontally(
    initialOffsetX = { -it / 4 },
    animationSpec = tween(500, easing = EaseOut)
) togetherWith slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(500, easing = EaseOut)
) + fadeOut(tween(500, easing = EaseOut))

val predictiveTransform = EnterTransition.None togetherWith slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(500, easing = EaseOut)
) + fadeOut(tween(500, easing = EaseOut))