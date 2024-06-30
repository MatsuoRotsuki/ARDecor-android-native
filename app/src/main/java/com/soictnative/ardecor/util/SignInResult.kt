package com.soictnative.ardecor.util

import com.google.firebase.auth.FirebaseUser

data class SignInResult(
    val firebaseUser: FirebaseUser? = null,
    val errorMessage: String?,
    val displayName: String? = null,
    val photoUrl: String? = null,
) {
}