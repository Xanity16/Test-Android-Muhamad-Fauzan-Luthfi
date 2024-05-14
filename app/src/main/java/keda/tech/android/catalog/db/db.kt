package keda.tech.android.catalog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Product::class
    ], version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        var TEST_MODE = false

        fun getAppDataBase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                if (TEST_MODE) {
                    val instance = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java
                    ).allowMainThreadQueries().build()
                    INSTANCE = instance
                } else {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "${context.packageName}.db"
                    ).build()
                    INSTANCE = instance
                }
            }
            return INSTANCE!!
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}