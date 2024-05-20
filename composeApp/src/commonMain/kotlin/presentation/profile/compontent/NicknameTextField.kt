package presentation.profile.compontent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorPrimary
import presentation.component.ui.ColorSecondary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.nickname

@Composable
fun NicknameTextField(
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    saveNickname: () -> Unit,
    nickname: String,
) {
    var isEnable by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = nickname,
            onValueChange = onTextChange,
            enabled = isEnable,
            readOnly = !isEnable,
            textStyle = TextStyle(
                color = ColorOnBackground,
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(Res.font.geologica_medium)),
                textAlign = TextAlign.Start
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(ColorPrimary),
            modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.CenterStart),
            decorationBox = { innerText ->
                if(nickname.isEmpty()){
                    Text(
                        text = stringResource(Res.string.nickname),
                        color = ColorSecondary,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_medium)),
                        textAlign = TextAlign.Start
                    )
                }
                innerText.invoke()
            }
        )
        Icon(
            imageVector = if (isEnable) Icons.Default.Check else Icons.Default.Edit,
            contentDescription = null,
            tint = ColorOnBackground,
            modifier = Modifier.size(24.dp)
                .clickable {
                    if (isEnable) saveNickname()
                    isEnable = !isEnable
                }
                .align(Alignment.CenterEnd)
        )
    }
}