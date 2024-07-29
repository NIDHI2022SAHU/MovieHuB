package com.example.moviehub.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviehub.utilities.Constants.Companion.USER_TABLE

@Entity(tableName =USER_TABLE)
data class Users (
        @PrimaryKey(autoGenerate = true)
        var id:Int=0,
        var name:String,
        var email:String,
        var phoneNum: String,
        var password: String
        )
