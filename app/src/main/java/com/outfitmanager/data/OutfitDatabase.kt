package com.outfitmanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context

/**
 * Room database for the Outfit Manager app.
 * Singleton pattern ensures only one instance exists.
 */
@Database(
    entities = [OutfitEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OutfitDatabase : RoomDatabase() {
    
    abstract fun outfitDao(): OutfitDao
    
    companion object {
        @Volatile
        private var INSTANCE: OutfitDatabase? = null
        
        fun getDatabase(context: Context): OutfitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OutfitDatabase::class.java,
                    "outfit_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
