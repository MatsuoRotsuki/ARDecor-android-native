package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class SavedPlacement(
    val id: Int,
    val name: String,
    val placements: String,
    val user_id: String,
    val created_at: String,
    val updated_at: String,
    val created_at_human: String? = null,
    val updated_at_human: String? = null,
    val products: List<Product>? = null,
) {

}
