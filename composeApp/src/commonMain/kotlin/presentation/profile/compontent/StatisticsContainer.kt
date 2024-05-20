package presentation.profile.compontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.component.DifficultyButtons
import presentation.component.ui.ColorLightGray
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorSecondary
import domain.model.Statistics
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.game.Difficulty
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.attempts_per_game
import wowo.composeapp.generated.resources.current_streak
import wowo.composeapp.generated.resources.easy
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.geologica_semibold
import wowo.composeapp.generated.resources.hard
import wowo.composeapp.generated.resources.loses
import wowo.composeapp.generated.resources.max_streak
import wowo.composeapp.generated.resources.medium
import wowo.composeapp.generated.resources.played
import wowo.composeapp.generated.resources.questions_per_game
import wowo.composeapp.generated.resources.wins

@Composable
fun StatisticsContainer(
    modifier: Modifier = Modifier,
    easyStatistics: Statistics,
    mediumStatistics: Statistics,
    hardStatistics: Statistics,
) {
    var difficulty by remember { mutableStateOf(Difficulty.Easy) }
    var currentStatistics by remember { mutableStateOf(easyStatistics) }

    LaunchedEffect(key1 = difficulty, key2 = easyStatistics) {
        currentStatistics = when (difficulty) {
            Difficulty.Easy -> easyStatistics
            Difficulty.Medium -> mediumStatistics
            Difficulty.Hard -> hardStatistics
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "filters",
            color = ColorSecondary,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
        DifficultyButtons(
            onDifficultySelect = { difficulty = it },
            difficulty = difficulty
        )
        Spacer(Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ColorLightGray)
                .padding(vertical = 15.dp, horizontal = 20.dp)
        ) {
            Text(
                text = when (difficulty) {
                    Difficulty.Hard -> stringResource(Res.string.hard)
                    Difficulty.Medium -> stringResource(Res.string.medium)
                    Difficulty.Easy -> stringResource(Res.string.easy)
                },
                color = ColorOnBackground,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(Res.font.geologica_semibold))
            )
            Spacer(Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticsItem(
                    title = stringResource(Res.string.played),
                    value = currentStatistics.played
                )
                StatisticsItem(
                    title = stringResource(Res.string.wins),
                    value = currentStatistics.wins,
                )
                StatisticsItem(
                    title = stringResource(Res.string.loses),
                    value = currentStatistics.loses,
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                StatisticsItem(
                    title = stringResource(Res.string.questions_per_game),
                    value = if (currentStatistics.totalQuestion > 0)
                        currentStatistics.totalQuestion / currentStatistics.played else 0,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                StatisticsItem(
                    title = stringResource(Res.string.attempts_per_game),
                    value = if (currentStatistics.totalAttempt > 0)
                        currentStatistics.totalAttempt / currentStatistics.played else 0,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                StatisticsItem(
                    title = stringResource(Res.string.current_streak),
                    value = currentStatistics.currentStreak,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                StatisticsItem(
                    title = stringResource(Res.string.max_streak),
                    value = currentStatistics.maxStreak,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}