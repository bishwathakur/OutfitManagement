package com.outfitmanager.domain

import androidx.compose.ui.graphics.Color

/**
 * The five states an outfit can be in.
 * Each state has a display name and color for UI consistency.
 */
enum class OutfitState(val displayName: String, val color: Color) {
    AVAILABLE("Available", Color(0xFF4CAF50)),        // Green
    WORN("Worn", Color(0xFFFFC107)),                  // Amber
    NEEDS_LAUNDRY("Needs Laundry", Color(0xFFFF5722)), // Deep Orange
    IN_LAUNDRY("In Laundry", Color(0xFF2196F3)),      // Blue
    WASHED("Washed", Color(0xFF9C27B0));              // Purple
    
    companion object {
        /**
         * Get suggested next states based on typical workflow.
         * Returns all states to allow manual overrides.
         */
        fun getSuggestedNextStates(current: OutfitState): List<OutfitState> {
            return when (current) {
                AVAILABLE -> listOf(WORN, NEEDS_LAUNDRY, IN_LAUNDRY, WASHED)
                WORN -> listOf(NEEDS_LAUNDRY, AVAILABLE, IN_LAUNDRY, WASHED)
                NEEDS_LAUNDRY -> listOf(IN_LAUNDRY, WASHED, AVAILABLE, WORN)
                IN_LAUNDRY -> listOf(WASHED, AVAILABLE, NEEDS_LAUNDRY, WORN)
                WASHED -> listOf(AVAILABLE, WORN, NEEDS_LAUNDRY, IN_LAUNDRY)
            }
        }
    }
}
