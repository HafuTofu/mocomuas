package com.example.uas_mocom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MatchEntity::class], version = 1, exportSchema = false)
abstract class MatchDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao

    companion object {
        @Volatile
        private var INSTANCE: MatchDatabase? = null

        fun getInstance(context: Context): MatchDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MatchDatabase::class.java,
                    "scoreboard.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
