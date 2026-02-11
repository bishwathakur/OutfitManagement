package com.outfitmanager.data

import android.content.Context
import android.net.Uri
import com.outfitmanager.domain.OutfitState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileOutputStream

/**
 * Repository for outfit data operations.
 * Handles database operations and image file management.
 */
class OutfitRepository(private val context: Context) {
    
    private val dao = OutfitDatabase.getDatabase(context).outfitDao()
    
    /**
     * Get all outfits as a reactive Flow.
     */
    fun getAllOutfits(): Flow<List<OutfitEntity>> = dao.getAllOutfits()
    
    /**
     * Get outfits filtered by state.
     */
    fun getOutfitsByState(state: OutfitState): Flow<List<OutfitEntity>> =
        dao.getOutfitsByState(state)
    
    /**
     * Get a single outfit by ID.
     */
    suspend fun getOutfitById(id: Int): OutfitEntity? = dao.getOutfitById(id)
    
    /**
     * Save a new outfit.
     * Copies the image to internal storage and stores the URI.
     */
    suspend fun insertOutfit(
        imageUri: Uri,
        name: String,
        type: String,
        category: String = "Regular",
        notes: String,
        state: OutfitState = OutfitState.AVAILABLE
    ): Long {
        // Copy image to app's internal storage
        val savedImageUri = copyImageToInternalStorage(imageUri)
        
        val outfit = OutfitEntity(
            imageUri = savedImageUri,
            name = name,
            type = type,
            category = category,
            state = state,
            notes = notes,
            lastUpdated = System.currentTimeMillis()
        )
        
        return dao.insertOutfit(outfit)
    }
    
    /**
     * Update an existing outfit's state.
     * Sets wornSinceTimestamp when moving to WORN state.
     * Sets inLaundrySinceTimestamp when moving to IN_LAUNDRY state.
     * Clears timestamps when leaving respective states.
     */
    suspend fun updateOutfitState(outfitId: Int, newState: OutfitState) {
        val outfit = dao.getOutfitById(outfitId) ?: return
        
        val wornTimestamp = when {
            newState == OutfitState.WORN && outfit.state != OutfitState.WORN -> System.currentTimeMillis()
            newState != OutfitState.WORN -> null
            else -> outfit.wornSinceTimestamp
        }
        
        val laundryTimestamp = when {
            newState == OutfitState.IN_LAUNDRY && outfit.state != OutfitState.IN_LAUNDRY -> System.currentTimeMillis()
            newState != OutfitState.IN_LAUNDRY -> null
            else -> outfit.inLaundrySinceTimestamp
        }
        
        val updated = outfit.copy(
            state = newState,
            wornSinceTimestamp = wornTimestamp,
            inLaundrySinceTimestamp = laundryTimestamp,
            lastUpdated = System.currentTimeMillis()
        )
        dao.updateOutfit(updated)
    }
    
    /**
     * Update an outfit's details (for edit functionality).
     */
    suspend fun updateOutfit(
        outfitId: Int,
        imageUri: Uri?,
        name: String,
        type: String,
        category: String,
        notes: String
    ) {
        val outfit = dao.getOutfitById(outfitId) ?: return
        
        val newImageUri = if (imageUri != null && imageUri.toString() != outfit.imageUri) {
            // New image selected, copy it
            copyImageToInternalStorage(imageUri)
        } else {
            // Keep existing image
            outfit.imageUri
        }
        
        val updated = outfit.copy(
            imageUri = newImageUri,
            name = name,
            type = type,
            category = category,
            notes = notes,
            lastUpdated = System.currentTimeMillis()
        )
        dao.updateOutfit(updated)
    }
    
    /**
     * Delete an outfit and its image file.
     */
    suspend fun deleteOutfit(outfit: OutfitEntity) {
        // Delete image file
        try {
            val file = File(outfit.imageUri)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        dao.deleteOutfit(outfit)
    }
    
    /**
     * Check all worn items and auto-transition to "Needs Laundry" if worn too long.
     * Regular clothes: 3 days
     * Underwear: 1 day
     */
    suspend fun checkAndAutoTransitionWornItems() {
        val wornOutfits = dao.getWornOutfits()
        val now = System.currentTimeMillis()
        
        wornOutfits.forEach { outfit ->
            val wornDuration = now - (outfit.wornSinceTimestamp ?: return@forEach)
            val daysWorn = wornDuration / (24 * 60 * 60 * 1000)
            
            val shouldTransition = when (outfit.category.lowercase()) {
                "underwear" -> daysWorn >= 1
                else -> daysWorn >= 3
            }
            
            if (shouldTransition) {
                updateOutfitState(outfit.id, OutfitState.NEEDS_LAUNDRY)
            }
        }
    }
    
    /**
     * Get count of items that need laundry (for notifications).
     */
    suspend fun getNeedsLaundryCount(): Int {
        return dao.getNeedsLaundryCount()
    }
    
    /**
     * Check IN_LAUNDRY items and auto-transition to WASHED after 4 days.
     */
    suspend fun checkAndAutoTransitionLaundryItems() {
        val inLaundryOutfits = dao.getInLaundryOutfits()
        val now = System.currentTimeMillis()
        
        inLaundryOutfits.forEach { outfit ->
            val laundryDuration = now - (outfit.inLaundrySinceTimestamp ?: return@forEach)
            val daysInLaundry = laundryDuration / (24 * 60 * 60 * 1000)
            
            if (daysInLaundry >= 4) {
                updateOutfitState(outfit.id, OutfitState.WASHED)
            }
        }
    }
    
    /**
     * Get count of washed items (for notifications).
     */
    suspend fun getWashedCount(): Int {
        return dao.getWashedCount()
    }
    
    /**
     * Mark all WASHED items as AVAILABLE (collected from laundry).
     */
    suspend fun markAllWashedAsAvailable() {
        val washedOutfits = dao.getOutfitsByState(OutfitState.WASHED).first()
        washedOutfits.forEach { outfit ->
            updateOutfitState(outfit.id, OutfitState.AVAILABLE)
        }
    }
    
    /**
     * Copy an image from URI to app's internal storage.
     * Returns the file path as a string.
     */
    private fun copyImageToInternalStorage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Cannot open image URI")
        
        // Create outfits directory if it doesn't exist
        val outfitsDir = File(context.filesDir, "outfits")
        if (!outfitsDir.exists()) {
            outfitsDir.mkdirs()
        }
        
        // Generate unique filename
        val filename = "outfit_${System.currentTimeMillis()}.jpg"
        val file = File(outfitsDir, filename)
        
        // Copy file
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        
        return file.absolutePath
    }
}
