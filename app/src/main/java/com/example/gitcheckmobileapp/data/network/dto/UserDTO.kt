package com.example.gitcheckmobileapp.data.network.dto

import com.example.gitcheckmobileapp.data.model.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class UsersDTO(
    @SerialName("login")
    val login: String,
    @SerialName("id")
    val id: Long
)

fun UsersDTO.toUser(): User {
    return User(
        id = this.id,
        login = this.login
    )
}