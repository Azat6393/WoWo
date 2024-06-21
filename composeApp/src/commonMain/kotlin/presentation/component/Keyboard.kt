package presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import org.jetbrains.compose.resources.Font
import presentation.component.ui.ColorBackground
import presentation.component.ui.ColorKeyboardColor
import presentation.component.ui.ColorKeyboardNotInWordColor
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorPrimary
import utils.pxToDp
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_medium


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Keyboard(
    modifier: Modifier,
    onInputValue: (String) -> Unit,
    onDeleteValue: () -> Unit,
    onClearText: () -> Unit,
    onEnter: () -> Unit,
    language: String,
    isEnterEnable: Boolean,
    notInWordLetters: SnapshotStateList<String>,
) {

    var columnSize by remember { mutableStateOf(Size.Zero) }
    var itemWidth by remember { mutableStateOf(0f) }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(columnSize, language) {
        itemWidth = columnSize.width / getLetters(language)[0].size
    }

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates ->
                columnSize = layoutCoordinates.size.toSize()
            }
    ) {

        getLetters(language).forEach { list ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                list.forEach { letter ->
                    LetterItem(
                        modifier = Modifier.width(itemWidth.pxToDp()).height(50.dp).padding(2.dp),
                        letter = letter,
                        onClick = onInputValue,
                        notInWord = notInWordLetters.contains(letter.toString())
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 2.dp)) {
            Box(
                modifier = modifier
                    .weight(0.2f, true)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .background(if (!isEnterEnable) ColorKeyboardColor else ColorPrimary)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple()
                    ) { if (isEnterEnable) onEnter() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTER",
                    fontSize = 14.sp,
                    color = if (!isEnterEnable) ColorOnBackground else ColorBackground,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(Res.font.geologica_medium))
                )
            }
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = modifier
                    .weight(0.6f, true)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .background(ColorKeyboardColor)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple()
                    ) { onInputValue(" ") }
            )
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = modifier
                    .weight(0.2f, true)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .background(ColorKeyboardColor)
                    .combinedClickable(
                        onClick = { onDeleteValue() },
                        onLongClick = { onClearText() },
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = ColorOnBackground
                )
            }
        }
    }
}

@Composable
private fun LetterItem(
    modifier: Modifier,
    letter: Char,
    notInWord: Boolean,
    onClick: (String) -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(7.dp))
            .background(if (notInWord) ColorKeyboardNotInWordColor else ColorKeyboardColor)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple()
            ) {
                onClick(letter.toString())
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.uppercase(),
            fontSize = 16.sp,
            color = ColorOnBackground,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.geologica_medium))
        )
    }
}

private fun getLetters(language: String): List<List<Char>> {
    return when (language) {
        "tr" -> listOf(
            listOf(
                'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'ı', 'O', 'P', 'Ğ', 'Ü'
            ),
            listOf(
                'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Ş', 'İ'
            ),
            listOf(
                'Z', 'X', 'C', 'V', 'B', 'N', 'M', 'Ö', 'Ç'
            )
        )

        "ru" -> listOf(
            listOf(
                'Й', 'Ц', 'У', 'К', 'Е', 'Н', 'Г', 'Ш', 'Щ', 'З', 'Х', 'Ъ'
            ),
            listOf(
                'Ф', 'Ы', 'В', 'А', 'П', 'Р', 'О', 'Л', 'Д', 'Ж', 'Э'
            ),
            listOf(
                'Я', 'Ч', 'С', 'М', 'И', 'Т', 'Ь', 'Б', 'Ю', 'Ё'
            )
        )

        else -> listOf(
            listOf(
                'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'
            ),
            listOf(
                'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'
            ),
            listOf(
                'Z', 'X', 'C', 'V', 'B', 'N', 'M'
            )
        )
    }
}