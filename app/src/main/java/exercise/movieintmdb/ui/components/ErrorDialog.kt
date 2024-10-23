package exercise.movieintmdb.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(
            text = title,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.headlineSmall
        ) },
        text = { Text(
            text = message,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium
        ) },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "重新連接網路",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondary
    )
}