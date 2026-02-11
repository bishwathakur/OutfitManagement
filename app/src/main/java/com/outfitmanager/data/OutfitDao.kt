package com.outfitmanager.data

import androidx.room.*
import com.outfitmanager.domain.OutfitState
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for outfit operations.
 * Provides reactive queries using Flow for automatic UI updates.
 */
@Dao
interface OutfitDao {
    
    /**
     * Get all outfits sorted by most recently updated.
     */
    @Query("SELECT * FROM outfits ORDER BY lastUpdated DESC")
    fun getAllOutfits(): Flow<List<OutfitEntity>>
    
    /**
     * Get outfits filtered by a specific state.
     */
    @Query("SELECT * FROM outfits WHERE state = :state ORDER BY lastUpdated DESC")
    fun getOutfitsByState(state: OutfitState): Flow<List<OutfitEntity>>
    
    /**
     * Get a single outfit by ID.
     */
    @Query("SELECT * FROM outfits WHERE id = :id")
    suspend fun getOutfitById(id: Int): OutfitEntity?
    
    /**
     * Insert a new outfit or replace if exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: OutfitEntity): Long
    
    /**
     * Update an existing outfit.
     */
    @Update
    suspend fun updateOutfit(outfit: OutfitEntity)
    
    /**
     * Delete an outfit.
     */
    @Delete
    suspend fun deleteOutfit(outfit: OutfitEntity)
    
    /**
     * Get all outfits in "Worn" state for auto-transition checking.
     */
    @Query("SELECT * FROM outfits WHERE state = 'WORN' AND wornSinceTimestamp IS NOT NULL")
    suspend fun getWornOutfits(): List<OutfitEntity>
    
    /**
     * Count outfits that need laundry (for notifications).
     */
    @Query("SELECT COUNT(*) FROM outfits WHERE state = 'NEEDS_LAUNDRY'")
    suspend fun getNeedsLaundryCount(): Int
    
    @Query("SELECT * FROM outfits WHERE state = 'IN_LAUNDRY' AND inLaundrySinceTimestamp IS NOT NULL")
    suspend fun getInLaundryOutfits(): List<OutfitEntity>
    
    @Query("SELECT COUNT(*) FROM outfits WHERE state = 'WASHED'")
    suspend fun getWashedCount(): Int
}
