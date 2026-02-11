package com.outfitmanager.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val outfits by viewModel.outfits.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    var selectedOutfit by remember { mutableStateOf<OutfitEntity?>(null) }
    var showQuickActions by remember { mutableStateOf<OutfitEntity?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Outfits",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            // Show "Collect All" button when viewing WASHED items
            if (selectedFilter == OutfitState.WASHED && outfits.isNotEmpty()) {
                androidx.compose.material3.ExtendedFloatingActionButton(
                    onClick = { viewModel.markAllWashedAsAvailable() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Collect All")
                }
            } else {
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
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
                EmptyState(selectedFilter= selectedFilter)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
private fun EmptyState(
    selectedFilter: OutfitState?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (selectedFilter == null) {
                "No Outfits Yet"
            } else {
                "No ${selectedFilter.displayName} Outfits"
            },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (selectedFilter == null) {
                "Add your first outfit to start\norganizing your wardrobe"
            } else {
                "No outfits with this status"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
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
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Show logical next states
            val nextStates = OutfitState.getSuggestedNextStates(outfit.state).take(3)
            nextStates.forEach { state ->
                if (state != outfit.state) {
                    FilledTonalButton(
                        onClick = { onStateChange(state) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = state.color.copy(alpha = 0.15f),
                            contentColor = state.color
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            state.displayName,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}