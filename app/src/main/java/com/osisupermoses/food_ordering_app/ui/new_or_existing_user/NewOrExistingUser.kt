package com.osisupermoses.food_ordering_app.ui.new_or_existing_user

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.ui_common.Button

@Composable
fun NewOrExistingScreen(
    toLogInScreen: () -> Unit,
    toSignUpScreen: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    var radioBtnOneIsSelected by remember { mutableStateOf(false) }
    var radioBtnTwoIsSelected by remember { mutableStateOf(false) }
    val activity = (context as? Activity)
    val pressedTime = remember { mutableStateOf<Long>(0) }

    // Prevents MainScreen from going back to Login screen on backpress
    BackHandler {
        if (pressedTime.value + 2000 > System.currentTimeMillis()) {
            activity?.finish()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.please_press_back),
                Toast.LENGTH_SHORT
            ).show()
        }
        pressedTime.value = System.currentTimeMillis()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color(0xffFFC300).copy(0.1f)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 56.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.let_us_get_you),
                    style = MaterialTheme.typography.h1.copy(
                        color = Color(0xffDD0A35),
                        lineHeight = 65.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.large)
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(R.string.txt_new_or_existing_user),
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                )
                Row(
                    modifier = Modifier.padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = radioBtnOneIsSelected,
                        onClick = {
                            radioBtnOneIsSelected = true
                            radioBtnTwoIsSelected = false
                        },
                        colors =
                        RadioButtonDefaults.colors(
                            selectedColor = Color.DarkGray
                        )
                    )
                    Text(
                        text = stringResource(R.string.txt_new_user),
                        style = MaterialTheme.typography.h4
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = radioBtnTwoIsSelected,
                        onClick = {
                            radioBtnTwoIsSelected = true
                            radioBtnOneIsSelected = false
                        },
                        colors =
                        RadioButtonDefaults.colors(
                            selectedColor = Color.DarkGray
                        )
                    )
                    Text(
                        text = stringResource(R.string.txt_existing_user),
                        style = MaterialTheme.typography.h4
                    )

                }
            }
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(30.dp),
                text = stringResource(R.string.btn_continue)
            ) {
                when {
                    radioBtnOneIsSelected -> toSignUpScreen.invoke()
                    radioBtnTwoIsSelected -> toLogInScreen.invoke()
                    else -> Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}