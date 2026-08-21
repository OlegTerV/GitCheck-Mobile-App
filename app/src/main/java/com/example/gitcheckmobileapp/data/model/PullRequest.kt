package com.example.gitcheckmobileapp.data.model

import com.example.gitcheckmobileapp.data.network.dto.UsersDTO
import kotlinx.serialization.SerialName

data class PullRequest(
    val title: String,
    val number: Int,
    val createdAt: String,
    val user: UsersDTO,
    val authorAssociation: String
)