package com.example.gitcheckmobileapp.data.network.dto

import com.example.gitcheckmobileapp.data.model.AuthorOrCommitter
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.ui.utils.toReadbleDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import java.text.SimpleDateFormat
import java.time.ZoneOffset.UTC
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CommitsDTO(
    @SerialName("commit")
    val commit: CommitDTO
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CommitDTO(
    @SerialName("author")
    val author: AuthorOrCommitterDTO,
    @SerialName("committer")
    val committer: AuthorOrCommitterDTO,
    @SerialName("message")
    val message: String
)

@Serializable
data class AuthorOrCommitterDTO(
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("date")
    val date: String
)

fun CommitDTO.toOneCommit(): OneCommit{
    val authorModel = AuthorOrCommitter(
        name = this.author.name,
        email = this.author.email,
        date = this.author.date.toReadbleDate(),
    )

    val committerModel = AuthorOrCommitter(
        name = this.committer.name,
        email = this.committer.email,
        date = this.committer.date.toReadbleDate(),
    )

    return OneCommit(
        author = authorModel,
        committer = committerModel,
        message = this.message
    )
}