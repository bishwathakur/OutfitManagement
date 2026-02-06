package com.outfitmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.outfitmanager.data.OutfitRepository
import com.outfitmanager.ui.addedit.AddEditScreen
import com.outfitmanager.ui.addedit.AddEditViewModel
import com.outfitmanager.ui.home.HomeScreen
import com.outfitmanager.ui.home.HomeViewModel
import com.outfitmanager.ui.theme.OutfitManagementTheme

class MainActivity : ComponentActivity() {
    
    private lateinit var repository: OutfitRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize repository
        repository = OutfitRepository(applicationContext)
        
        setContent {
            OutfitManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OutfitApp(repository)
                }
            }
        }
    }
}

@Composable
fun OutfitApp(repository: OutfitRepository) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home screen
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HomeViewModel(repository) as T
                    }
                }
            )
            
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToEdit = { id -> navController.navigate("edit/$id") }
            )
        }
        
        // Add screen
        composable("add") {
            val viewModel: AddEditViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AddEditViewModel(repository) as T
                    }
                }
            )
            
            AddEditScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Edit screen
        composable(
            route = "edit/{outfitId}",
            arguments = listOf(navArgument("outfitId") { type = NavType.IntType })
        ) { backStackEntry ->
            val outfitId = backStackEntry.arguments?.getInt("outfitId")
            
            val viewModel: AddEditViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AddEditViewModel(repository) as T
                    }
                }
            )
            
            AddEditScreen(
                viewModel = viewModel,
                outfitId = outfitId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
