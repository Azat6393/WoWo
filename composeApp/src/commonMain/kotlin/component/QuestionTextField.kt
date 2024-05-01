package component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorOnBackground
import component.ui.ColorPrimary
import component.ui.ColorSecondary

@Composable
fun QuestionTextField(
    modifier: Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onAskQuestion: () -> Unit,
    isAskQuestionEnable: Boolean,
    isLoading: Boolean,
) {

    val leftRoundedCornerShape =
        RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp, bottomEnd = 0.dp, topEnd = 0.dp)

    val rightRoundedCornerShape =
        RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, bottomEnd = 50.dp, topEnd = 50.dp)

    Row(modifier = modifier.height(55.dp)) {
        Box(
            modifier = Modifier.weight(0.75f, true).fillMaxHeight()
                .clip(leftRoundedCornerShape)
                .border(border = BorderStroke(1.dp, ColorOnBackground), leftRoundedCornerShape)
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                maxLines = 1,
                textStyle = TextStyle(
                    color = ColorOnBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                decorationBox = {
                    if (text.isBlank()) {
                        Text(
                            text = text.ifBlank { "Is this ...?" },
                            color = if (text.isBlank()) ColorSecondary.copy(alpha = 0.6f) else ColorOnBackground,
                            maxLines = 1,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    } else it()
                },
                modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                    .padding(start = 30.dp, end = 50.dp)
            )
            Text(
                text = "${text.length}/40",
                color = ColorSecondary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(bottom = 2.dp, end = 10.dp)
            )
        }
        Row(
            modifier = Modifier.weight(0.25f, true).fillMaxHeight()
                .offset(x = (-1).dp)
                .clip(rightRoundedCornerShape)
                .border(border = BorderStroke(1.dp, ColorOnBackground), rightRoundedCornerShape)
                .clickable { if (isAskQuestionEnable) onAskQuestion() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp),
                    color = ColorPrimary
                )
            }
            AnimatedVisibility(!isLoading) {
                Text(
                    text = "Ask",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorOnBackground
                )
            }
        }
    }
}