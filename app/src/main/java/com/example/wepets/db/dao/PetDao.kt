package com.example.wepets.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.wepets.model.Pet

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(pet: Pet)

    @Update
    fun update(pet: Pet)

    @Delete
    fun delete(pet: Pet)


    @Query("SELECT * FROM pet WHERE id = :id")
    fun findById(id:Int):Pet

    @Query("SELECT * FROM pet ORDER BY namePet ASC")
    fun findAll():List<Pet>

    @Query("SELECT * FROM pet WHERE namePet LIKE '%' || :name || '%' OR ownerName LIKE '%' || :name || '%'")
    fun findByName(name: String): List<Pet>


}