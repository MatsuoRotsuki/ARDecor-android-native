package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class ProductImage(
    val id: Int,
    val product_id: Int,
    val image_url: String,
    val type: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
) {
}
