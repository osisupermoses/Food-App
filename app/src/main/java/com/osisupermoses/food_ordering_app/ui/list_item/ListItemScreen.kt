package com.osisupermoses.food_ordering_app.ui.list_item

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.list_item.components.ImagePreviewItem
import com.osisupermoses.food_ordering_app.ui.list_item.components.ListItemTopBar
import com.osisupermoses.food_ordering_app.ui.list_item.components.TextFieldRowItem
import com.osisupermoses.food_ordering_app.ui.ui_common.Button
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.util.dialog.awesome_custom_dalog.AwesomeCustomDialog
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator
import com.osisupermoses.trustsoft_fintech_compose.util.ui_utils.dialogs.awesome_custom_dalog.AwesomeCustomDialogType


@OptIn(ExperimentalPagerApi::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ListItemScreen(
    viewModel: ListItemViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    goToMenuScreen: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    var imageViewIsSelected by remember { mutableStateOf(false) }
    var imagePlaceHolder by remember { mutableStateOf(R.drawable.imageplaceholder) }
    val selectedImages = remember { mutableStateListOf<Any>() }
    val context = LocalContext.current

    val state = viewModel.state

    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            viewModel.updateSelectedImageList(listOfImages = it)
        }

    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    Scaffold(
        topBar = { ListItemTopBar(navigateUp = navigateUp) },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .border(
                                width = 2.dp,
                                color = Color.Gray
                            ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (viewModel.listOfSelectedImages.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            ) {
                                itemsIndexed(viewModel.listOfSelectedImages) { index, uri ->
                                    ImagePreviewItem(uri = uri,
                                        height = screenHeight * 0.5f,
                                        width = screenWidth * 0.6f,
                                        onClick = { viewModel.onItemRemove(index) }
                                    )
                                    Log.i("IMAGES", "URI: $uri")
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
                        }
//                PhotoPagerScreen(
//                    images = list,
//                    page = 3
//                )
                        Image(
                            painter = painterResource(id = R.drawable.ic_vector_add_photo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(8.dp)
                                .align(Alignment.BottomEnd)
                                .clickable {
                                    if (permissionState.hasPermission) {
                                        galleryLauncher.launch("image/*")
                                    } else
                                        permissionState.launchPermissionRequest()
                                }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge)) }
                item {
                    TextFieldRowItem(
                        value = viewModel.businessName,
                        text = stringResource(R.string.enter_your_business_name_here),
                        placeholder = stringResource(R.string.restaurant_name)
                    ) {
                        viewModel.businessName = it
                    }
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    TextFieldRowItem(
                        value = viewModel.productTitle,
                        text = stringResource(R.string.enter_a_unique),
                        placeholder = stringResource(R.string.product_nam)
                    ) {
                        viewModel.productTitle = it
                    }
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    TextFieldRowItem(
                        value = viewModel.productPrice,
                        text = stringResource(R.string.enter_your_selling_price_here),
                        placeholder = stringResource(R.string.price),
                        keyboardType = KeyboardType.Decimal
                    ) {
                        viewModel.productPrice = it
                    }
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    TextFieldRowItem(
                        value = viewModel.productDescription,
                        text = stringResource(R.string.give_a_concise_description),
                        placeholder = stringResource(R.string.description)
                    ) {
                        viewModel.productDescription = it
                    }
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    TextFieldRowItem(
                        value = viewModel.productQuantity,
                        text = stringResource(R.string.quantity_in_stock),
                        placeholder = stringResource(R.string.items_in_stock)
                    ) {
                        viewModel.productQuantity = it
                    }
                }
                item {
                    Button(
                        modifier = Modifier
                            .padding(30.dp)
                            .align(Alignment.BottomCenter),
                        text = stringResource(id = R.string.submit_listing),
                        enabled = viewModel.listOfSelectedImages.isNotEmpty()
                    ) {
                        viewModel.onSubmitListingClick {
                            viewModel.successDialogIsVisible = true
                        }
                    }
                }
            }
        }
        if (viewModel.successDialogIsVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AwesomeCustomDialog(
                    type = AwesomeCustomDialogType.SUCCESS,
                    title = stringResource(R.string.successful_listing),
                    desc = stringResource(R.string.item_was_successfully),
                    onOkayClick = {
                        goToMenuScreen.invoke()
                    }
                )
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