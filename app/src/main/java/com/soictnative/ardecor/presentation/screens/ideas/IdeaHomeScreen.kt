package com.soictnative.ardecor.presentation.screens.ideas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.presentation.common.Dimens
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.screens.home.shimmerEffect
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdeaHomeRoute(
    roomState: ScreenState<List<RoomType>>?,
    ideaState: ScreenState<List<Idea>>?,
    onRoomTypeClicked: (RoomType) -> Unit,
    onIdeaClicked: (Idea) -> Unit,
    onError: (String) -> Unit,
    onRefresh: () -> Unit,
    onClickAdd: () -> Unit,
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

    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullToRefreshState)) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) {
            item {
                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = "Khám phá ý tưởng",
                    modifier = Modifier
                        .padding(horizontal = MediumPadding1),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                Spacer(modifier = Modifier.height(MediumPadding1))

                Text(
                    text = "Tìm kiếm theo kiểu phòng",
                    modifier = Modifier.padding(start = MediumPadding1),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding2))

                when(roomState) {
                    is ScreenState.Loading -> {
                        RoomTypeShimmerEffect()
                    }
                    is ScreenState.Success -> {
                        RoomTypeSuccess(
                            roomTypes = roomState.uiData,
                            onRoomTypeClicked = onRoomTypeClicked,
                        )
                    }
                    is ScreenState.Error -> {
                        LaunchedEffect(key1 = roomState) {
                            onError(roomState.message)
                        }
                    }
                    else -> Unit
                }
            }

            item {
                //IMAGE OF DESIGN
                when(ideaState) {
                    is ScreenState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is ScreenState.Error -> {
                        LaunchedEffect(key1 = ideaState) {
                            onError(ideaState.message)
                        }
                    }
                    is ScreenState.Success -> {
                        IdeasSuccess(
                            ideas = ideaState.uiData,
                            onIdeaClicked = onIdeaClicked
                        )
                    }
                    else -> Unit
                }
            }
        }

        PullRefreshIndicator(isRefreshing, pullToRefreshState, Modifier.align(Alignment.TopCenter))

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MediumPadding1),
            onClick = onClickAdd
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}

@Composable
fun RoomTypeShimmerEffect() {
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
fun RoomTypeSuccess(
    roomTypes: List<RoomType>,
    onRoomTypeClicked: (RoomType) -> Unit,
) {
    val context = LocalContext.current

    LazyRow(modifier = Modifier
        .padding(start = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = roomTypes, key = { it.id }) { roomType ->
            Box(
                modifier = Modifier
                    .padding(ExtraSmallPadding2)
                    .height(150.dp)
                    .width(200.dp)
                    .clickable {
                        onRoomTypeClicked(roomType)
                    }
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(context).data(roomType.image_url).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = roomType.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun IdeasSuccess(
    ideas: List<Idea>,
    onIdeaClicked: (Idea) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = (ideas.size * 400).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = ideas, key = { it.id }) { idea ->
            IdeaCard(idea = idea, onIdeaClicked = onIdeaClicked)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IdeaCard(
    idea: Idea,
    onIdeaClicked: (Idea) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        idea.images?.size ?: 0
    })
    Column(
        Modifier
            .clickable {
                onIdeaClicked(idea)
            }
    ) {
        Text(
            text = idea.name,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = MediumPadding1)
        )
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill
        ) { page ->
            AsyncImage(
                model = idea.images!![page].image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(min = 250.dp),
                contentScale = ContentScale.Crop
            )
        }

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
    }
}