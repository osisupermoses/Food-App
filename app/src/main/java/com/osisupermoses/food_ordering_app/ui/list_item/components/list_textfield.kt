package com.osisupermoses.food_ordering_app.ui.list_item.components

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.sign_up.components.TextField

@Composable
fun TextFieldRowItem(
    value: String,
    text: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChanged: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = value,
            text = text,
            keyboardType = keyboardType,
            placeholderText = placeholder
        ) {
            onValueChanged.invoke(it)
        }
    }
}