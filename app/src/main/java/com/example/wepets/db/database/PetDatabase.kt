package com.example.wepets.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wepets.db.dao.PetDao
import com.example.wepets.model.Pet
import kotlin.concurrent.Volatile

@Database(
    entities = [Pet::class],
    version = 1
)
abstract class PetDatabase:RoomDatabase() {

    abstract val petDao:PetDao


    companion object {
        @Volatile
        private var INSTANCE: PetDatabase?=null

        fun getDatabase(context: Context): PetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetDatabase::class.java,
                    "pet_database"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}