package com.osisupermoses.food_ordering_app.ui.checkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.sign_up.components.TextField
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.Rubik
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.ui_common.Button
import org.w3c.dom.Text

@Composable
fun Address(
    modifier: Modifier,
    address: TextFieldValue,
    enabled: Boolean,
    addTextTile: Boolean = true,
    readOnly: Boolean,
    topText: String,
    saveBtnVisibility: Boolean = true,
    onAddressValueChange: (TextFieldValue) -> Unit,
    onEditAddressClick: (TextFieldValue) -> Unit,
    onSaveClick: (TextFieldValue) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CheckoutTextField(
            textFieldValue = address,
            isAddress = true,
            enabled = enabled,
            readOnly = readOnly,
            addTextTile = addTextTile,
            text = topText,
            onEditAddressClick = { onEditAddressClick(address) }
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

@Composable
fun CheckoutTextField(
    modifier: Modifier = Modifier,
    isAddress: Boolean = false,
    addTextTile: Boolean = true,
    textFieldValue: TextFieldValue,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isSingleLine: Boolean = true,
    placeholderText: String = "",
    icon: ImageVector? = null,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    onEditAddressClick: () -> Unit = {},
    onValueChanged: (TextFieldValue) -> Unit = {}
) {
    val passwordFocusRequest = FocusRequester.Default
    Column {
        if(addTextTile) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon",
                        tint = Color(0xff696F79),
                        modifier = Modifier.padding(
                            end = MaterialTheme.spacing.small
                        )
                    )
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.h5,
                    color = Color(0xffAAAAAA)
                )
            }
        }
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = onValueChanged,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(5.dp)),
            singleLine = isSingleLine,
            textStyle = TextStyle(
                fontFamily = Rubik,
                fontSize = 14.sp,
                letterSpacing = 0.02.em,
                lineHeight = 14.sp,
                color = MaterialTheme.colors.onBackground
            ),
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                cursorColor = Color(0xffDD0A35),
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xffDD0A35),
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(CenterHorizontally)
                )
            },
            trailingIcon = {
                if (!isAddress) Unit
                else Box(modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = GoldYellow,
                        shape = CircleShape
                    )
                ) {
                    if (!enabled || enabled)
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable { onEditAddressClick.invoke() }
                        )
                }
            }
        )
    }
}