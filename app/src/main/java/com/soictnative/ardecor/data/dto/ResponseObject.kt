package com.soictnative.ardecor.data.dto

data class ResponseObject<T> (
    val totalPages: Int? = null,
    val data: T,
    val message: String? = null,
    val success: Boolean
) {
}