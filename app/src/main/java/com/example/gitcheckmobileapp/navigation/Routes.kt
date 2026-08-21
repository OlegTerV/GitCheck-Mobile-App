package com.example.gitcheckmobileapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
data class RepoInfoScreen (val repoData: String)

@Serializable
object SearchScreen

@Serializable
data class UserRepositoriesScreen (val userNickname: String)

@Serializable
data class CommitsScreen (val repoData: String)

@Serializable
data class PullRequestsScreen (val repoData: String)