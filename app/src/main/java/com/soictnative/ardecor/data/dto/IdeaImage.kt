package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class IdeaImage (
    val id: Int,
    val idea_id: Int,
    val image_url: String,
    val created_at: String,
    val updated_at: String,
)
