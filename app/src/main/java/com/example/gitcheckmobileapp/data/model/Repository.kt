package com.example.gitcheckmobileapp.data.model

data class Repository(
    val id: Long,
    val name: String,
    val description: String,
    val readme: String? = null,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int
)