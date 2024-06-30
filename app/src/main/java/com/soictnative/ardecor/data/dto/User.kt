package com.soictnative.ardecor.data.dto

import android.net.Uri

data class User(
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
) {
    constructor(): this( "", "", "")
}