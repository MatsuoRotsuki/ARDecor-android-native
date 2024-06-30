package com.soictnative.ardecor.presentation.screens.save_placement.save_override

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavePlacementOverrideRoute(
    modifier: Modifier = Modifier,
    placementState: ScreenState<List<SavedPlacement>>?,
    selectedPlacement: SavedPlacement?,
    onOptionSelected: (SavedPlacement?) -> Unit,
    onConfirm: () -> Unit,
    onError: (String) -> Unit,
    onRefresh: () -> Unit,
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
                SavedPlacementOverrideSuccess(
                    selectedPlacement = selectedPlacement,
                    onOptionSelected = onOptionSelected,
                    savedPlacements = placementState.uiData
                )
            else -> Unit
        }
        PullRefreshIndicator(isRefreshing, pullToRefreshState, Modifier.align(Alignment.TopCenter))
        if (selectedPlacement != null) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp),
                onClick = onConfirm) {
                Text(text = "Xác nhận")
            }
        }
    }
}

@Composable
fun SavedPlacementOverrideSuccess(
    selectedPlacement: SavedPlacement?,
    onOptionSelected: (SavedPlacement?) -> Unit,
    savedPlacements: List<SavedPlacement>?,
) {
    val context = LocalContext.current
    val collapsedState = remember(savedPlacements) { savedPlacements?.map { true }?.toMutableStateList() }
    if (savedPlacements?.isEmpty() == true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "No data",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .statusBarsPadding()
        ) {
            item {
                Spacer(modifier = Modifier.height(MediumPadding1))
                Text(
                    modifier = Modifier
                        .padding(start = MediumPadding1),
                    text = "Danh sách bố trí đã lưu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
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
                            .selectable(
                                selected = (dataItem == selectedPlacement),
                                onClick = { onOptionSelected(dataItem) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = dataItem.name,
                                fontSize = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = dataItem.created_at_human!!,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        RadioButton(
                            selected = (selectedPlacement == dataItem),
                            onClick = null
                        )
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
                        }                }
                    Divider()
                }
                if (!collapsed!!) {
                    items(items = dataItem.products ?: emptyList(), key = { it.id }) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, start = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(1f),
                                model = ImageRequest.Builder(context).data(product.image_url).build(),
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
                                Spacer(modifier = Modifier.height(ExtraSmallPadding))
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun UpdatePlacementNameDialog(
) {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(
                text = "Tên bố trí",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            TextField(value = "abc", onValueChange = {})
        },
        confirmButton = {
            Button(
                onClick = { /*TODO*/ }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Cancel")
            }
        }
    )
}