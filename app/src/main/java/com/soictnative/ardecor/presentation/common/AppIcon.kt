package com.soictnative.ardecor.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    resourceId: Int,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Icon(
        imageVector = ImageVector.vectorResource(resourceId),
        contentDescription = null,
        tint = tint
    )
}