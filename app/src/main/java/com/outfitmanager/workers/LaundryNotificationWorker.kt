package com.outfitmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.outfitmanager.data.OutfitRepository
import com.outfitmanager.notification.NotificationHelper

/**
 * Background worker that sends daily laundry reminder notification at 2 PM.
 * Shows count of outfits that need laundry.
 */
class LaundryNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val repository = OutfitRepository(applicationContext)
            val count = repository.getNeedsLaundryCount()
            
            if (count > 0) {
                NotificationHelper.showLaundryNotification(applicationContext, count)
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
