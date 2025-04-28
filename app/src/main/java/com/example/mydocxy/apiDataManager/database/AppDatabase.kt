package com.example.mydocxy.apiDataManager.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


//@Database(entities = [ObjectData::class], version = 2, exportSchema = false)  // Increment version number
//@TypeConverters(Converters::class)  // Register TypeConverters if needed
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun objectDataDao(): ObjectDataDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Check if the column 'color' exists before adding it
//                val cursor = database.query("PRAGMA table_info(objects)")
//                var columnExists = false
//
//                while (cursor.moveToNext()) {
//                    val columnName = cursor.getString(cursor.getColumnIndex("name"))
//                    if (columnName == "color") {
//                        columnExists = true
//                        break
//                    }
//                }
//                cursor.close()
//
//                // Add the column only if it doesn't exist
//                if (!columnExists) {
//                    database.execSQL("ALTER TABLE objects ADD COLUMN color TEXT")
//                }
//
//                // Repeat the same for other columns if needed
//                if (!columnExists) {
//                    database.execSQL("ALTER TABLE objects ADD COLUMN capacity TEXT")
//                    database.execSQL("ALTER TABLE objects ADD COLUMN price REAL")
//                    database.execSQL("ALTER TABLE objects ADD COLUMN description TEXT")
//                }
//            }
//        }
//
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "object_database"
//                )
//                    .addMigrations(MIGRATION_1_2)  // Add the migration here
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}



@Database(entities = [ObjectData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun objectDataDao(): ObjectDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "object_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}





