package com.example.gitcheckmobileapp.data.local.entities

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity (tableName = "Users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val id: Long,

    @ColumnInfo(name = "userNickname")
    val login: String,
)