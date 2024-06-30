package com.soictnative.ardecor.util

sealed class ScreenState<out T : Any> {

    object Unspecified : ScreenState<Nothing>()
    object Loading : ScreenState<Nothing>()
    data class Error(val message: String) : ScreenState<Nothing>()
    data class Success<out T : Any>(val uiData: T) : ScreenState<T>()
}