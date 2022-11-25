package com.example.simbirsofttodolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Timestamp

@Entity (tableName = "deals")
data class Deal(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "dateStart")
    val dateStart: Long,
    @ColumnInfo(name = "dateFinish")
    val dateFinish: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String
    ) : Serializable