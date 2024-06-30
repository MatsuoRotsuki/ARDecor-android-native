package com.soictnative.ardecor.util

import android.util.Patterns

fun validateEmail(email: String) : RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password cannot be empty")

    if (password.length < 6)
        return RegisterValidation.Failed("Password should contain at least 6 characters")

    return RegisterValidation.Success
}

fun validateDisplayName(displayName: String): RegisterValidation {
    if (displayName.isEmpty())
        return RegisterValidation.Failed("Display name cannot be empty")

    if (displayName.length < 6)
        return RegisterValidation.Failed("Display name should contain at least 6 characters")

    return RegisterValidation.Success
}