package com.osisupermoses.food_ordering_app.ui.checkout.add_payment_card.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.paystack.android.model.Card
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.CardType
import com.osisupermoses.food_ordering_app.ui.theme.*

@ExperimentalAnimationApi
@Composable
fun PaymentCard(
    nameText: TextFieldValue,
    cardNumber: TextFieldValue,
    expiryNumber: TextFieldValue,
    cvcNumber: TextFieldValue
) {
    var backVisible by remember { mutableStateOf(false) }
    var cardType by remember { mutableStateOf(CardType.None) }
    val isVerveCard =
        cardNumber.text.startsWith("1") ||
                cardNumber.text.startsWith("2") ||
                cardNumber.text.startsWith("3") ||
                cardNumber.text.startsWith("6") ||
                cardNumber.text.startsWith("7") ||
                cardNumber.text.startsWith("8") ||
                cardNumber.text.startsWith("9") ||
                cardNumber.text.startsWith("0")

    // Creating a Vertical Gradient Color For Mastercard colours
    val masterCardGradient =
        Brush.verticalGradient(
            0f to masterCardRed,
            0.5f to masterCardYellow,
            1f to masterCardOrange
        )

    if (cvcNumber.text.length == 1 && !backVisible) {
        backVisible = true
    } else if (cvcNumber.text.length == 2) {
        backVisible = true
    } else if (cvcNumber.text.length == 3) {
        backVisible = false
    }

    //Todo: handle card type logic.
    cardType = if (cardNumber.text.startsWith("4")) {
        CardType.Visa
    } else if (cardNumber.text.startsWith("5")) {
        CardType.MasterCard
    } else if (isVerveCard) {
        CardType.Verve
    } else CardType.None

    // Setting textfield text length with card types
    val length =
        if (cardType == CardType.MasterCard || cardType == CardType.Visa) 16
        else 18

    val initial =
        if (cardType == CardType.MasterCard || cardType == CardType.Visa)
            remember { "*****************" }
                    .replaceRange(0..length, cardNumber.text.take(length))
        else
            remember { "*******************" }
                .replaceRange(0..length, cardNumber.text.take(length))

    // Displaying logo and color according to card type
    val animatedColor = animateColorAsState(
        targetValue =
        if (cardType == CardType.Visa) {
            visaCardColor
        } else if (cardType == CardType.MasterCard) {
            masterCardYellow
        } else if (cardType == CardType.Verve) {
            ErrorColor
        } else {
            MaterialTheme.colors.onBackground
        }
    )
    Box {
        Surface(
            modifier = Modifier.padding(16.dp).fillMaxWidth().height(220.dp)
                .graphicsLayer(
                    rotationY = animateFloatAsState(if (backVisible) 180f else 0f).value,
                    translationY = 0f
                ).clickable {
                    backVisible = !backVisible
                },
            shape = RoundedCornerShape(25.dp),
            color = animatedColor.value,
            elevation = 18.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = !backVisible) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val (symbol, logo, cardName, cardNameLabel, number, expiry, expiryLabel) = createRefs()

                        Image(
                            painter = painterResource(
                                id = R.drawable.card_symbol
                            ),
                            contentDescription = "symbol",
                            modifier = Modifier.padding(20.dp).constrainAs(symbol) {
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                            }
                        )

                        AnimatedVisibility(visible = cardType != CardType.None,
                            modifier = Modifier.padding(20.dp).constrainAs(logo) {
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)
                            }) {
                            Image(
                                painter = painterResource(
                                    id = cardType.image
                                ),
                                contentDescription = "symbol",
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Text(
                            text = initial.chunked(4).joinToString(" "),
                            style = MaterialTheme.typography.h5,
                            maxLines = 1,
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(spring())
                                .padding(vertical = 16.dp, horizontal = 16.dp)
                                .constrainAs(number) {
                                    linkTo(
                                        start = parent.start,
                                        end = parent.end
                                    )
                                    linkTo(
                                        top = parent.top,
                                        bottom = parent.bottom
                                    )
                                }
                        )

                        Text(
                            text = "CARD HOLDER",
                            style = MaterialTheme.typography.caption,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .constrainAs(cardNameLabel) {
                                    start.linkTo(parent.start)
                                    bottom.linkTo(cardName.top)
                                }
                        )
                        Text(
                            text = nameText.text,
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(TweenSpec(300))
                                .padding(start = 16.dp, bottom = 16.dp)
                                .constrainAs(cardName) {
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                }
                        )

                        Text(
                            text = "Expiry",
                            style = MaterialTheme.typography.caption,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .constrainAs(expiryLabel) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(expiry.top)
                                }
                        )
                        Text(
                            text = expiryNumber.text.take(4).chunked(2).joinToString(" / "),
                            style = MaterialTheme.typography.h6,
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(TweenSpec(300))
                                .padding(end = 16.dp, bottom = 16.dp)
                                .constrainAs(expiry) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }
                }

                AnimatedVisibility(visible = backVisible) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val (backScanner) = createRefs()
                        Spacer(
                            modifier = Modifier.height(50.dp).background(
                                Color.Black
                            ).fillMaxWidth().constrainAs(backScanner) {
                                linkTo(
                                    top = parent.top,
                                    bottom = parent.bottom
                                )
                            }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = backVisible,
            modifier = Modifier
                .padding(end = 50.dp, bottom = 50.dp)
                .align(Alignment.BottomEnd)
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 60.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cvcNumber.text,
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 4.dp, horizontal = 16.dp)

                )
            }

        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewPaymentCard(){
    PaymentCard(
        TextFieldValue("Abiodun"),
        TextFieldValue("*****************"),
        TextFieldValue("0224"),
        TextFieldValue("123")
    )
}