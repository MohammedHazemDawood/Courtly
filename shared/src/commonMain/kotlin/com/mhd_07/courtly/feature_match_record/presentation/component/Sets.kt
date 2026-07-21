package com.mhd_07.courtly.feature_match_record.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mhd_07.courtly.core.domain.model.Side
import com.mhd_07.courtly.core.domain.model.Team
import com.mhd_07.courtly.core.presentation.ui.theme.LocalDimensions

@Composable
fun Sets(
    modifier: Modifier,
    leftName: String,
    rightName: String,
    bestOf: Int,
    finished: Boolean,
    winner: Side?,
    currentSet: Pair<Int, Int>,
    prevSets : List<Pair<Int, Int>>
) {
    val dimension = LocalDimensions.current
    ConstraintLayout(modifier = modifier) {
        val (teamLeftName, teamRightName, teamLeftSets, teamRightSets) = createRefs()
        createVerticalChain(
            teamLeftSets,
            teamRightSets.withChainParams(
                topMargin = dimension.small
            ),
            chainStyle = ChainStyle.Packed
        )

        TeamSets(
            modifier = Modifier.fillMaxWidth(if (bestOf >= 5) 0.5f else 0.35f)
                .constrainAs(teamLeftSets) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            playedSets = (prevSets).map { it.first } + currentSet.first,
            bestOf = bestOf,
            finished = finished,
            winner = winner == Side.TeamLeft
        )
        TeamSets(
            modifier = Modifier.fillMaxWidth(if (bestOf >= 5) 0.5f else 0.35f)
                .constrainAs(teamRightSets) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            playedSets = prevSets.map { it.second } + currentSet.second,
            bestOf = bestOf,
            finished = finished,
            winner = winner == Side.TeamRight
        )
        Text(
            text = leftName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(teamLeftName) {
                centerVerticallyTo(teamLeftSets)
                start.linkTo(parent.start, margin = dimension.xSmall)
                end.linkTo(teamLeftSets.start, margin = dimension.xSmall)
                width = Dimension.fillToConstraints
                horizontalBias = 0f
            }, textAlign = TextAlign.Start,
            color = if (winner == Side.TeamLeft) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = rightName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.constrainAs(teamRightName) {
                centerVerticallyTo(teamRightSets)
                start.linkTo(teamRightSets.end, margin = dimension.xSmall)
                end.linkTo(parent.end, margin = dimension.xSmall)
                width = Dimension.fillToConstraints
                horizontalBias = 1f
            }, textAlign = TextAlign.End,
            color = if (winner == Side.TeamRight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
    }
}
