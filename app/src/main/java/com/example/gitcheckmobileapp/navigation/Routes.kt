package com.example.gitcheckmobileapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
data class RepoInfoScreen (val ownerName: String, val repoName: String, val repoId: Long, val repoAbout: String)

@Serializable
object SearchScreen

@Serializable
data class UserRepositoriesScreen (val userId: Long, val userName: String)

@Serializable
data class CommitsScreen (val ownerName: String, val repoName: String, val repoId: Long)

@Serializable
data class PullRequestsScreen (val ownerName: String, val repoName: String, val repoId: Long)