package com.example.gitcheckmobileapp.data.network.dto

import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class RepositoryDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("owner")
    val owner: UsersDTO,
    @SerialName("description")
    val description: String?,
    @SerialName("stargazers_count")
    val starsCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int
)

fun RepositoryDTO.toRepository(): Repository {
    return Repository(
        id = this.id,
        name = this.name,
        owner = User(this.owner.id, this.owner.login),
        description = this.description ?: "",
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount
    )
}