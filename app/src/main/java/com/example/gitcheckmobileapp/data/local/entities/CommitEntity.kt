package com.example.gitcheckmobileapp.data.local.entities

import androidx.room3.ColumnInfo
import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey

@Entity(
    tableName = "Commits",
    foreignKeys = [
        ForeignKey(
            entity = RepositoryEntity::class,
            parentColumns = ["repoId"],
            childColumns = ["repoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CommitEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "commitId")
    val id: Long = 0,
    val repoId: Long,

    @Embedded(prefix = "author_")
    val author: AuthorOrCommitterData,
    @Embedded(prefix = "committer_")
    val committer: AuthorOrCommitterData,
    val message: String
)

data class AuthorOrCommitterData (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "date")
    val date: String
)