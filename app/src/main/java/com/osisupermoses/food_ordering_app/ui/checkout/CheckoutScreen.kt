package com.osisupermoses.food_ordering_app.ui.checkout

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.checkout.add_payment_card.AddPaymentCardScreen
import com.osisupermoses.food_ordering_app.ui.checkout.components.*
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.ui_common.SectionHeader
import com.osisupermoses.food_ordering_app.util.dialog.awesome_custom_dalog.AwesomeCustomDialog
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator
import com.osisupermoses.food_ordering_app.util.parseNumberToCurrencyFormat
import com.osisupermoses.food_ordering_app.util.toasty
import com.osisupermoses.trustsoft_fintech_compose.util.ui_utils.dialogs.awesome_custom_dalog.AwesomeCustomDialogType
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun CheckoutScreen(
    viewModel: CheckoutViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    goToMenuScreen: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    BottomSheetScaffold(
        topBar = {
            CheckoutHeader(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.medium)
                    .fillMaxWidth()
            ) {
                navigateUp.invoke()
            }
        },
        scaffoldState = scaffoldState,
        sheetContent = {
            Box(
                modifier = Modifier.wrapContentHeight()
            ) {
                AddPaymentCardScreen(viewModel) {
                    viewModel.onClickSaveCard {
                        coroutineScope.launch {
                            if (sheetState.isExpanded) {
                                sheetState.collapse()
                            }
                            else {
                                sheetState.expand()
                            }
                        }
                        toasty(context, context.getString(R.string.entries_saved))
                    }
                }
            }
        },
        drawerScrimColor = Color.Red,
        sheetElevation = 20.dp,
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 30.dp
        )
    ) { paddingValues ->
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
                        enabled = viewModel.enabled,
                        readOnly = viewModel.readOnly,
                        topText = stringResource(viewModel.topText),
                        onAddressValueChange = { viewModel.address = it },
                        saveBtnVisibility = viewModel.saveBtnVisibility,
                        addTextTile = viewModel.saveBtnVisibility,
                        onEditAddressClick = { addressTextField ->
                            viewModel.onEditAddressClick(addressTextField)
                        },
                        onSaveClick = { addressTextField ->
//                            keyboardController?.hide()
                            viewModel.onSaveAddressClick(addressTextField)
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
                        viewModel.cardList.let {
                            itemsIndexed(it.toList()) { index, card ->
                                CardPlaceHolderItem(
                                    lastFourDigits = card.cardNumber.takeLast(4),
                                    painter =
                                    if (card.cardNumber.startsWith("4")) R.drawable.ic_visa_logo
                                    else if(card.cardNumber.startsWith("5")) R.drawable.mastercard_logo
                                    else R.drawable.verve,
                                    selected = viewModel.selectedCardHolderIndex == index,
                                    onClickCard = {
                                        viewModel.selectedCardHolderIndex = index
                                        viewModel.onPickCard(card)
                                    },
                                    onDeleteClick = {
                                        card.id?.let { it1 -> viewModel.onDeleteCard(it1) }
                                    }
                                ) {
                                    viewModel.onPickCard(card)
                                    coroutineScope.launch {
                                        if (sheetState.isExpanded) {
                                            sheetState.collapse()
                                        } else {
                                            sheetState.expand()
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            }
                        }
                        item {
                            AddNewCard {
                                viewModel.onAddNewCard()
                                coroutineScope.launch {
                                    if (sheetState.isExpanded) {
                                        sheetState.collapse()
                                    }
                                    else {
                                        sheetState.expand()
                                    }
                                }
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
                        itemPrice = viewModel.currencySymbol + parseNumberToCurrencyFormat(viewModel.itemPrice.toDouble()),
                        deliveryFee = viewModel.currencySymbol + parseNumberToCurrencyFormat(viewModel.deliveryFee.toDouble()),
                        discount = "${viewModel.currencySymbol}${parseNumberToCurrencyFormat(viewModel.discount)}",
                        totalAmount = "${viewModel.currencySymbol}${parseNumberToCurrencyFormat(viewModel.total)}"
                    )
                }
                item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.large)) }
                item {
                    CheckoutButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        totalAmt = "${viewModel.currencySymbol}${parseNumberToCurrencyFormat(viewModel.total)}"
                    ) {
                        if (
                            viewModel.selectedCardHolderIndex != null &&
                            viewModel.address.text.isNotBlank()
                        ) {
                            viewModel.onCheckoutClick(
                                context = context,
                                successScreen = {
                                    viewModel.successDialogIsVisible = true
                                },
                                failedScreen = {
                                    viewModel.errorDialogIsVisible = true
                                }
                            )
                        } else if (viewModel.address.text.isBlank()) {
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
            if (viewModel.successDialogIsVisible) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AwesomeCustomDialog(
                        type = AwesomeCustomDialogType.SUCCESS,
                        title = stringResource(R.string.payment_successful),
                        desc = "TRANSACTION REFERENCE: " + viewModel.transactionReference,
                        onOkayClick = {
                            goToMenuScreen.invoke()
                        }
                    )
                }
            }
            if (viewModel.errorDialogIsVisible) {
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
                            viewModel.errorDialogIsVisible = false
                            goToMenuScreen.invoke()
                        },
                        onRetry = {
                            viewModel.onCheckoutClick(context = context)
                            viewModel.errorDialogIsVisible = false
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
        }
    }
}