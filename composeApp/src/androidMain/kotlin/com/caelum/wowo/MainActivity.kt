package com.caelum.wowo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import di.commonModule
import di.domainModule
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import presentation.game.GameScreen
import presentation.game.GameViewModel
import presentation.game.component.CustomAlertDialog
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.exit
import wowo.composeapp.generated.resources.exit_text
import wowo.composeapp.generated.resources.exit_title

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val viewModel: GameViewModel = koinViewModel()
                val state = viewModel.state

                ExitHandler()
                GameScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    showRewardedAd = {}
                )
            }
        }
    }

    @Composable
    private fun ExitHandler() {
        var showExitDialog by remember { mutableStateOf(false) }

        if (showExitDialog) {
            CustomAlertDialog(
                onDismissRequest = { showExitDialog = false },
                onConfirmation = {
                    showExitDialog = false
                    finish()
                },
                dialogText = stringResource(Res.string.exit_text),
                dialogTitle = stringResource(Res.string.exit_title),
                positiveText = stringResource(Res.string.exit)
            )
        }
        BackHandler {
            showExitDialog = true
        }
    }
}
