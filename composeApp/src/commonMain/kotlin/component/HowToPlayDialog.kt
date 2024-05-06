package component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import component.ui.ColorBackground
import component.ui.ColorOnBackground
import component.ui.ColorSecondary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.game.LetterCondition
import presentation.game.WordLetterUI
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.ask_image
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.geologica_semibold
import wowo.composeapp.generated.resources.how_to_play
import wowo.composeapp.generated.resources.how_to_play_eight
import wowo.composeapp.generated.resources.how_to_play_eleven
import wowo.composeapp.generated.resources.how_to_play_five
import wowo.composeapp.generated.resources.how_to_play_four
import wowo.composeapp.generated.resources.how_to_play_nine
import wowo.composeapp.generated.resources.how_to_play_one
import wowo.composeapp.generated.resources.how_to_play_seven
import wowo.composeapp.generated.resources.how_to_play_six
import wowo.composeapp.generated.resources.how_to_play_ten
import wowo.composeapp.generated.resources.how_to_play_three
import wowo.composeapp.generated.resources.how_to_play_two

@Composable
fun HowToPlayDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(vertical = 20.dp, horizontal = 10.dp),
            shape = RoundedCornerShape(15.dp),
            color = ColorBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 20.dp, horizontal = 20.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(Res.string.how_to_play),
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_semibold)),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp).align(Alignment.CenterEnd)
                            .clickable { onDismissRequest() }
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_one),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_medium)),
                        textAlign = TextAlign.Start,
                        color = ColorOnBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_two),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_three),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Image(
                        painter = painterResource(Res.drawable.ask_image),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_four),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_five),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_medium)),
                        textAlign = TextAlign.Start,
                        color = ColorOnBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    WordContainer(
                        modifier = Modifier.fillMaxWidth(),
                        wordLetters = generateWordUI(
                            word = "MEXICO",
                            index = 4,
                            condition = LetterCondition.InCorrectSpot
                        ),
                        style = WordStyleSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_six),
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    WordContainer(
                        modifier = Modifier.fillMaxWidth(),
                        wordLetters = generateWordUI(
                            word = "CANADA",
                            index = 0,
                            condition = LetterCondition.WrongSpot
                        ),
                        style = WordStyleSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_seven),
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    WordContainer(
                        modifier = Modifier.fillMaxWidth(),
                        wordLetters = generateWordUI(
                            word = "SWEDEN",
                            index = 2,
                            condition = LetterCondition.NotInWord
                        ),
                        style = WordStyleSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_eight),
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_nine),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_ten),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = stringResource(Res.string.how_to_play_eleven),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                        textAlign = TextAlign.Start,
                        color = ColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private fun generateWordUI(
    word: String,
    index: Int,
    condition: LetterCondition,
): List<WordLetterUI> {
    val wordLetters = mutableStateListOf<WordLetterUI>()
    word.forEachIndexed { i, c ->
        wordLetters.add(
            WordLetterUI(
                index = i,
                letter = c.toString(),
                condition = if (i == index) condition else LetterCondition.Input
            )
        )
    }
    return wordLetters
}
