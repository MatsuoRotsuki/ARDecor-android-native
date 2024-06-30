package com.soictnative.ardecor.presentation.screens.home
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.data.entity.RecentlyViewedProductEntity
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.IconSize
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
fun HomeRoute(
    productsState: ScreenState<List<Product>>?,
    recentlyViewedProductState: ScreenState<List<RecentlyViewedProductEntity>>?,
    categoriesState: ScreenState<List<Category>>?,
    onProductClicked: (Int) -> Unit,
    onCategoryClicked: (Category) -> Unit,
    onClickSearch: (() -> Unit)? = null,
    onError: (String) -> Unit,
    onRefresh: () -> Unit,
    //
    products: LazyPagingItems<Product>,
    //
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                onRefresh()
                delay(1000L)
                isRefreshing = false
            }
        }
    )

    Box(Modifier.pullRefresh(pullToRefreshState)) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) {
            item {
                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = "Khám phá",
                    modifier = Modifier
                        .padding(horizontal = MediumPadding1),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(ExtraSmallPadding2))

                val interactionSource = remember{
                    MutableInteractionSource()
                }
                val isClicked = interactionSource.collectIsPressedAsState().value
                LaunchedEffect(key1 = isClicked) {
                    if (isClicked) {
                        onClickSearch?.invoke()
                    }
                }
                Box(modifier = Modifier
                    .padding(horizontal = MediumPadding1)
                    .fillMaxWidth()
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                        value = "",
                        onValueChange = {
                        },
                        placeholder = {
                            Text(
                                text = "Search something...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorResource(id = R.color.placeholder)
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = MaterialTheme.shapes.extraLarge,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                modifier = Modifier.size(IconSize),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        interactionSource = interactionSource
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = "Danh mục",
                    modifier = Modifier.padding(start = MediumPadding1),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(ExtraSmallPadding2))

                //Categories
                when(categoriesState) {
                    is ScreenState.Loading -> {
                        CategoryShimmerEffect()
                    }
                    is ScreenState.Success -> {
                        CategorySuccess(
                            categories = categoriesState.uiData,
                            onCategoryClicked = onCategoryClicked
                        )
                    }
                    is ScreenState.Error -> {
                        LaunchedEffect(categoriesState) {
                            onError(categoriesState.message)
                        }
                        CategoryShimmerEffect()
                    }
                    else -> Unit
                }
            }

            item {
                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = "Sản phẩm nổi bật",
                    modifier = Modifier.padding(start = MediumPadding1),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(ExtraSmallPadding2))


//                when(productsState) {
//                    is ScreenState.Loading -> {
//                        RecommendedShimmerEffect()
//                    }
//                    is ScreenState.Success -> {
//                        RecommendedSuccess(
//                            products = productsState.uiData,
//                            onProductClicked = onProductClicked
//                        )
//                    }
//                    is ScreenState.Error -> {
//                        LaunchedEffect(key1 = productsState) {
//                            onError(productsState.message)
//                        }
//                        RecommendedShimmerEffect()
//                    }
//                    else -> Unit
//                }

                val handlePagingResult = handlePagingResult(products)

                if(handlePagingResult) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize=160.dp),
                        modifier = Modifier
                            .padding(horizontal = MediumPadding1)
                            .heightIn(max = 800.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(/*items = products,*/ key = { index -> products[index]?.id ?: index }, count = products.itemCount) { /*product*/ index ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(ExtraSmallPadding2)
                                    .clickable {
                                        onProductClicked(products[index]!!.id)
                                    }
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f),
                                    model = products[index]!!.image_url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )

                                Text(
                                    modifier = Modifier.padding(top = ExtraSmallPadding),
                                    text = products[index]!!.name,
                                    fontSize = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    modifier = Modifier.padding(top = ExtraSmallPadding),
                                    text = String.format("$%.2f", products[index]!!.price),
                                    fontSize = 18.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            item {
                //Recently View Products
                when(recentlyViewedProductState) {
                    is ScreenState.Loading -> {
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
                                onProductClicked = onProductClicked
                            )
                        }
                    }
                    is ScreenState.Error -> {
                        LaunchedEffect(recentlyViewedProductState) {
                            onError(recentlyViewedProductState.message)
                        }
                        ViewedRecentlyShimmerEffect()
                    }
                    else -> Unit
                }
            }
        }

        PullRefreshIndicator(isRefreshing, pullToRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun handlePagingResult(products: LazyPagingItems<Product>): Boolean {
    val loadState = products.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            RecommendedShimmerEffect()
            false
        }

        error != null -> {
            false
        }

        else -> {
            true
        }
    }
}

@Composable
fun CategoryShimmerEffect() {
    LazyRow(modifier = Modifier
        .padding(start = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun CategorySuccess(
    categories: List<Category>,
    onCategoryClicked: (Category) -> Unit
) {
    LazyRow(modifier = Modifier
        .padding(start = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = categories, key = { it.id }) { category ->
            Box(
                modifier = Modifier
                    .padding(ExtraSmallPadding)
                    .height(150.dp)
                    .width(200.dp)
                    .clickable {
                        onCategoryClicked(category)
                    }
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = category.image_url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontSize = 22.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun ViewedRecentlyShimmerEffect() {
    LazyRow(modifier = Modifier
        .padding(start = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(4) {
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .padding(
                        top = ExtraSmallPadding2,
                        start = ExtraSmallPadding2,
                        end = ExtraSmallPadding2,
                        bottom = SmallPadding
                    )
            ) {
                Box(modifier = Modifier
                    .aspectRatio(1f)
                    .shimmerEffect())

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun ViewRecentlySuccess(
    products : List<RecentlyViewedProductEntity>,
    onProductClicked: (Int) -> Unit
) {

    LazyRow(modifier = Modifier
        .padding(start = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = products, key = { it.id }) {product ->
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .padding(ExtraSmallPadding2)
                    .clickable {
                        onProductClicked(product.id)
                    }
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(160.dp)
                        .aspectRatio(1f),
                    model = product.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Text(
                    modifier = Modifier.padding(top = ExtraSmallPadding),
                    text = product.name,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = ExtraSmallPadding),
                    text = String.format("$%.2f", product.price),
                    fontSize = 18.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RecommendedShimmerEffect()
{
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize=160.dp),
        modifier = Modifier
            .heightIn(max = 1000.dp)
            .padding(horizontal = MediumPadding1),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(6) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ExtraSmallPadding2)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun RecommendedSuccess(
    products: List<Product>,
    onProductClicked: (Product) -> Unit
) {
    LazyVerticalGrid(
        userScrollEnabled = false,
        columns = GridCells.Adaptive(minSize=160.dp),
        modifier = Modifier
            .padding(horizontal = MediumPadding1)
            .heightIn(max = (products.size * 200).dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = products, key = { it.id }) { product ->
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
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(top = ExtraSmallPadding),
                    text = String.format("$%.2f", product.price),
                    fontSize = 18.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}