package com.osisupermoses.food_ordering_app.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.osisupermoses.food_ordering_app.R

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(
    welcomeViewModel: OnBoardingViewModel = hiltViewModel(),
    nextScreen: () -> Unit = {}
) {

    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffDD0A35).copy(alpha = 0.05f))
    ) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onBoardingPage = pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .align(CenterHorizontally)
                .weight(1f)
                .offset(y = (-35).dp),
            spacing = 8.dp,
            pagerState = pagerState,
            indicatorShape = RoundedCornerShape(2.dp),
            indicatorHeight = 5.dp,
            indicatorWidth = 17.79.dp,
            activeColor = Color(0xffDD0A35),
            inactiveColor = Color(0xffC4C4C4)
        )
        FinishButton(
            modifier = Modifier.weight(1.3f),
            pagerState = pagerState
        ) {
            welcomeViewModel.saveOnBoardingState(completed = true)
            nextScreen.invoke()
        }
        Row(
            modifier = Modifier.align(CenterHorizontally),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(R.string.powered_by_),
                style = MaterialTheme.typography.overline.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff042E46)
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun PagerScreen(onBoardingPage: OnBoardingPage) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
        ) {
            onBoardingPage.icon?.let { painterResource(it) }?.let {
                Image(
                    painter = it,
                    contentDescription = "Logo",
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .padding(
                        top = 20.dp,
                        bottom = 39.dp
                    )
                )
            }
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f),
                painter = painterResource(onBoardingPage.image),
                contentDescription = "Image"
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .padding(top = 20.dp),
                text = stringResource(onBoardingPage.description),
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }

}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 25.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .height(57.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xff042E46),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onClick.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.proceed),
                    style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}