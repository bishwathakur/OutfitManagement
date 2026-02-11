package com.outfitmanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room database for the Outfit Manager app.
 * Singleton pattern ensures only one instance exists.
 */
@Database(
    entities = [OutfitEntity::class],
    version = 3,
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add category column with default value "Regular"
                database.execSQL("ALTER TABLE outfits ADD COLUMN category TEXT NOT NULL DEFAULT 'Regular'")
                // Add wornSinceTimestamp column (nullable)
                database.execSQL("ALTER TABLE outfits ADD COLUMN wornSinceTimestamp INTEGER")
            }
        }
        
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE outfits ADD COLUMN inLaundrySinceTimestamp INTEGER")
            }
        }
    }
}
