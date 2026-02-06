package com.outfitmanager.workers

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Helper object to schedule WorkManager tasks for:
 * 1. Periodic state checks (every 6 hours)
 * 2. Daily laundry notifications (at 2 PM)
 */
object WorkManagerHelper {
    
    /**
     * Schedule periodic checks for auto-transitioning worn items.
     * Runs every 6 hours.
     */
    fun scheduleStateCheckWorker(context: Context) {
        val checkRequest = PeriodicWorkRequestBuilder<OutfitStateWorker>(
            6, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "outfit_state_check",
            ExistingPeriodicWorkPolicy.KEEP,
            checkRequest
        )
    }
    
    /**
     * Schedule daily notification at 2 PM showing laundry count.
     */
    fun scheduleDailyNotification(context: Context) {
        // Calculate delay until next 2 PM
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14)  // 2 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If it's already past 2 PM today, schedule for 2 PM tomorrow
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis
        
        val notificationRequest = PeriodicWorkRequestBuilder<LaundryNotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_laundry_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            notificationRequest
        )
    }
    
    /**
     * Cancel all scheduled work (for debugging or user preference).
     */
    fun cancelAllWork(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("outfit_state_check")
        WorkManager.getInstance(context).cancelUniqueWork("daily_laundry_notification")
    }
}
