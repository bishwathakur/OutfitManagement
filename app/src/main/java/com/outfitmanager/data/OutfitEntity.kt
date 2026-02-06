package com.outfitmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.outfitmanager.domain.OutfitState

/**
 * Room entity representing an outfit.
 * Stores all outfit metadata and current state.
 */
@Entity(tableName = "outfits")
data class OutfitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    /** URI to the outfit image stored in app's internal storage */
    val imageUri: String,
    
    /** User-provided name (optional) */
    val name: String,
    
    /** Type: Casual, Formal, Gym, or Other */
    val type: String,
    
    /** Current state of the outfit */
    val state: OutfitState,
    
    /** Optional user notes */
    val notes: String,
    
    /** Timestamp of last update (for sorting) */
    val lastUpdated: Long = System.currentTimeMillis()
)
