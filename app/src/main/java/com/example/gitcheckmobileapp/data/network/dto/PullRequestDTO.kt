package com.example.gitcheckmobileapp.data.network.dto

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.ui.utils.toReadbleDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.Int

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class PullRequestDTO(
    @SerialName("title")
    val title: String,
    @SerialName("number")
    val number: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("user")
    val user: UsersDTO,
    @SerialName("author_association")
    val authorAssociation: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun PullRequestDTO.toPullRequest(): PullRequest {
    return PullRequest(
        title = this.title,
        number = this.number,
        createdAt = this.createdAt.toReadbleDate(),
        user = this.user,
        authorAssociation = this.authorAssociation
    )
}