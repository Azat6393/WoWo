package com.caelum_software.wowo.compontent

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.stringResource
import presentation.component.CustomAlertDialog
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.exit
import wowo.composeapp.generated.resources.exit_text
import wowo.composeapp.generated.resources.exit_title

@Composable
fun ExitHandler(
    exit: () -> Unit
) {
    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        CustomAlertDialog(
            onDismissRequest = { showExitDialog = false },
            onConfirmation = {
                showExitDialog = false
                exit()
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