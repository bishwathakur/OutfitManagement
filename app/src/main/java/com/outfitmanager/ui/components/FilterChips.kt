package com.outfitmanager.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outfitmanager.domain.OutfitState

/**
 * Horizontal scrolling row of filter chips for outfit states.
 * Includes "All" option plus each state.
 */
@Composable
fun FilterChips(
    selectedFilter: OutfitState?,
    onFilterSelected: (OutfitState?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // "All" filter
        FilterChip(
            selected = selectedFilter == null,
            onClick = { onFilterSelected(null) },
            label = { 
                Text(
                    "All",
                    style = MaterialTheme.typography.labelLarge
                ) 
            },
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = null,
            shape = RoundedCornerShape(12.dp)
        )
        
        // State filters
        OutfitState.values().forEach { state ->
            FilterChip(
                selected = selectedFilter == state,
                onClick = { onFilterSelected(state) },
                label = { 
                    Text(
                        state.displayName,
                        style = MaterialTheme.typography.labelLarge
                    ) 
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = state.color.copy(alpha = 0.2f),
                    selectedLabelColor = state.color.copy(alpha = 1f)
                ),
                border = null,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}