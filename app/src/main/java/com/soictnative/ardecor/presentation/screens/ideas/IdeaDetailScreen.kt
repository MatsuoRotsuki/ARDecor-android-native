package com.soictnative.ardecor.presentation.screens.ideas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.soictnative.ardecor.data.dto.Idea
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.presentation.common.AppIcon
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IdeaDetailRoute(
    ideaState: ScreenState<Idea>?,
    onClickInitialize: (Idea) -> Unit,
    onClickProduct: (Product) -> Unit,
    onClickBack: () -> Unit,
    onError: (String) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        val (nest, backBtn) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(nest) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .padding(horizontal = SmallPadding)
            ) {
                when(ideaState) {
                    is ScreenState.Loading -> {
                        item {
                            IdeaDetailLoading()
                        }
                    }
                    is ScreenState.Error -> {
                        item {
                            LaunchedEffect(key1 = ideaState) {
                                onError(ideaState.message)
                            }
                        }
                    }
                    is ScreenState.Success -> {
                        item {
                            AssistChip(
                                onClick = { /*TODO*/ },
                                label = { Text(ideaState.uiData.room_type?.name ?: "") })
                            Text(
                                text = ideaState.uiData.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = MediumPadding1)
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .aspectRatio(1f),
                                    model = "https://t4.ftcdn.net/jpg/05/49/98/39/240_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier
                                    .padding(start=ExtraSmallPadding2)) {
                                    Text(text = "Loc Pham", fontSize = 16.sp)
                                    ideaState.uiData.created_at_human?.let { Text(text = it, fontSize = 14.sp) }
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .padding(top = MediumPadding1, bottom = MediumPadding1),
                                text = ideaState.uiData.description ?: "",
                                fontSize = 16.sp
                            )
                        }
                        item {
                            val pagerState = rememberPagerState(pageCount = {
                                ideaState.uiData.images?.size ?: 0
                            })
                            HorizontalPager(state = pagerState) { page ->
                                AsyncImage(
                                    model = ideaState.uiData.images?.get(page)!!.image_url,
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

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(start = SmallPadding, top = ExtraSmallPadding2, bottom = ExtraSmallPadding2),
                                    text = "Sản phẩm có trong thiết kế",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                OutlinedButton(
                                    modifier = Modifier
                                        .weight(0.4f),
                                    onClick = { onClickInitialize(ideaState.uiData) },
                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                ) {
                                    AppIcon(resourceId = R.drawable.ic_ar_cube)
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text("Xem trên AR")
                                }
                            }
                        }

                        items(items = ideaState.uiData.products ?: emptyList(), key = { it.id }) { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = ExtraSmallPadding2, start = SmallPadding, bottom = ExtraSmallPadding2)
                                    .clickable {
                                        onClickProduct(product)
                                    },
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
                                        .padding(start = 10.dp, end = 12.dp)
                                        .weight(1f),
                                ) {
                                    Text(
                                        text = product.name,
                                        fontSize = 16.sp,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    Spacer(modifier = Modifier.height(ExtraSmallPadding))
                                }
                            }
                        }
                    } else -> Unit
                }
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
    }
}

@Composable
fun IdeaDetailLoading() {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}