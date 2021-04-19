package com.muliamaulana.tangutang.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muliamaulana.tangutang.model.Debt

//Database annotation to specify the entities and set version
@Database(entities = [Debt::class], version = 1, exportSchema = false)
abstract class DebtRoomDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: DebtRoomDatabase? = null

        fun getDatabase(context: Context): DebtRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DebtRoomDatabase::class.java,
                    "debt_db"
                )
                    .allowMainThreadQueries() // allows Room to executing task in main thread
                    .fallbackToDestructiveMigration() // allows Room to recreate table if no migrations found
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun debtDao(): DebtDao
}