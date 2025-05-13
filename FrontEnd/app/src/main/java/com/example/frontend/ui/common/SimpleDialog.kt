package com.example.frontend.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SimpleDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    text: String,
    buttonText: String = "OK"
){
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = title)
            },
            text = {
                Text(text = text)
            },
            confirmButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = buttonText)
                }
            }
        )
    }
}