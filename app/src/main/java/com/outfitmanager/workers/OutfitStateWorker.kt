package com.outfitmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.outfitmanager.data.OutfitRepository

/**
 * Background worker that checks worn outfits and auto-transitions them
 * to "Needs Laundry" based on duration worn and category.
 * 
 * Runs periodically (every 6 hours) to ensure timely transitions.
 */
class OutfitStateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val repository = OutfitRepository(applicationContext)
            repository.checkAndAutoTransitionWornItems()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
