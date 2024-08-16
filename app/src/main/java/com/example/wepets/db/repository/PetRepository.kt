package com.example.wepets.db.repository

import com.example.wepets.db.dao.PetDao
import com.example.wepets.model.Pet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetRepository (private val petDao: PetDao){

    fun insertPet(pet:Pet){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.insert(pet)
        }
    }

    fun updatePet(pet:Pet){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.update(pet)
        }
    }

    fun deletePet(pet:Pet){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.delete(pet)
        }
    }

    fun findById(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            petDao.findById(id)
        }
    }

    fun findAll(callback: (List<Pet>)->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val pets = petDao.findAll()
            withContext(Dispatchers.Main){
                callback(pets)
            }
        }
    }
}