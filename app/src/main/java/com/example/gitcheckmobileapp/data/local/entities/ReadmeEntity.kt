package com.example.gitcheckmobileapp.data.local.entities

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey

@Entity(
    tableName = "Readme",
    foreignKeys = [
        ForeignKey(
            entity = RepositoryEntity::class,
            parentColumns = ["repoId"],
            childColumns = ["repoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReadmeEntity(
    @PrimaryKey
    @ColumnInfo(name = "repoId")
    val repoId: Long,
    val name: String,
    val content: String,
    val decodedContent: String,
    val encoding: String
)