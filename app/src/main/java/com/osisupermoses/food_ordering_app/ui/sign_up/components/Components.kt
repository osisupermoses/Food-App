package com.osisupermoses.food_ordering_app.ui.sign_up.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.Rubik
import com.osisupermoses.food_ordering_app.ui.theme.black15
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.healthbloc.presentation.screens.register_user_screen.components.Country
import com.osisupermoses.healthbloc.presentation.screens.register_user_screen.components.getFlagEmojiFor


@Composable
fun SignUpTopBar(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = text,
            style = black15.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff042E46)
            ),
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun PhoneNumberTextField(
    value: String,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier,
    countriesList: List<Country>,
    mobileCountry: Country,
    onValueChanged: (String) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    val mobileCountryState = remember { mutableStateOf(mobileCountry) }

    Column{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Phone,
                contentDescription = "PhoneIcon",
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(
                    end = MaterialTheme.spacing.small
                )
            )
            Text(
                text = stringResource(R.string.txt_phone_num),
                color = Color(0xffAAAAAA),
                style = MaterialTheme.typography.h5
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(5.dp)),
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Rubik,
                fontSize = 14.sp,
                letterSpacing = 0.02.em,
                lineHeight = 14.sp,
                color = MaterialTheme.colors.onBackground
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone).copy(
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xffDD0A35),
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions ( onDone = { focusManager.clearFocus() }),
            placeholder = {
                Text(
                    text = stringResource(R.string.txt_phone_num_placeholder),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(CenterHorizontally)
                )
            },
            leadingIcon = {
                CountryPickerView(
                    countries = countriesList,
                    selectedCountry = mobileCountryState.value,
                    onSelection = onCountrySelected
                )
            },
            visualTransformation = PhoneNumberVisualTransformation() //{ mobileNumberFilter(it) }
        )
    }
}

@Composable
fun CountryPickerView(
    selectedCountry: Country,
    onSelection: (Country) -> Unit,
    countries: List<Country>
) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.clickable { showDialog = true },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 20.dp, end = 5.dp),
            text = "${getFlagEmojiFor(selectedCountry.nameCode)} + ${selectedCountry.code}",
            style = MaterialTheme.typography.h6
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "See more countries"
        )
    }
    if (showDialog)
        CountryCodePickerDialog(countries, onSelection) {
            showDialog = false
        }
}

@Composable
fun CountryCodePickerDialog(
    countries: List<Country>,
    onSelection: (Country) -> Unit,
    dismiss: () -> Unit,
) {
    Dialog(onDismissRequest = dismiss) {
        Box {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 40.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White)
            ) {
                for (country in countries) {
                    item {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onSelection(country)
                                    dismiss()
                                }
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = "${getFlagEmojiFor(country.nameCode)} ${country.fullName}"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
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
    onValueChanged: (String) -> Unit = {}
) {
    val passwordFocusRequest = FocusRequester.Default
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = CenterVertically
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
        OutlinedTextField(
            value = value,
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
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        )
    }
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String,
    enabled: Boolean = true,
    placeholderText: String = "",
    isSingleLine: Boolean = true,
    onAction: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
    onValueChanged: (String) -> Unit = {}
) {
    val passwordFocusRequest = FocusRequester.Default

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = "Email",
                tint = Color(0xff696F79),
                modifier = Modifier.padding(
                    end = MaterialTheme.spacing.small
                )
            )
            Text(
                text = stringResource(R.string.txt_email),
                style = MaterialTheme.typography.h5,
                color = Color(0xffAAAAAA)
            )
        }
        OutlinedTextField(
            value = email,
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xffDD0A35),
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions { passwordFocusRequest.requestFocus() },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(CenterHorizontally)
                )
            }
        )
    }
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    value: String,
    text: String = stringResource(R.string.txt_password),
    enabled: Boolean = true,
    placeholderText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit = {}
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Lock",
                tint = Color(0xff696F79),
                modifier = Modifier.padding(
                    end = MaterialTheme.spacing.small
                )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
                color = Color(0xffAAAAAA)
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = Rubik,
                fontSize = 14.sp,
                letterSpacing = 0.02.em,
                lineHeight = 14.sp,
                color = MaterialTheme.colors.onBackground
            ),
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequest),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xffDD0A35),
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            },
            visualTransformation =
                if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = "Password Visibility"
                    )
                }
            },
            keyboardActions = onAction
        )
    }
}

@Composable
fun CheckedBoxWithText(
    isChecked: Boolean,
    enabled: Boolean = true,
    onTextClicked: () -> Unit = {},
    onCheckedChanged: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChanged,
            enabled = enabled,
            colors = CheckboxDefaults.colors(checkedColor = Color.DarkGray)
        )
        Text(
            text = stringResource(R.string.txt_policy_normal),
            style = MaterialTheme.typography.body2,
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = stringResource(R.string.txt_policy_link),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            modifier = Modifier.clickable { onTextClicked.invoke() }
        )
    }
}