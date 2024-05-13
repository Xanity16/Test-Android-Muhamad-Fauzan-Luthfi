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

        fun getAppDataBase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "${context.packageName}.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }
}