package com.outfitmanager.data

import androidx.room.TypeConverter
import com.outfitmanager.domain.OutfitState

/**
 * Type converters for Room to handle enum serialization.
 */
class Converters {
    
    @TypeConverter
    fun fromOutfitState(state: OutfitState): String {
        return state.name
    }
    
    @TypeConverter
    fun toOutfitState(value: String): OutfitState {
        return OutfitState.valueOf(value)
    }
}
