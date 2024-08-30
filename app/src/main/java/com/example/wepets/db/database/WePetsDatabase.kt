package com.example.wepets.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.db.dao.ScheduleDao
import com.example.wepets.model.Pet
import com.example.wepets.model.Revenue
import com.example.wepets.model.Schedule
import kotlin.concurrent.Volatile

@Database(
    entities = [Pet::class, Revenue::class,Schedule::class],
    version = 1
)
abstract class WePetsDatabase:RoomDatabase() {

    abstract val getPetDao:PetDao
    abstract val getRevenueDao:RevenueDao
    abstract val getScheduleDao:ScheduleDao


    companion object {
        @Volatile
        private var INSTANCE: WePetsDatabase?=null

        fun getDatabase(context: Context): WePetsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WePetsDatabase::class.java,
                    "pet_database"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}