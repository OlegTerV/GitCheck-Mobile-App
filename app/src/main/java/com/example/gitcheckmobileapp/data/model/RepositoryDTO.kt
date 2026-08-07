package com.example.gitcheckmobileapp.data.model
import kotlinx.serialization.Serializable

data class RepositoryDTO(
    val id: Long,
    val name: String,
    val desc: String,
    val readme: String
)