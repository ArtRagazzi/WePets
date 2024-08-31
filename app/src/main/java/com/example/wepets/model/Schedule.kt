package com.example.wepets.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val customerId :Int,
    val date:String,
    val time:String,
    val typeWork:String,
    val img:Int,
    val value:Double

): Parcelable
