package com.example.gitcheckmobileapp.data.model

import androidx.room3.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val id: Long,
    val name: String,
    val owner: User,
    val description: String = "",
    val readme: String? = null,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int
)