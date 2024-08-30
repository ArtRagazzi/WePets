package com.example.wepets.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.wepets.model.Schedule

@Dao
interface ScheduleDao {

    @Insert
    fun insert(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)

    @Query("SELECT * FROM schedule ORDER BY date ASC ")
    fun findAll():List<Schedule>


    @Query("SELECT * FROM schedule WHERE date >= :startDate AND date<= :endDate ORDER BY id")
    fun findByDay(startDate: String, endDate: String): List<Schedule>
}