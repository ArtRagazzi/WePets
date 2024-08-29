package com.example.wepets.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wepets.db.dao.PetDao
import com.example.wepets.db.dao.RevenueDao
import com.example.wepets.model.Pet
import com.example.wepets.model.Revenue
import kotlin.concurrent.Volatile

@Database(
    entities = [Pet::class, Revenue::class],
    version = 2
)
abstract class WePetsDatabase:RoomDatabase() {

    abstract val getPetDao:PetDao
    abstract val getRevenueDao:RevenueDao


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