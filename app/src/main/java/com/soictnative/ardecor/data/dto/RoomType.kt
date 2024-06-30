package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class RoomType(
    val id: Int,
    val name: String,
    val image_url: String,
)
