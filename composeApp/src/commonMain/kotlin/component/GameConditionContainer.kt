package component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorPrimary
import component.ui.ColorSecondary
import presentation.game.GameConditionsUI

@Composable
fun GameConditionContainer(
    modifier: Modifier,
    gameCondition: GameConditionsUI,
    isWeb: Boolean = false
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GameConditionItem(
            title = "Incorrect",
            text = "${gameCondition.attempts}/${gameCondition.maxAttempts}",
            isWeb = isWeb
        )
        Spacer(Modifier.width(if (isWeb) 120.dp else 80.dp))
        GameConditionItem(
            title = "Questions",
            text = "${gameCondition.question}/${gameCondition.maxQuestion}",
            isWeb = isWeb
        )
    }
}

@Composable
fun GameConditionItem(
    title: String,
    text: String,
    isWeb: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = ColorSecondary,
            fontSize = if (isWeb) 20.sp else 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = text,
            color = ColorPrimary,
            fontSize = if (isWeb) 36.sp else 30.sp,
            fontWeight = FontWeight.Normal
        )
    }
}