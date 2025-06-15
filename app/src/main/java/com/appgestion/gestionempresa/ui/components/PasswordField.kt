package com.appgestion.gestionempresa.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    placeholder: String = "••••••",
    isError: Boolean = false,
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value               = password,
        onValueChange       = onPasswordChange,
        label               = { Text(label) },
        placeholder         = { Text(placeholder) },
        singleLine          = true,
        modifier            = modifier,
        isError             = isError,
        visualTransformation = if (visible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions     = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon        = {
            IconButton (onClick = { visible = !visible })
            {
                Icon(
                    imageVector   = if (visible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = if (visible)
                        "Ocultar contraseña"
                    else
                        "Mostrar contraseña"
                )
            }
        }
    )
}