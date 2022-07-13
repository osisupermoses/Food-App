package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.osisupermoses.food_ordering_app.R

@ExperimentalPagerApi
@Composable
fun PhotoPager(
    modifier: Modifier = Modifier,
    images: List<Any>,
    page: Int,
    onContentChange: (String, String) -> Unit = {_,_->},
    onShortChange: (String, String) -> Unit = {_,_->}
) {
    val pagerState = rememberPagerState()

    HorizontalPager(count = images.size, state = pagerState) { page_ ->
        val image = images[page_]

        Box {
            AsyncImage(
                model = image,
                placeholder = rememberAsyncImagePainter(model = R.drawable.imageplaceholder),
                contentDescription = null,
                modifier = modifier.clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }

    LaunchedEffect(page) {
        pagerState.scrollToPage(page)
    }
}