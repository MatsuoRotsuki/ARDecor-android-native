package com.soictnative.ardecor.presentation.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.soictnative.ardecor.data.dto.Category
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllCategoryRoute(
    categoryState: ScreenState<List<Category>>?,
    onCategoryClicked: (Category) -> Unit,
    onError: (String) -> Unit,
    onRefresh: () -> Unit,
    onClickBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                onRefresh
                delay(1000L)
                isRefreshing = false
            }
        }
    )
    Box(modifier = Modifier.pullRefresh(pullToRefreshState)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClickBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = SmallPadding)
            ) {
                when(categoryState) {
                    is ScreenState.Loading -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    is ScreenState.Success -> {
                        item {
                            Spacer(modifier = Modifier.height(MediumPadding1))
                            Text(
                                text = "Danh mục",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items (items = categoryState.uiData, key = { it.id }) { category ->
                            CategoryCard(
                                category = category,
                                onCategoryClicked = onCategoryClicked
                            )
                        }
                    }
                    is ScreenState.Error -> {
                        item {
                            LaunchedEffect(key1 = categoryState) {
                                onError(categoryState.message)
                            }
                        }
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
fun CategoryCard(
    category: Category,
    onCategoryClicked: (Category) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clickable {
                onCategoryClicked(category)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(90.dp)
                    .width(120.dp),
                model = ImageRequest.Builder(context).data(category.image_url).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 6.dp),
                text = category.name,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
    Divider()
}


