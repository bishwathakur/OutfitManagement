package com.outfitmanager.data

import android.content.Context
import android.net.Uri
import com.outfitmanager.domain.OutfitState
import kotlinx.coroutines.flow.Flow
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
        notes: String,
        state: OutfitState = OutfitState.AVAILABLE
    ): Long {
        // Copy image to app's internal storage
        val savedImageUri = copyImageToInternalStorage(imageUri)
        
        val outfit = OutfitEntity(
            imageUri = savedImageUri,
            name = name,
            type = type,
            state = state,
            notes = notes,
            lastUpdated = System.currentTimeMillis()
        )
        
        return dao.insertOutfit(outfit)
    }
    
    /**
     * Update an existing outfit's state.
     */
    suspend fun updateOutfitState(outfitId: Int, newState: OutfitState) {
        val outfit = dao.getOutfitById(outfitId) ?: return
        val updated = outfit.copy(
            state = newState,
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
