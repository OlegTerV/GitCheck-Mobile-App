package com.example.gitcheckmobileapp.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.gitcheckmobileapp.data.local.dao.GithubDAO
import com.example.gitcheckmobileapp.data.local.entities.CommitEntity
import com.example.gitcheckmobileapp.data.local.entities.PullRequestEntity
import com.example.gitcheckmobileapp.data.local.entities.ReadmeEntity
import com.example.gitcheckmobileapp.data.local.entities.RepositoryEntity
import com.example.gitcheckmobileapp.data.local.entities.UserEntity

@Database(
    entities = [
        CommitEntity::class,
        PullRequestEntity::class,
        ReadmeEntity::class,
        RepositoryEntity::class,
        UserEntity::class,
    ],
    version = 2)
abstract class AppDataBase: RoomDatabase() {
    abstract fun githubDao(): GithubDAO
}