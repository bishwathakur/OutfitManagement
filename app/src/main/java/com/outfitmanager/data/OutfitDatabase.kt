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
    version = 2,
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
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Add category column with default value "Regular"
                database.execSQL("ALTER TABLE outfits ADD COLUMN category TEXT NOT NULL DEFAULT 'Regular'")
                // Add wornSinceTimestamp column (nullable)
                database.execSQL("ALTER TABLE outfits ADD COLUMN wornSinceTimestamp INTEGER")
            }
        }
    }
}
