package com.outfitmanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitmanager.data.OutfitEntity
import com.outfitmanager.data.OutfitRepository
import com.outfitmanager.domain.OutfitState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for the home screen.
 * Manages outfit list, filtering, and state updates.
 */
class HomeViewModel(
    private val repository: OutfitRepository
) : ViewModel() {
    
    // Selected filter state
    private val _selectedFilter = MutableStateFlow<OutfitState?>(null)
    val selectedFilter = _selectedFilter.asStateFlow()
    
    // Outfits from repository (filtered based on selection)
    val outfits: StateFlow<List<OutfitEntity>> = _selectedFilter
        .flatMapLatest { filter ->
            if (filter == null) {
                repository.getAllOutfits()
            } else {
                repository.getOutfitsByState(filter)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    /**
     * Update the selected filter.
     */
    fun setFilter(state: OutfitState?) {
        _selectedFilter.value = state
    }
    
    /**
     * Update an outfit's state.
     */
    fun updateOutfitState(outfitId: Int, newState: OutfitState) {
        viewModelScope.launch {
            repository.updateOutfitState(outfitId, newState)
        }
    }
    
    /**
     * Delete an outfit.
     */
    fun deleteOutfit(outfit: OutfitEntity) {
        viewModelScope.launch {
            repository.deleteOutfit(outfit)
        }
    }
}
