package com.osisupermoses.food_ordering_app.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.ui_common.Button
import com.osisupermoses.food_ordering_app.ui.sign_up.components.*
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    toMenuScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val state = signUpViewModel.state.value
    val fullName = signUpViewModel.fullName
    val email = signUpViewModel.userEmail
    val password = signUpViewModel.password
    val phoneNum = signUpViewModel.phoneNumber
    val isChecked = signUpViewModel.isChecked
    val countriesList = signUpViewModel.countriesList
    val mobileCountry = signUpViewModel.mobileCountry
    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = scaffoldState) {
        signUpViewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    Scaffold(
        topBar = {
            SignUpTopBar(text = stringResource(R.string.register_to_access))
        },
        backgroundColor = Color(0xffDD0A35).copy(alpha = 0.05f),
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .border(
                    width = 1.dp,
                    color = Color(0xffDD0A35),
                    shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
                .background(
                    color = Color(0xffFFC300).copy(0.05f),
                    shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.please_register),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                TextField(
                    modifier = Modifier.height(55.dp),
                    value = fullName.value,
                    placeholderText = stringResource(R.string.txt_full_name),
                    icon = Icons.Default.Person,
                    text = stringResource(R.string.txt_full_name_placeholder)
                ) { signUpViewModel.onEvent(SignUpEvent.EnteredFullName(it)) }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                PhoneNumberTextField(
                    value = phoneNum.value,
                    mobileCountry = mobileCountry.value,
                    onCountrySelected = { selectedCountry ->
                        mobileCountry.value.code = selectedCountry.code
                        mobileCountry.value.nameCode = selectedCountry.nameCode
                    },
                    countriesList = countriesList
                ) {
                    signUpViewModel.onEvent(SignUpEvent.EnteredPhoneNumber(it))
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                EmailInput(
                    modifier = Modifier.height(55.dp),
                    email = email.value,
                    placeholderText = stringResource(R.string.txt_email_placeholder)
                ) { signUpViewModel.onEvent(SignUpEvent.EnteredEmail(it)) }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                PasswordInput(
                    modifier = Modifier.height(55.dp),
                    value = password.value,
                    placeholderText = stringResource(R.string.txt_password_placeholder)
                ) {
                    signUpViewModel.onEvent(SignUpEvent.EnteredPassword(it))
                }
                CheckedBoxWithText(isChecked.value) {
                    signUpViewModel.onEvent(SignUpEvent.OnCheckedChanged(it))
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Button(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    textColor = Color.White,
                    enabled = signUpViewModel.isValidEntry(),
                    text = stringResource(R.string.btn_get_started)
                ) {
                    keyboardController?.hide()
                    signUpViewModel.onWrongPasswordInput { toMenuScreen.invoke() }
                }
            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomCircularProgressIndicator(message = "Loading...")
                }
            }
        }
    }
}