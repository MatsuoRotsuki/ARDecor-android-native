package com.soictnative.ardecor.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.soictnative.ardecor.R
import com.soictnative.ardecor.data.dto.Product
import com.soictnative.ardecor.presentation.common.Dimens
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding2
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRoute(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    productState: ScreenState<List<Product>>?,
    sortState: String?,
    minPriceState: Float?,
    maxPriceState: Float?,
    onSortChanged: (String?) -> Unit,
    onPriceRangeChosen: (Float?, Float?) -> Unit,
    onError: (String) -> Unit,
    onProductClicked: (Product) -> Unit,
    onClickBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = MediumPadding1),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)) {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClickBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    placeholder = {
                        Text(
                            text = "Nhập từ khoá tìm kiếm...",
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
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.IconSize),
                            tint = Color.DarkGray
                        )
                    }
                )
            }
            Spacer(Modifier.height(MediumPadding1))
            Divider()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(SmallPadding),
                contentPadding = PaddingValues(horizontal = SmallPadding)
            ) {
                when(productState) {
                    is ScreenState.Loading -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
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
                            LazyRow(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Top),
                                horizontalArrangement = Arrangement.spacedBy(SmallPadding)) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentSize()
                                    ) {
                                        var expanded by remember { mutableStateOf(false) }
                                        FilterChip(
                                            selected = sortState != null,
                                            onClick = { expanded = !expanded },
                                            label = { Text("Sắp xếp theo") },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.ArrowDropDown,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                                )
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }) {
                                            DropdownMenuItem(
                                                text = { Text("Tên") },
                                                onClick = {
                                                    onSortChanged("name")
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("Giá: thấp đến cao") },
                                                onClick = {
                                                    onSortChanged("priceLowToHigh")
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("Giá: cao đến thấp") },
                                                onClick = {
                                                    onSortChanged("priceHighToLow")
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("Mới nhất") },
                                                onClick = {
                                                    onSortChanged("newest")
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("Cũ nhất") },
                                                onClick = {
                                                    onSortChanged("oldest")
                                                    expanded = false
                                                })
                                        }
                                    }
                                }
                                
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .wrapContentSize()
                                    ) {
                                        var expanded by remember { mutableStateOf(false) }
                                        FilterChip(
                                            selected = minPriceState != null || maxPriceState != null,
                                            onClick = { expanded = true },
                                            label = { Text("Khoảng giá") },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.ArrowDropDown,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                                )
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }) {
                                            DropdownMenuItem(
                                                text = { Text("$0.00 - 49.99") },
                                                onClick = {
                                                    onPriceRangeChosen(0.0f, 49.99f)
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("$50.00 - 99.99") },
                                                onClick = {
                                                    onPriceRangeChosen(50.0f, 99.99f)
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("$100.00 - 149.99") },
                                                onClick = {
                                                    onPriceRangeChosen(100.0f, 149.99f)
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("$150.00 - 199.99") },
                                                onClick = {
                                                    onPriceRangeChosen(150.0f, 199.99f)
                                                    expanded = false
                                                })
                                            DropdownMenuItem(
                                                text = { Text("$200.00+") },
                                                onClick = {
                                                    onPriceRangeChosen(200.0f, null)
                                                    expanded = false
                                                })
                                        }
                                    }
                                }
                            }
                            Text(
                                text = "Tất cả sản phẩm (${productState.uiData.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            LazyRow(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Top),
                                horizontalArrangement = Arrangement.spacedBy(SmallPadding)) {
                                item {
                                   if (sortState != null) {
                                       FilterChip(
                                           selected = false,
                                           onClick = { onSortChanged(null) },
                                           label = { when(sortState) {
                                                "name" -> Text("Tên")
                                               "priceLowToHigh" -> Text("Giá: thấp đến cao")
                                               "priceHighToLow" -> Text("Giá: cao đến thấp")
                                               "newest" -> Text("Mới nhất")
                                               "oldest" -> Text("Cũ nhất")
                                           } },
                                           trailingIcon = {
                                               Icon(
                                                   imageVector = Icons.Filled.Close,
                                                   contentDescription = null,
                                                   modifier = Modifier.size(FilterChipDefaults.IconSize)
                                               ) }
                                       )
                                   }
                                    if (minPriceState != null || maxPriceState != null) {
                                        var label = "$${minPriceState}"
                                        label += if (maxPriceState == null) "+" else " - ${maxPriceState}"
                                        FilterChip(
                                            selected = false,
                                            onClick = { onPriceRangeChosen(null, null) },
                                            label = { Text(label) },
                                            trailingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.Close,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                                ) }
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize=160.dp),
                                modifier = Modifier
                                    .heightIn(max = 800.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(items = productState.uiData, key = { it.id }) { product ->
                                    ProductItemCard(
                                        product = product,
                                        onProductClicked = onProductClicked
                                    )
                                }
                            }
                        }
                    }
                    is ScreenState.Error -> {
                        item {
                            LaunchedEffect(key1 = productState ) {
                                onError(productState.message)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    onProductClicked: (Product) -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier
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
            modifier = Modifier.padding(top = Dimens.ExtraSmallPadding),
            text = product.name,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = Dimens.ExtraSmallPadding),
            text = String.format("$%.2f", product.price),
            fontSize = 18.sp,
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )
    }
}