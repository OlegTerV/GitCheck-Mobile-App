package com.example.gitcheckmobileapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
data class RepoInfoScreen (val repoId: Long)

@Serializable
object SearchScreen

@Serializable
data class UserRepositoriesScreen (val userNickname: String)
