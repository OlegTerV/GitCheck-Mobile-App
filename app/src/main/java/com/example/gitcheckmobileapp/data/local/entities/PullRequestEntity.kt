package com.example.gitcheckmobileapp.data.local.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.example.gitcheckmobileapp.data.network.dto.UsersDTO

@Entity(
    tableName = "PullRequests",
    foreignKeys = [
        ForeignKey(
            entity = RepositoryEntity::class,
            parentColumns = ["repoId"],
            childColumns = ["repoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PullRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val repoId: Long,
    val title: String,
    val number: Int,
    val createdAt: String,
    val userId: Long,
    val authorAssociation: String
)