package com.outfitmanager.ui.addedit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitmanager.data.OutfitRepository
import com.outfitmanager.domain.OutfitState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Add/Edit outfit screen.
 * Manages form state and save operations.
 */
class AddEditViewModel(
    private val repository: OutfitRepository
) : ViewModel() {
    
    data class OutfitFormState(
        val outfitId: Int? = null,
        val imageUri: Uri? = null,
        val name: String = "",
        val type: String = "Casual",
        val notes: String = "",
        val isSaving: Boolean = false,
        val error: String? = null
    )
    
    private val _formState = MutableStateFlow(OutfitFormState())
    val formState = _formState.asStateFlow()
    
    /**
     * Load an existing outfit for editing.
     */
    fun loadOutfit(outfitId: Int) {
        viewModelScope.launch {
            val outfit = repository.getOutfitById(outfitId) ?: return@launch
            _formState.value = OutfitFormState(
                outfitId = outfit.id,
                imageUri = Uri.parse(outfit.imageUri),
                name = outfit.name,
                type = outfit.type,
                notes = outfit.notes
            )
        }
    }
    
    /**
     * Update image URI.
     */
    fun setImageUri(uri: Uri) {
        _formState.value = _formState.value.copy(imageUri = uri, error = null)
    }
    
    /**
     * Update name.
     */
    fun setName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }
    
    /**
     * Update type.
     */
    fun setType(type: String) {
        _formState.value = _formState.value.copy(type = type)
    }
    
    /**
     * Update notes.
     */
    fun setNotes(notes: String) {
        _formState.value = _formState.value.copy(notes = notes)
    }
    
    /**
     * Save the outfit.
     * Returns true if successful.
     */
    suspend fun saveOutfit(): Boolean {
        val state = _formState.value
        
        // Validate
        if (state.imageUri == null) {
            _formState.value = state.copy(error = "Photo is required")
            return false
        }
        
        _formState.value = state.copy(isSaving = true)
        
        return try {
            if (state.outfitId == null) {
                // Create new outfit
                repository.insertOutfit(
                    imageUri = state.imageUri,
                    name = state.name,
                    type = state.type,
                    notes = state.notes,
                    state = OutfitState.AVAILABLE
                )
            } else {
                // Update existing outfit
                repository.updateOutfit(
                    outfitId = state.outfitId,
                    imageUri = state.imageUri,
                    name = state.name,
                    type = state.type,
                    notes = state.notes
                )
            }
            true
        } catch (e: Exception) {
            _formState.value = state.copy(
                isSaving = false,
                error = e.message ?: "Failed to save outfit"
            )
            false
        }
    }
}
