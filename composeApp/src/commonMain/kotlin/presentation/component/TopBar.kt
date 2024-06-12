package presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.component.ui.ColorOnBackground
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_black
import wowo.composeapp.generated.resources.help_icon

@Composable
fun TopBar(
    modifier: Modifier,
    showHowToPlay: () -> Unit,
    openProfile: () -> Unit,
    profileVisible: Boolean = false,
) {
    Card(
        modifier = modifier.height(60.dp),
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "WoWo",
                color = MaterialTheme.colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily(Font(Res.font.geologica_black)),
                modifier = Modifier.align(Alignment.Center)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.help_icon),
                    contentDescription = null,
                    tint = ColorOnBackground,
                    modifier = Modifier.padding(end = 20.dp).size(30.dp)
                        .clickable { showHowToPlay() }
                )
                if (profileVisible) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = ColorOnBackground,
                        modifier = Modifier.padding(end = 20.dp).size(30.dp)
                            .clickable { openProfile() }
                    )
                }
            }
        }
    }
}
