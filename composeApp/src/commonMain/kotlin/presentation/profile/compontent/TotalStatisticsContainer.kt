package presentation.profile.compontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.ui.ColorGreen
import component.ui.ColorRed
import org.jetbrains.compose.resources.stringResource
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.attempts_per_game
import wowo.composeapp.generated.resources.loses
import wowo.composeapp.generated.resources.played
import wowo.composeapp.generated.resources.questions_per_game
import wowo.composeapp.generated.resources.wins

@Composable
fun TotalStatisticsContainer(
    modifier: Modifier = Modifier,
    wins: Int,
    played: Int,
    loses: Int,
    questions: Int,
    attempts: Int,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatisticsItem(
                title = stringResource(Res.string.played),
                value = played
            )
            StatisticsItem(
                title = stringResource(Res.string.wins),
                value = wins,
                color = ColorGreen,
            )
            StatisticsItem(
                title = stringResource(Res.string.loses),
                value = loses,
                color = ColorRed
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            StatisticsItem(
                title = stringResource(Res.string.questions_per_game),
                value = questions,
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            StatisticsItem(
                title = stringResource(Res.string.attempts_per_game),
                value = attempts,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}