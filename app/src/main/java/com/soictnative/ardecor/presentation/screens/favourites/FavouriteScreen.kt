package com.soictnative.ardecor.presentation.screens.favourites

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
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
import com.soictnative.ardecor.data.entity.FavouriteProductEntity
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteRoute(
    favouriteProductState: ScreenState<List<FavouriteProductEntity>>?,
    onClickProduct: (Int) -> Unit,
    onClickDelete: (FavouriteProductEntity) -> Unit,
    onRefresh: () -> Unit,
    onClickBack: () -> Unit,
    onError: (String) -> Unit,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            item {
                Spacer(Modifier.height(MediumPadding1))

                Text(
                    text = "Sản phẩm yêu thích",
                    modifier = Modifier
                        .padding(horizontal = MediumPadding1),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(ExtraSmallPadding2))
            }

            when (favouriteProductState) {
                is ScreenState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                is ScreenState.Error -> {
                    item {
                        LaunchedEffect(key1 = favouriteProductState) {
                            onError(favouriteProductState.message)
                        }
                    }
                }
                is ScreenState.Success -> {
                    if (favouriteProductState.uiData.size > 0) {
                        items(items = favouriteProductState.uiData, key = { it.id }) { product ->
                            Row(Modifier.fillMaxWidth()
                                .padding(vertical = ExtraSmallPadding2)) {
                                AsyncImage(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .aspectRatio(1f),
                                    model = product.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                                Column() {
                                    Text(
                                        modifier = Modifier
                                            .padding(start = MediumPadding1, top = ExtraSmallPadding),
                                        text = product.name,
                                        fontSize = 16.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = MediumPadding1, top = MediumPadding1),
                                        text = String.format("$%.2f", product.price),
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row() {
                                        IconButton(
                                            modifier = Modifier.padding(MediumPadding1),
                                            onClick = { onClickProduct(product.id) }) {
                                            Icon(
                                                Icons.Default.Visibility,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        IconButton(
                                            modifier = Modifier.padding(MediumPadding1),
                                            onClick = { onClickDelete(product) }) {
                                            Icon(
                                                Icons.Outlined.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .statusBarsPadding(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                androidx.compose.material3.Text(
                                    text = "Không có sản phẩm nào",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = MediumPadding1)
                                )
                                Button(onClick = onClickBack) {
                                    androidx.compose.material3.Text("Quay trở lại")
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        }

        PullRefreshIndicator(refreshing = isRefreshing, state = pullToRefreshState, Modifier.align(Alignment.TopCenter))
    }
}