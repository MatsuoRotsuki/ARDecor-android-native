package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

@Immutable
data class Product(
    val id: Int,
    val name: String,
    val price: Float,
    val model_path: String? = null,
    val is_stackable: Int = 0,
    val source: String? = null,
    val description: String? = null,
    val image_url: String,
//    val colors: List<String>? = null,
    val specification_id: String? = null,
    val category_id: Int,
    val created_at: String? = null,
    val updated_at: String? = null,
    val product_variations: List<Product>? = null,
    val images: List<ProductImage>? = null,
    val category: Category? = null,
    val measurements: List<Measurement>? = null,
) {
//    constructor(): this("0", "", 0f, "", "", "", emptyList(), emptyList(),"", Timestamp.now())
}
