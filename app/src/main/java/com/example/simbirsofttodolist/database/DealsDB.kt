package com.example.simbirsofttodolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database (entities = [Deal::class], version = 1)
abstract class DealsDB: RoomDatabase() {
    abstract fun getDao(): Dao
    companion object{
        fun getDB(context: Context) : DealsDB {
            return Room.databaseBuilder(
                context.applicationContext,
                DealsDB::class.java,
                "deals.db"
            ).build()
        }
    }
}