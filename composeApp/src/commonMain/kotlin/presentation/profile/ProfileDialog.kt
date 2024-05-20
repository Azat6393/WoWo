package presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import presentation.component.ui.ColorBackground
import presentation.component.ui.ColorLightGray
import presentation.component.ui.ColorPrimary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.profile.compontent.NicknameTextField
import presentation.profile.compontent.StatisticsContainer
import presentation.profile.compontent.TotalStatisticsContainer
import utils.koinViewModel
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_semibold
import wowo.composeapp.generated.resources.profile

@Composable
fun ProfileDialog(onDismissRequest: () -> Unit) {

    val viewModel = koinViewModel<ProfileViewModel>()
    val state = viewModel.state

    LaunchedEffect(true) {
        viewModel.onEvent(ProfileEvent.GetUserInfo)
        viewModel.onEvent(ProfileEvent.GetUserStatistics)
    }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 600.dp)
                .padding(vertical = 20.dp, horizontal = 10.dp),
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
                        text = stringResource(Res.string.profile),
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
                Spacer(Modifier.height(30.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorLightGray)
                        .padding(vertical = 20.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    NicknameTextField(
                        modifier = Modifier.fillMaxWidth(),
                        onTextChange = { viewModel.onEvent(ProfileEvent.OnNickNameChanged(it)) },
                        saveNickname = { viewModel.onEvent(ProfileEvent.SaveNickname) },
                        nickname = state.nickname
                    )
                    Spacer(Modifier.height(15.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.score.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorPrimary,
                            fontFamily = FontFamily(Font(Res.font.geologica_semibold))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = ColorPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.height(15.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(), color = ColorBackground,
                        thickness = 2.dp, startIndent = 2.dp
                    )
                    Spacer(Modifier.height(15.dp))
                    TotalStatisticsContainer(
                        modifier = Modifier.fillMaxWidth(),
                        wins = state.totalWins,
                        loses = state.totalLoses,
                        played = state.totalPlayed,
                        questions = state.questionsPerGame,
                        attempts = state.attemptsPerGame
                    )
                }
                Spacer(Modifier.height(30.dp))
                StatisticsContainer(
                    modifier = Modifier.fillMaxWidth(),
                    easyStatistics = state.easyStatistics,
                    mediumStatistics = state.mediumStatistics,
                    hardStatistics = state.hardStatistics
                )
            }
        }
    }
}