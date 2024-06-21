package presentation.game.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorPrimary
import presentation.component.ui.ColorSecondary
import presentation.game.FocusedCompose
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.ask
import wowo.composeapp.generated.resources.ask_hint
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.geologica_semibold
import wowo.composeapp.generated.resources.question

@Composable
fun QuestionTextField(
    modifier: Modifier,
    text: String,
    onAskQuestion: () -> Unit,
    onFocusChanged: (FocusedCompose) -> Unit,
    focusedCompose: FocusedCompose,
    isAskQuestionEnable: Boolean,
    isLoading: Boolean,
    isEasyMode: Boolean,
) {

    val leftRoundedCornerShape =
        RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp, bottomEnd = 0.dp, topEnd = 0.dp)

    val rightRoundedCornerShape =
        RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, bottomEnd = 50.dp, topEnd = 50.dp)

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(text = ""))
    }

    LaunchedEffect(text) {
        textFieldValueState = TextFieldValue(text = text, selection = TextRange(text.length))
    }

    LaunchedEffect(focusedCompose) {
        if (focusedCompose is FocusedCompose.Word) focusManager.clearFocus()
    }

    Row(modifier = modifier.height(55.dp)) {
        Box(
            modifier = Modifier.weight(0.75f, true).fillMaxHeight()
                .clip(leftRoundedCornerShape)
                .border(
                    border =
                    when (focusedCompose) {
                        FocusedCompose.Question -> BorderStroke(2.dp, ColorPrimary)
                        FocusedCompose.Word -> BorderStroke(1.dp, ColorOnBackground)
                    }, leftRoundedCornerShape
                )
        ) {
            CompositionLocalProvider(LocalTextInputService provides null) {
                BasicTextField(
                    value = textFieldValueState,
                    onValueChange = { },
                    textStyle = TextStyle(
                        color = ColorOnBackground,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_medium))
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    cursorBrush = SolidColor(ColorPrimary),
                    decorationBox = {
                        if (text.isBlank()) {
                            Text(
                                text = text.ifBlank {
                                    if (isEasyMode) stringResource(Res.string.question) else
                                        stringResource(Res.string.ask_hint)
                                },
                                color = if (text.isBlank()) ColorSecondary.copy(alpha = 0.6f) else ColorOnBackground,
                                maxLines = 1,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(Res.font.geologica_medium))
                            )
                        }
                        it()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(start = 30.dp, end = 50.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) onFocusChanged(FocusedCompose.Question)
                        }
                )
            }


            Text(
                text = "${text.length}/40",
                color = ColorSecondary.copy(alpha = 0.6f),
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(bottom = 2.dp, end = 10.dp)
            )
        }
        Row(
            modifier = Modifier.weight(0.25f, true).fillMaxHeight()
                .offset(
                    x = when (focusedCompose) {
                        FocusedCompose.Question -> (-2).dp
                        FocusedCompose.Word -> (-1).dp
                    }
                )
                .clip(rightRoundedCornerShape)
                .border(
                    border = when (focusedCompose) {
                        FocusedCompose.Question -> BorderStroke(2.dp, ColorPrimary)
                        FocusedCompose.Word -> BorderStroke(1.dp, ColorOnBackground)
                    }, rightRoundedCornerShape
                )
                .clickable { if (isAskQuestionEnable) onAskQuestion() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = ColorPrimary
                )
            }
            AnimatedVisibility(!isLoading) {
                Text(
                    text = stringResource(Res.string.ask),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorOnBackground,
                    fontFamily = FontFamily(Font(Res.font.geologica_semibold))
                )
            }
        }
    }
}