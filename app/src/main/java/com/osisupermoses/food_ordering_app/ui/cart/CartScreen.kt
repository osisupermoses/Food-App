package com.osisupermoses.food_ordering_app.ui.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.cart.components.CartHolderItem
import com.osisupermoses.food_ordering_app.ui.cart.components.CheckoutComposable
import com.osisupermoses.food_ordering_app.ui.list_item.components.ListItemTopBar
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator
import com.osisupermoses.food_ordering_app.util.parseNumberToCurrencyFormat

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    toItemDetailScreen: (String) -> Unit,
    toCheckoutScreen: (String, String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    Scaffold(
        topBar = {
            ListItemTopBar(
                title = stringResource(id = R.string.shopping_cart),
                navigateUp = navigateUp
            )
         },
        scaffoldState = scaffoldState,
        backgroundColor = Color.White
    ) { paddingValues ->
        if (viewModel.cartItemList.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_item_added),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.LightGray,
                    ),
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .fillMaxWidth()
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                LazyColumn {
                    viewModel.cartItemList?.let { listOfCartItems ->
                        itemsIndexed(listOfCartItems.toList()) { index, cartItem ->
                            CartHolderItem(
                                context = context,
                                image = viewModel.getCartItemImageUriToString(cartItem.image.toUri()),
                                cartItemTitle = cartItem.title,
                                cartItemAmount = parseNumberToCurrencyFormat(cartItem.cartPrice.toDouble()),
                                cartItemQuantity = cartItem.cart_quantity,
                                onRemoveQuantityClick = {
                                    viewModel.onRemoveQuantityClick(cartItem)
                                },
                                onAddQuantityClick = { viewModel.onAddQuantityClick(cartItem) },
                                onDeleteClick = { viewModel.onDeleteClick(cartItem.id.toString()) },
                                onItemClick = {
                                    viewModel.onItemClick(index) {
                                        toItemDetailScreen.invoke(cartItem.product_id)
                                    }
                                }
                            )
                        }
                    }
                }
                CheckoutComposable(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    subTotal = parseNumberToCurrencyFormat(viewModel.subTotalAmount().toDouble()),
                    shippingCharges = parseNumberToCurrencyFormat(viewModel.deliveryCharges.toDouble()),
                    totalAmount = parseNumberToCurrencyFormat(viewModel.totalAmount().toDouble()),
                    btnEnabled = viewModel.cartItemList!!.isNotEmpty()
                ) {
                    toCheckoutScreen.invoke(viewModel.subTotalAmount(), viewModel.deliveryCharges)
                }
            }
        }
        if (viewModel.state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomCircularProgressIndicator()
            }
        }
    }
}

