package com.outfitmanager.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outfitmanager.data.OutfitEntity
import com.outfitmanager.domain.OutfitState
import com.outfitmanager.ui.components.FilterChips
import com.outfitmanager.ui.components.OutfitCard
import com.outfitmanager.ui.detail.OutfitDetailSheet

/**
 * Home screen showing outfit grid with filters.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val outfits by viewModel.outfits.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    
    var selectedOutfit by remember { mutableStateOf<OutfitEntity?>(null) }
    var showQuickActions by remember { mutableStateOf<OutfitEntity?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Outfit Manager") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add outfit"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )
            
            // Outfit grid or empty state
            if (outfits.isEmpty()) {
                EmptyState()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(outfits, key = { it.id }) { outfit ->
                        OutfitCard(
                            outfit = outfit,
                            onClick = { selectedOutfit = outfit },
                            onLongClick = { showQuickActions = outfit }
                        )
                    }
                }
            }
        }
    }
    
    // Detail bottom sheet
    selectedOutfit?.let { outfit ->
        OutfitDetailSheet(
            outfit = outfit,
            onDismiss = { selectedOutfit = null },
            onStateChange = { newState ->
                viewModel.updateOutfitState(outfit.id, newState)
                selectedOutfit = null
            },
            onEdit = {
                onNavigateToEdit(outfit.id)
                selectedOutfit = null
            },
            onDelete = {
                viewModel.deleteOutfit(outfit)
                selectedOutfit = null
            }
        )
    }
    
    // Quick actions menu
    showQuickActions?.let { outfit ->
        QuickActionsMenu(
            outfit = outfit,
            onDismiss = { showQuickActions = null },
            onStateChange = { newState ->
                viewModel.updateOutfitState(outfit.id, newState)
                showQuickActions = null
            }
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "👔",
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No outfits yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add your first outfit to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickActionsMenu(
    outfit: OutfitEntity,
    onDismiss: () -> Unit,
    onStateChange: (OutfitState) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Quick Actions") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Show logical next states
                val nextStates = OutfitState.getSuggestedNextStates(outfit.state).take(3)
                nextStates.forEach { state ->
                    if (state != outfit.state) {
                        Button(
                            onClick = { onStateChange(state) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = state.color
                            )
                        ) {
                            Text(state.displayName)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
