package com.caelum.wowo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import mobile_ui.MobileApp
import presentation.game.component.CustomAlertDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showExitDialog by remember { mutableStateOf(false) }

            if (showExitDialog) {
                CustomAlertDialog(
                    onDismissRequest = { showExitDialog = false },
                    onConfirmation = {
                        showExitDialog = false
                        finish()
                    },
                    dialogText = "are your sure to exit?",
                    dialogTitle = "exit",
                    positiveText = "exit"
                )
            }
            MobileApp()
            BackHandler {
                showExitDialog = true
            }
        }
    }
}
