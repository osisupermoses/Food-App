package com.osisupermoses.food_ordering_app.ui.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.getCards
import com.osisupermoses.food_ordering_app.ui.checkout.add_payment_card.AddPaymentCardScreen
import com.osisupermoses.food_ordering_app.ui.checkout.components.*
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.ui_common.SectionHeader
import com.osisupermoses.food_ordering_app.util.dialog.awesome_custom_dalog.AwesomeCustomDialog
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator
import com.osisupermoses.food_ordering_app.util.toasty
import com.osisupermoses.trustsoft_fintech_compose.util.ui_utils.dialogs.awesome_custom_dalog.AwesomeCustomDialogType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    toPaymentCard: (Int) -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    var saveBtnVisibility by remember { mutableStateOf(true) }
    var enabled by remember { mutableStateOf(true) }
    var readOnly by remember { mutableStateOf(false) }
    var topText by remember { mutableStateOf(R.string.input_a_valid_address_below) }
    var successDialogIsVisible by remember { mutableStateOf(false) }
    var errorDialogIsVisible by remember { mutableStateOf(false) }
    var paymentCardIsVisible by remember { mutableStateOf(false) }
    var checkoutPageIsVisible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }


        Scaffold(
            topBar = {
                CheckoutHeader(
                    modifier = Modifier
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                ) {
                    navigateUp.invoke()
                }
            },
            scaffoldState = scaffoldState
        ) { paddingValues ->
            AnimatedVisibility(visible = checkoutPageIsVisible) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    LazyColumn {
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.large)) }
                        item { SectionHeader(sectionTitle = stringResource(id = R.string.address)) }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }
                        item {
                            Address(
                                modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                                    .fillMaxWidth(),
                                address = viewModel.address,
                                enabled = enabled,
                                readOnly = readOnly,
                                topText = stringResource(topText),
                                onAddressValueChange = { viewModel.address = it },
                                saveBtnVisibility = saveBtnVisibility,
                                addTextTile = saveBtnVisibility,
                                onEditAddressClick = {
                                    topText = R.string.you_can_edit
                                    readOnly = false
                                    enabled = true
                                    viewModel.address = it
                                    saveBtnVisibility = true
                                },
                                onSaveClick = {
                                    if (it.isNotBlank()) {
                                        toasty(context, context.getString(R.string.address_saved))
                                        saveBtnVisibility = false
                                        readOnly = true
                                        enabled = false
                                        viewModel.address = it
                                    } else toasty(context, context.getString(R.string.please_input_an_address))
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
                        item {
                            Divider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.large)) }
                        item { SectionHeader(sectionTitle = stringResource(id = R.string.payment_method)) }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }
                        item {
                            LazyRow {
                                item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium)) }
                                viewModel.state.value.cardList?.let {
                                    itemsIndexed(it.toList()) { index, cardholder ->
                                        CardHolderItem(
                                            lastFourDigits = cardholder.cardLast4digits,
                                            painter = cardholder.cardIssuerIcon,
                                            selected = viewModel.selectedCardHolderIndex == index,
                                            onClickCard = {
                                                viewModel.selectedCardHolderIndex = index
                                            }
                                        ) {
                                            paymentCardIsVisible = true
                                            checkoutPageIsVisible = false
                                        }
                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                    }
                                }
                                item {
                                    AddNewCard {
                                        paymentCardIsVisible = true
                                        checkoutPageIsVisible = false
                                    }
                                }
                                item { Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium)) }
                            }
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.large)) }
                        item {
                            PaymentDetails(
                                modifier = Modifier
                                    .padding(horizontal = MaterialTheme.spacing.medium)
                                    .fillMaxWidth(),
                                itemPrice = viewModel.currencySymbol + viewModel.itemPrice,
                                deliveryFee = viewModel.currencySymbol + viewModel.deliveryFee,
                                discount = "${viewModel.currencySymbol}${viewModel.discount}",
                                totalAmount = "${viewModel.currencySymbol}${viewModel.total}"
                            )
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.large)) }
                        item {
                            CheckoutButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp),
                                totalAmt = "${viewModel.currencySymbol}${viewModel.total}"
                            ) {
                                if (viewModel.selectedCardHolderIndex != null && viewModel.address.isNotBlank()) {
                                    viewModel.makePayment(
                                        context = context,
                                        successScreen = {
                                            successDialogIsVisible = true
                                        },
                                        failedScreen = {
                                            errorDialogIsVisible = true
                                        }
                                    )
                                } else if (viewModel.address.isBlank()) {
                                    toasty(
                                        context,
                                        context.getString(R.string.address_field_cannot_be_blank)
                                    )
                                } else toasty(
                                    context,
                                    context.getString(R.string.please_select_a_card)
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge)) }
                    }
                    if (successDialogIsVisible) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AwesomeCustomDialog(
                                type = AwesomeCustomDialogType.SUCCESS,
                                title = stringResource(R.string.payment_successful),
                                desc = "TRANSACTION REFERENCE:" + viewModel.state.value.transReference,
                                onDismiss = {
                                    successDialogIsVisible = false
                                }
                            )
                        }
                    }
                    if (errorDialogIsVisible) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AwesomeCustomDialog(
                                type = AwesomeCustomDialogType.ERROR,
                                title = stringResource(R.string.unsuccessful_payment),
                                desc = ("REASON: " + viewModel.state.value.error),
                                twoOptionsNeeded = true,
                                onDismiss = {
                                    errorDialogIsVisible = false
                                },
                                onRetry = {
                                    viewModel.makePayment(context = context)
                                    errorDialogIsVisible = false
                                }
                            )
                        }
                    }
                    if (viewModel.state.value.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomCircularProgressIndicator()
                        }
                    }
                    AnimatedVisibility(
                        visible = paymentCardIsVisible,
                        enter = EnterTransition.None,
                        exit = ExitTransition.None
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AddPaymentCardScreen(viewModel) {
                                checkoutPageIsVisible = true
                                paymentCardIsVisible = false
                            }
                        }
                    }
                }
            }
    }
}