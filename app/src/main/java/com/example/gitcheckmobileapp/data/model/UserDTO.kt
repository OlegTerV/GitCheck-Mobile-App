package com.example.gitcheckmobileapp.data.model

import kotlinx.serialization.Serializable

data class UserDTO(
    val id: Long,
    val name: String,
    val photo: String,
)