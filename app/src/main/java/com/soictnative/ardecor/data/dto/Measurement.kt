package com.soictnative.ardecor.data.dto

data class Measurement(
    val id: Int,
    val value: Double,
    val product_id: Int,
    val measurement_type_id: Int,
    val measurement_type: MeasurementType? = null,
) {
}