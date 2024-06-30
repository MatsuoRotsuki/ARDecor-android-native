package com.soictnative.ardecor.data.dto

import androidx.compose.runtime.Immutable

//sealed class Category (
//    val category: String
//) {
//    object Chair: Category("Chair")
//    object Cupboard: Category("Cupboard")
//    object Table: Category("Table")
//    object Accessory: Category("Accessory")
//    object Furniture: Category("Furniture")
//}

@Immutable
data class Category(
    val id: Int,
    val name: String,
    val description: String? = null,
    val image_url: String,
    val products: List<Product>? = null,
) {

}