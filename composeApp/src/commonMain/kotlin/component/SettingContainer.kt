package component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorBackground
import component.ui.ColorOnBackground
import component.ui.ColorPrimary
import component.ui.ColorSecondary
import domain.model.Category
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.game.Difficulty
import presentation.game.GameSettings
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.category
import wowo.composeapp.generated.resources.difficulty
import wowo.composeapp.generated.resources.easy
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.hard
import wowo.composeapp.generated.resources.language
import wowo.composeapp.generated.resources.medium

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingContainer(
    modifier: Modifier = Modifier,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(Res.string.language),
            color = ColorSecondary,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
        Spacer(Modifier.height(5.dp))
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Start
        ) {
            gameSettings.languages.forEach { language ->
                Button(
                    onClick = { onLanguageSelect(language) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (language == gameSettings.selectedLanguage) ColorPrimary else ColorBackground,
                        contentColor = if (language == gameSettings.selectedLanguage) ColorBackground else ColorOnBackground
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (language == gameSettings.selectedLanguage) ColorPrimary else ColorOnBackground
                    ),
                    content = {
                        Text(
                            language,
                            fontFamily = FontFamily(Font(Res.font.geologica_regular))
                        )
                    }
                )
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(30.dp))
        Text(
            stringResource(Res.string.category),
            color = ColorSecondary,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
        Spacer(Modifier.height(5.dp))
        LazyRow {
            items(items = gameSettings.categories, key = { it.uuid }) { category ->
                Button(
                    onClick = { onCategorySelect(category) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else ColorBackground,
                        contentColor = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorBackground else ColorOnBackground
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else ColorOnBackground
                    ),
                    content = {
                        Text(
                            category.name,
                            fontFamily = FontFamily(Font(Res.font.geologica_regular))
                        )
                    }
                )
                Spacer(Modifier.width(10.dp))
            }
        }
        /*  FlowRow(
              verticalArrangement = Arrangement.Center,
              horizontalArrangement = Arrangement.Start,
          ) {
              gameSettings.categories.forEach { category ->
                  Button(
                      onClick = { onCategorySelect(category) },
                      colors = ButtonDefaults.buttonColors(
                          backgroundColor = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else ColorBackground,
                          contentColor = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorBackground else ColorOnBackground
                      ),
                      border = BorderStroke(
                          width = 1.dp,
                          color = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else ColorOnBackground
                      ),
                      content = {
                          Text(
                              category.name,
                              fontFamily = FontFamily(Font(Res.font.geologica_regular))
                          )
                      }
                  )
                  Spacer(Modifier.width(10.dp))
              }
          }*/
        Spacer(Modifier.height(30.dp))
        Text(
            stringResource(Res.string.difficulty),
            color = ColorSecondary,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
        Spacer(Modifier.height(5.dp))
        DifficultyButtons(onDifficultySelect, gameSettings.difficulty)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DifficultyButtons(
    onDifficultySelect: (Difficulty) -> Unit,
    difficulty: Difficulty,
) {
    FlowRow(
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Start
    ) {
        Button(
            onClick = { onDifficultySelect(Difficulty.Easy) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (difficulty == Difficulty.Easy) ColorPrimary else ColorBackground,
                contentColor = if (difficulty == Difficulty.Easy) ColorBackground else ColorOnBackground
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (difficulty == Difficulty.Easy) ColorPrimary else ColorOnBackground
            ),
            content = {
                Text(
                    stringResource(Res.string.easy),
                    fontFamily = FontFamily(Font(Res.font.geologica_regular))
                )
            }
        )
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { onDifficultySelect(Difficulty.Medium) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (difficulty == Difficulty.Medium) ColorPrimary else ColorBackground,
                contentColor = if (difficulty == Difficulty.Medium) ColorBackground else ColorOnBackground
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (difficulty == Difficulty.Medium) ColorPrimary else ColorOnBackground
            ),
            content = {
                Text(
                    stringResource(Res.string.medium),
                    fontFamily = FontFamily(Font(Res.font.geologica_regular))
                )
            }
        )
        Spacer(Modifier.width(10.dp))
        Button(
            onClick = { onDifficultySelect(Difficulty.Hard) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (difficulty == Difficulty.Hard) ColorPrimary else ColorBackground,
                contentColor = if (difficulty == Difficulty.Hard) ColorBackground else ColorOnBackground
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (difficulty == Difficulty.Hard) ColorPrimary else ColorOnBackground
            ),
            content = {
                Text(
                    stringResource(Res.string.hard),
                    fontFamily = FontFamily(Font(Res.font.geologica_regular))
                )
            }
        )
    }
}
