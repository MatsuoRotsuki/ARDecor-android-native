package com.soictnative.ardecor.presentation.screens.save_placement.save_current

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.presentation.common.AppIcon
import com.soictnative.ardecor.presentation.common.Dimens
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.util.ScreenState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedPlacementByUserRoute(
    modifier: Modifier = Modifier,
    placementState: ScreenState<List<SavedPlacement>>?,
    onClickInitialize: (SavedPlacement) -> Unit,
    onClickStartAR: () -> Unit,
    onClickCreateIdea: (SavedPlacement) -> Unit,
    onClickDelete: (SavedPlacement) -> Unit,
    onError: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullToRefreshState)
    ) {
        when (placementState) {
            is ScreenState.Loading ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(min = 200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            is ScreenState.Error ->
                LaunchedEffect(key1 = placementState) {
                    onError(placementState.message)
                }
            is ScreenState.Success ->
                SavedPlacementByUserSuccess(
                    onClickCreateIdea = onClickCreateIdea,
                    onClickInitialize = onClickInitialize,
                    onClickDelete = onClickDelete,
                    savedPlacements = placementState.uiData,
                    onClickStartAR = onClickStartAR
                )
            else -> Unit
        }
        PullRefreshIndicator(isRefreshing, pullToRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun SavedPlacementByUserSuccess(
    onClickInitialize: (SavedPlacement) -> Unit,
    onClickCreateIdea: (SavedPlacement) -> Unit,
    onClickDelete: (SavedPlacement) -> Unit,
    savedPlacements: List<SavedPlacement>?,
    onClickStartAR: () -> Unit,
) {
    val collapsedState = remember(savedPlacements) { savedPlacements?.map { true }?.toMutableStateList() }
    if (savedPlacements?.isEmpty() == true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Không có dữ liệu, hãy lưu bố trí nội thất",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = MediumPadding1)
            )
            Button(onClick = onClickStartAR) {
                Text("Bắt đầu chế độ xem AR")
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            item {
                Spacer(Modifier.height(MediumPadding1))
                Text(
                    modifier = Modifier.padding(start = MediumPadding1),
                    text = "Danh sách các bố trí đã lưu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            savedPlacements?.forEachIndexed { index, dataItem ->
                val collapsed = collapsedState?.get(index)
                item(key = "header_$index") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = dataItem.name,
                                fontSize = 24.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = dataItem.created_at_human!!,
                                fontSize = 16.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(onClick = { collapsedState?.set(index, !collapsed!!) }) {
                            Icon(
                                Icons.Default.run {
                                    if (collapsed ?: true)
                                        KeyboardArrowDown
                                    else
                                        KeyboardArrowUp
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Divider()
                }
                if (!collapsed!!) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, start = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = { onClickDelete(dataItem) }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                                }
                                Text(
                                    text = "Xoá",
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = { onClickInitialize(dataItem) }) {
                                    AppIcon(resourceId = R.drawable.ic_ar_cube)
                                }
                                Text(
                                    text = "Xem trên AR",
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = { onClickCreateIdea(dataItem) }) {
                                    Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                                }
                                Text(
                                    text = "Tạo ý tưởng thiết kế",
                                )
                            }
                        }
                    }
                    items(items = dataItem.products!!, key = { it.id }) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, start = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .width(80.dp)
                                    .aspectRatio(1f),
                                model = product.image_url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 6.dp, end = 12.dp)
                                    .weight(1f),
                            ) {
                                Text(
                                    text = product.name,
                                    fontSize = 18.sp,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding))
                            }
                        }
                    }
                }
            }
        }
    }
}