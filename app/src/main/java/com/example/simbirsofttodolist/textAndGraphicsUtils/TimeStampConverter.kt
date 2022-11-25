package com.example.simbirsofttodolist.textAndGraphicsUtils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimeStampConverter {
    fun getDate(timestamp: Long): Long{
        return try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp * 1000L
            val time = android.text.format.DateFormat.format("dd-MM-yyyy", calendar).toString().split('-')
            return setFinishDate(
                time[1],
                time[0],
                time[2],
                "23",
                "59",
                "59",
            )
        } catch (e: Exception) {
            0
        }
    }
    fun setFinishDate(
        month : String,
        day : String,
        year : String,
        hour : String = "00",
        minute : String = "00",
        seconds : String = "00",
    ): Long{
        val strDate = "$month-$day-${year} $hour:$minute:$seconds"
        val formatter: DateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm:ss")
        val date = formatter.parse(strDate) as Date
        return (date.time / 1000L)
    }
    fun setStartDate(): String{
        val c = Calendar.getInstance()

        val year = c.get(android.icu.util.Calendar.YEAR)
        val month = c.get(android.icu.util.Calendar.MONTH) + 1
        val day = c.get(android.icu.util.Calendar.DAY_OF_MONTH)

        val hour = c.get(android.icu.util.Calendar.HOUR_OF_DAY)
        val minute = c.get(android.icu.util.Calendar.MINUTE)
        val seconds = c.get(android.icu.util.Calendar.SECOND)

        val strDate =  "$month-$day-$year $hour:$minute:$seconds"
        val formatter: DateFormat = SimpleDateFormat("MM-dd-yyyy hh:mm:ss")
        val date = formatter.parse(strDate) as Date
        return (date.time / 1000L).toString()
    }

}