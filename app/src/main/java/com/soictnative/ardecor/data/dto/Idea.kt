package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class Idea(
    val id: Int,
    val name: String,
    val image_url: String,
    val placements: String,
    val description : String? = null,
    val room_type_id: Int,
    val user_id: String,
    val created_at: String,
    val updated_at: String,
    val created_at_human: String? = null,
    val updated_at_human: String? = null,
    val products: List<Product>? = null,
    val images: List<IdeaImage>? = null,
    val room_type: RoomType? = null,
)
