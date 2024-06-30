package com.soictnative.ardecor.presentation.screens.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.screens.home.RecommendedShimmerEffect
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryDetailRoute(
    categoryState: ScreenState<Category>? = null,
    onProductClicked: (Product) -> Unit,
    onError: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                onRefresh()
                delay(1000L)
                isRefreshing = false
            }
        }
    )

    Box(modifier = Modifier
        .pullRefresh(pullToRefreshState)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            item(key = "categoryDetailHeader") {
                Spacer(modifier = Modifier.height(MediumPadding1))

                if (categoryState is ScreenState.Success) {
                    Text(
                        text = categoryState.uiData.name,
                        modifier = Modifier
                            .padding(horizontal = MediumPadding1),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(ExtraSmallPadding2))
            }

            item(key = "categoryProducts") {
                when(categoryState) {
                    is ScreenState.Loading -> {
                        RecommendedShimmerEffect()
                    }
                    is ScreenState.Success -> {
                        CategoryProductSuccess(
                            products = categoryState.uiData.products ?: emptyList(),
                            onProductClicked = onProductClicked
                        )
                    }
                    is ScreenState.Error -> {
                        LaunchedEffect(key1 = categoryState) {
                            onError(categoryState.message)
                        }
                        RecommendedShimmerEffect()
                    }
                    else -> Unit
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullToRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun CategoryProductSuccess(
    products: List<Product>,
    onProductClicked: (Product) -> Unit,
) {
    if (products.size > 0) {
        LazyVerticalGrid(
            userScrollEnabled = false,
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .padding(horizontal = MediumPadding1)
                .heightIn(max = (products.size * 200).dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = products,
                key = { it.id }
            ) { product ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(ExtraSmallPadding2)
                        .clickable {
                            onProductClicked(product)
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        model = product.image_url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        modifier = Modifier.padding(top = ExtraSmallPadding),
                        text = product.name,
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.padding(top = ExtraSmallPadding),
                        text = String.format("$%.2f", product.price),
                        fontSize = 18.sp,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "No data",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}