package component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    modifier: Modifier,
) {
    Card(
        modifier = modifier.height(50.dp),
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "WoWo",
                color = MaterialTheme.colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
