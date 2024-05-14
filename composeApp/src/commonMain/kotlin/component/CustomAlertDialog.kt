package component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import component.ui.ColorBackground
import component.ui.ColorPrimary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.confirm
import wowo.composeapp.generated.resources.dismiss
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.geologica_semibold

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positiveText: String = stringResource(Res.string.confirm),
) {
    AlertDialog(
        backgroundColor = ColorBackground,
        title = {
            Text(
                text = dialogTitle,
                fontFamily = FontFamily(Font(Res.font.geologica_semibold))
            )
        },
        text = {
            Text(
                text = dialogText,
                fontFamily = FontFamily(Font(Res.font.geologica_regular))
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = positiveText,
                    fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                    color = ColorPrimary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = stringResource(Res.string.dismiss),
                    fontFamily = FontFamily(Font(Res.font.geologica_regular)),
                    color = ColorPrimary
                )
            }
        }
    )
}