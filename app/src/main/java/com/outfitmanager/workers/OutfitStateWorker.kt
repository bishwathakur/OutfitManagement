package com.outfitmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.outfitmanager.data.OutfitRepository
import com.outfitmanager.notification.NotificationHelper

/**
 * Background worker that periodically checks outfit states.
 * - Checks WORN items and auto-transitions to NEEDS_LAUNDRY based on duration
 * - Checks IN_LAUNDRY items and auto-transitions to WASHED after 4 days
 * - Sends notification when laundry is ready
 */
class OutfitStateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val repository = OutfitRepository(applicationContext)
            
            // Check worn items (existing logic)
            repository.checkAndAutoTransitionWornItems()
            
            // Check laundry items (NEW)
            val washedCountBefore = repository.getWashedCount()
            repository.checkAndAutoTransitionLaundryItems()
            val washedCountAfter = repository.getWashedCount()
            
            // Send notification if new items are washed
            val newlyWashed = washedCountAfter - washedCountBefore
            if (newlyWashed > 0) {
                NotificationHelper.showLaundryReadyNotification(applicationContext, newlyWashed)
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
