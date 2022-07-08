package com.osisupermoses.food_ordering_app.ui.checkout.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.sign_up.components.TextField
import com.osisupermoses.food_ordering_app.ui.ui_common.Button

@Composable
fun Address(
    modifier: Modifier,
    address: String,
    enabled: Boolean,
    addTextTile: Boolean = true,
    readOnly: Boolean,
    topText: String,
    saveBtnVisibility: Boolean = true,
    onAddressValueChange: (String) -> Unit,
    onEditAddressClick: (String) -> Unit,
    onSaveClick: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = address,
            isAddress = true,
            enabled = enabled,
            readOnly = readOnly,
            addTextTile = addTextTile,
            text = topText,
            onEditAddressClick = { onEditAddressClick.invoke(address) }
        ) {
            onAddressValueChange.invoke(it)
        }
        if (saveBtnVisibility) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .size(100.dp, 30.dp)
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.save)
            ) {
                onSaveClick.invoke(address)
            }
        }
    }
}