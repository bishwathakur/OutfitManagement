package com.outfitmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.outfitmanager.domain.OutfitState

/**
 * Colored badge showing outfit state.
 * Compact pill design with state color and white text.
 */
@Composable
fun StateBadge(
    state: OutfitState,
    modifier: Modifier = Modifier
) {
    Text(
        text = state.displayName,
        modifier = modifier
            .background(
                color = state.color.copy(alpha = 0.85f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.White,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Medium
    )
}