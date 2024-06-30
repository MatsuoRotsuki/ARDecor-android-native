package com.soictnative.ardecor.presentation.screens.ideas

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.soictnative.ardecor.data.dto.RoomType
import com.soictnative.ardecor.data.dto.SavedPlacement
import com.soictnative.ardecor.presentation.common.Dimens.ExtraSmallPadding
import com.soictnative.ardecor.presentation.common.Dimens.MediumPadding1
import com.soictnative.ardecor.presentation.common.Dimens.SmallPadding
import com.soictnative.ardecor.util.ScreenState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIdeaRoute(
    roomTypeState: ScreenState<List<RoomType>>?,
    savedPlacement: SavedPlacement?,
    onSubmit: (String, String, Int, List<Uri>, SavedPlacement) -> Unit,
    onClickBack: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var roomTypeDropdownExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    var selectedRoomTypeId by remember { mutableStateOf(0) }

    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val clipData = result.data?.clipData
            if (clipData != null) {
                val selectedUris = mutableListOf<Uri>()
                for (i in 0 until clipData.itemCount) {
                    selectedUris.add(clipData.getItemAt(i).uri)
                }
                imageUris = selectedUris
            } else {
                val imageUri = result.data?.data ?: return@rememberLauncherForActivityResult
                imageUris = listOf(imageUri)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = MediumPadding1),
        contentAlignment = Alignment.TopCenter
    ) {
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = "Tạo ý tưởng thiết kế mới",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Button(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    onClick = {
                        onSubmit(
                            name,
                            description,
                            selectedRoomTypeId,
                            imageUris,
                            savedPlacement!!
                        )
                    }
                ) {
                    Text(
                        text = "Đăng",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(15.dp))

            LazyColumn(
                modifier = Modifier.padding(horizontal = MediumPadding1)
                    .padding(bottom = MediumPadding1)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label =  { Text("Tên") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(MediumPadding1))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Mô tả") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    ExposedDropdownMenuBox(
                        expanded = roomTypeDropdownExpanded,
                        onExpandedChange = {
                            roomTypeDropdownExpanded = !roomTypeDropdownExpanded
                        },
                    ) {
                        OutlinedTextField(
                            value = selectedOptionText,
                            onValueChange = { selectedOptionText = it },
                            label = { Text("Kiểu phòng") },
                            readOnly = true,
                            trailingIcon = {
                               ExposedDropdownMenuDefaults.TrailingIcon(
                                   expanded = roomTypeDropdownExpanded
                               )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        if (roomTypeState is ScreenState.Success) {
                            ExposedDropdownMenu(
                                expanded = roomTypeDropdownExpanded,
                                onDismissRequest = { roomTypeDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                roomTypeState.uiData.forEach { roomType ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedOptionText = roomType.name
                                            roomTypeDropdownExpanded = false
                                            selectedRoomTypeId = roomType.id
                                        },
                                        text = { Text(roomType.name) },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(MediumPadding1))
                    Text(
                        text = "Chọn ảnh",
                        fontSize = 18.sp,
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (imageUris.isEmpty()) {
                            IconButton(onClick = {
                                activityResultLauncher.launch(
                                    Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    ).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                )
                            }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        } else {
                            LazyRow(verticalAlignment = Alignment.CenterVertically) {
                                items (items = imageUris, key = { it.path!! }) { imageUri ->
                                    Box(modifier = Modifier.padding(4.dp)) {
                                        Image(
                                            painter = rememberImagePainter(imageUri),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(300.dp)
                                                .padding(10.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                imageUris = imageUris.toMutableList().apply {
                                                    remove(imageUri)
                                                }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .background(Color.White.copy(alpha = 0.7f))
                                                .size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(MediumPadding1))
                    Text(
                        text = "Danh sách sản phẩm bao gồm",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(SmallPadding))
                }
                items (items = savedPlacement?.products!!, key = { it.id }) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .width(80.dp)
                                .aspectRatio(1f),
                            contentDescription = null,
                            model = product.image_url,
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 6.dp, end = 12.dp)
                                .weight(1f)
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