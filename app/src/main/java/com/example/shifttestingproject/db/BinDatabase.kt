package com.example.shifttestingproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shifttestingproject.db.dao.BinDao

@Database(entities = [BinDbModel::class], version = 2, exportSchema = false)
abstract class BinDatabase : RoomDatabase() {

    abstract fun getBinDao(): BinDao

    companion object {
        private var database: BinDatabase? = null

        @Synchronized
        fun getInstance(context: Context): BinDatabase {
            return if (database == null) {
                database = Room.databaseBuilder(
                    context,
                    BinDatabase::class.java, "binDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database as BinDatabase
            } else {
                database as BinDatabase
            }
        }
    }
}