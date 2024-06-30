package com.soictnative.ardecor.presentation.screens.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.User
import com.soictnative.ardecor.presentation.common.AppIcon
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileRoute(
    profileState: ScreenState<User>?,
//    onRefesh: () -> Unit,
    onClickUpdateProfile: () -> Unit,
    onClickSavedPlacements: () -> Unit,
    onClickFavouriteCollection: () -> Unit,
    onClickLogOut: () -> Unit,
    onError: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
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
                // Avatar
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight())
                {
                    when (profileState) {
                        is ScreenState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ScreenState.Error -> {
                            LaunchedEffect(key1 = profileState) {
                                onError(profileState.message)
                            }
                        }
                        is ScreenState.Success -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .aspectRatio(1f)
                                        .clip(CircleShape),
                                    model = profileState.uiData.photoUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop)
                                Spacer(modifier = Modifier.height(SmallPadding))
                                Text(
                                    text = profileState.uiData.displayName,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 1
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            ) {
                                IconButton(
                                    modifier = Modifier,
                                    onClick = onClickUpdateProfile
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        }
                        else -> Unit
                    }
                }
            }
            
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = SmallPadding, top = MediumPadding1)
                ) {
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                onClickSavedPlacements()
                            },
                        icon = { AppIcon(
                            resourceId = R.drawable.ic_ar_cube,
                            tint = MaterialTheme.colorScheme.onBackground
                        ) },
                        text = {
                            Text(
                                text = "Danh sách bố trí nội thất đã lưu",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        trailing = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Divider()
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                onClickFavouriteCollection()
                            },
                        icon = { AppIcon(
                            resourceId = R.drawable.ic_floor,
                            tint = MaterialTheme.colorScheme.onBackground)
                        },
                        text = {
                            Text(
                                text = "Bộ sưu tập",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        trailing = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Divider()
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                onClickLogOut()
                            },
                        icon = {
                            Icon(
                                Icons.Default.Output,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        text = {
                            Text(
                                text = "Đăng xuất",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        trailing = {
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Divider()
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