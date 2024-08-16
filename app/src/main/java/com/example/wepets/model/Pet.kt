package com.example.wepets.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "pet")
data class Pet(

    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val namePet:String,
    val size:String,
    val breed:String,
    val sexPet:String,
    val ownerName:String,
    val phoneNumber:String,
    val photoUrl:String?

): Parcelable