package com.soictnative.ardecor.presentation.screens.product_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.presentation.common.AppIcon
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.presentation.screens.home.ViewRecentlySuccess
import com.soictnative.ardecor.presentation.screens.home.ViewedRecentlyShimmerEffect
import com.soictnative.ardecor.presentation.screens.home.shimmerEffect
import com.soictnative.ardecor.util.ScreenState

@Composable
fun ProductDetailsRoute(
    productState: ScreenState<Product>?,
    recentlyViewedProductState: ScreenState<List<RecentlyViewedProductEntity>>?,
    onClickVariation: (Int) -> Unit,
    onClickViewAR: (Product) -> Unit,
    onClickView3d: (Product) -> Unit,
    onClickAddToFavourite: (Product) -> Unit,
    onClickBack: () -> Unit,
    onError: (String) -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .padding(top = MediumPadding1)
    ) {
        val (backBtn, rightBtn, nest) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(nest) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            when(productState) {
                is ScreenState.Loading -> {
                    ProductDetailsLoading()
                }
                is ScreenState.Error -> {
                    LaunchedEffect(key1 = productState) {
                        onError(productState.message)
                    }
                }
                is ScreenState.Success -> {
                    ProductDetailsSuccess(
                        product = productState.uiData,
                        recentlyViewedProductState = recentlyViewedProductState,
                        onClickVariation = onClickVariation,
                        onClickViewAR = onClickViewAR,
                        onClickView3d = onClickView3d,
                        onError = onError
                    )
                }
                else -> Unit
            }
        }
        FilledIconButton(
            modifier = Modifier.constrainAs(backBtn) {
                top.linkTo(parent.top, margin = MediumPadding1)
                start.linkTo(parent.start, margin = SmallPadding)
            },
            onClick = onClickBack
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }

        if (productState is ScreenState.Success) {
            FilledIconButton(
                modifier = Modifier.constrainAs(rightBtn) {
                    top.linkTo(parent.top, margin = MediumPadding1)
                    end.linkTo(parent.end, margin = SmallPadding)
                },
                onClick = { onClickAddToFavourite(productState.uiData) }) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }
        }
    }
}

@Composable
fun ProductDetailsLoading() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(), contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) { iteration ->
                val color = Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(SmallPadding))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SmallPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(5) {
                Box(modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .shimmerEffect()
                )
            }
        }

        Spacer(modifier = Modifier.height(SmallPadding))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = SmallPadding)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(20.dp)
                .padding(start = MediumPadding1)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .padding(horizontal = SmallPadding)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(MediumPadding1))

        Box(
            modifier = Modifier
                .height(16.dp)
                .width(100.dp)
                .padding(horizontal = SmallPadding)
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(MediumPadding1))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsSuccess(
    product: Product,
    recentlyViewedProductState: ScreenState<List<RecentlyViewedProductEntity>>?,
    onClickVariation: (Int) -> Unit,
    onClickViewAR: (Product) -> Unit,
    onClickView3d: (Product) -> Unit,
    onError: (String) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        product.images?.size ?: 0
    })
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                modifier = Modifier.aspectRatio(1f)
            ) { page ->
                AsyncImage(
                    model = product.images!![page].image_url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(SmallPadding))
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = SmallPadding),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = product.product_variations ?: emptyList(), key = { it.id }) { variation ->
                    Box(modifier = Modifier.clickable {
                        onClickVariation(variation.id)
                    }
                    ) {
                        AsyncImage(
                            model = variation.image_url,
                            contentDescription = null,
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(ExtraSmallPadding2))
                                .then(
                                    if (product.id == variation.id) Modifier.border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(15.dp)
                                    ) else Modifier
                                )
                                .padding(ExtraSmallPadding2),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(SmallPadding))
        }
        
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MediumPadding1),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier.padding(ExtraSmallPadding2),
                    onClick = { onClickView3d(product) },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    AppIcon(resourceId = R.drawable.ic_cube3d, tint = MaterialTheme.colorScheme.surface)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("360 View")
                }
                Button(
                    modifier = Modifier.padding(ExtraSmallPadding2),
                    onClick = { onClickViewAR(product) },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    AppIcon(resourceId = R.drawable.ic_ar_cube, tint = MaterialTheme.colorScheme.surface)
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("AR View")
                }
            }
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = SmallPadding),
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = String.format("$%.2f", product.price),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Text(
                modifier = Modifier.padding(start = SmallPadding, end = 10.dp),
                text = product.description ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(MediumPadding1))
        }

        item {
            Text(
                modifier = Modifier.padding(horizontal = SmallPadding),
                text = "Thông số kỹ thuật",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            product.measurements?.forEach { measurement ->
                Row(
                    modifier = Modifier
                        .padding(vertical = ExtraSmallPadding2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = SmallPadding),
                        text = measurement.measurement_type!!.name,
                        fontSize = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(Modifier.weight(0.1f))

                    Text(
                        modifier = Modifier
                            .weight(0.4f),
                        text = "${measurement.value} ${measurement.measurement_type.unit}",
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Divider()
            }
        }
        item {
            when(recentlyViewedProductState) {
                is ScreenState.Loading -> {
                    ViewedRecentlyShimmerEffect()
                }
                is ScreenState.Error -> {
                    LaunchedEffect(recentlyViewedProductState) {
                        onError(recentlyViewedProductState.message)
                    }
                    ViewedRecentlyShimmerEffect()
                }
                is ScreenState.Success -> {
                    if (recentlyViewedProductState.uiData.size > 0) {
                        Spacer(modifier = Modifier.height(MediumPadding1))

                        Text(
                            text = "Đã xem gần đây",
                            modifier = Modifier.padding(start = MediumPadding1),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(ExtraSmallPadding2))

                        ViewRecentlySuccess(
                            products = recentlyViewedProductState.uiData,
                            onProductClicked = {
                                onClickVariation(it)
                            }
                        )
                    }
                }
                else -> Unit
            }
        }
    }
}