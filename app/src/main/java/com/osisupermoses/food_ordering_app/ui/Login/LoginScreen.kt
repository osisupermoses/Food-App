package com.osisupermoses.food_ordering_app.ui.Login

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.ui.ui_common.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.Login.components.LoginTopBar
import com.osisupermoses.food_ordering_app.ui.sign_up.components.EmailInput
import com.osisupermoses.food_ordering_app.ui.sign_up.components.PasswordInput
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    toForgottenPasswordScreen: () -> Unit,
    toMenuScreen: () -> Unit
) {
    val state = loginViewModel.state.value
    val email = loginViewModel.userEmail
    val password = loginViewModel.password
    val switchedState = loginViewModel.switchChanged
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = scaffoldState) {
        loginViewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    Scaffold(
        topBar = {
            LoginTopBar(
                text = stringResource(R.string.login_for_quality_foods)
            )
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
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.txt_login_into_your_account),
                    style = MaterialTheme.typography.h3.copy(
                        color = Color(0xffDD0A35)
                    ),
                    modifier = Modifier
                        .align(Start)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.txt_welcome_back),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .align(Start)
                        .padding(start = 2.dp)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                EmailInput(
                    modifier = Modifier.height(55.dp),
                    email = email.value,
                    placeholderText = stringResource(R.string.txt_email_placeholder)
                ) { loginViewModel.onEvent(LoginEvent.EnteredEmail(it)) }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                PasswordInput(
                    modifier = Modifier.height(55.dp),
                    value = password.value,
                    placeholderText = stringResource(R.string.txt_password_placeholder)
                ) {
                    loginViewModel.onEvent(LoginEvent.EnteredPassword(it))
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = switchedState.value,
                            onCheckedChange = {
                                loginViewModel.onEvent(
                                    LoginEvent.OnSwitchChanged(
                                        it
                                    )
                                )
                            },
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                        Text(
                            text = stringResource(R.string.txt_remind_me),
                            style = MaterialTheme.typography.body2,
                        )
                    }
                    Text(
                        text = stringResource(R.string.txt_forgot_password),
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        modifier = Modifier.clickable {
                            toForgottenPasswordScreen.invoke()
                        }
                    )
                }
                Button(
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.spacing.medium,
                        horizontal = 30.dp
                    ),
                    text = stringResource(R.string.btn_login)
                ) {
                    loginViewModel.onLoginBtnClick(toMenuScreen)
//                    toMenuScreen.invoke() // for testing
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.spacing.extraLarge
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Divider(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Text(
                        text = stringResource(R.string.txt_login_with),
                        style = MaterialTheme.typography.body2,
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                    Divider(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Google
                    val token = stringResource(R.string.default_web_client_id)
                    val launcher =
                        registerGoogleActivityResultLauncher(loginViewModel, toMenuScreen)
                    Card(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small)
                            .size(40.dp)
                            .clickable {
                                launcher.launch(
                                    loginViewModel.googleSignInClient(
                                        context,
                                        token
                                    ).signInIntent
                                )
                            },
                        shape = RoundedCornerShape(5.dp),
                        elevation = 3.dp
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_google_logo_foreground),
                            contentDescription = "Google Logo",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomCircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun registerGoogleActivityResultLauncher(
    viewModel: LoginViewModel,
    nextScreen: () -> Unit = {}
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    // Callback
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            viewModel.loginWithGoogle(account.idToken!!) {
                nextScreen.invoke()
                viewModel.onEvent(LoginEvent.HasLoggedInOrFailed())
            }
        } catch (e: ApiException) {
            viewModel.onEvent(LoginEvent.HasLoggedInOrFailed(e.message!!))
            // Google Sign In failed, update UI appropriately
            Log.w("GoogleSignIn", "Google sign in failed", e)
        }
    }
}