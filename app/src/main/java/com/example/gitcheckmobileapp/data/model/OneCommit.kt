package com.example.gitcheckmobileapp.data.model

import com.example.gitcheckmobileapp.data.network.dto.AuthorOrCommitterDTO
import kotlinx.serialization.SerialName

data class OneCommit(
    val author: AuthorOrCommitter,
    val committer: AuthorOrCommitter,
    val message: String
)

data class AuthorOrCommitter(
    val name: String,
    val email: String,
    val date: String
)