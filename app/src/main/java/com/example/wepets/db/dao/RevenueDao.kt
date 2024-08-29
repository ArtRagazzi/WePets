package com.example.wepets.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.wepets.model.Revenue

@Dao
interface RevenueDao {

    @Insert
    fun insert(revenue: Revenue)

    @Update
    fun update(revenue: Revenue)

    @Delete
    fun delete(revenue: Revenue)

    @Query("SELECT * FROM revenue ORDER BY date ASC ")
    fun findAll():List<Revenue>


    @Query("SELECT * FROM revenue WHERE date >= :startDate AND date<= :endDate")
    fun findByMonth(startDate: String, endDate: String): List<Revenue>


}