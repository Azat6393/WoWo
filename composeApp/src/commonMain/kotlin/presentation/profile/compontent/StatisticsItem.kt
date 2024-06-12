package presentation.profile.compontent

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorSecondary
import org.jetbrains.compose.resources.Font
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.geologica_regular

@Composable
fun StatisticsItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    color: Color = ColorOnBackground,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = ColorSecondary,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
        Text(
            text = value.toString(),
            color = color,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(Res.font.geologica_medium))
        )
    }
}