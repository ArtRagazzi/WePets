package com.example.wepets.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "revenue")
data class Revenue (
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val name:String,
    val date:Date,
    val value:Double,
    val type:String,
) : Parcelable