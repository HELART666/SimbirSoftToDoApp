package com.example.simbirsofttodolist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertDeal(deal: Deal)
    @Query("SELECT * FROM deals")
    fun getAllDeals(): Flow<List<Deal>>
    @Query("SELECT * FROM deals WHERE datetime(dateFinish, 'unixepoch') BETWEEN datetime(:dateStart, 'unixepoch') AND datetime(:dateEnd, 'unixepoch')")
    fun getDealsForDate(dateStart: Long, dateEnd: Long): Flow<List<Deal>>
    @Query("DELETE from deals WHERE id = :id")
    fun deleteDeal(id: Int?): Int
}