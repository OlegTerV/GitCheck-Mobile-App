package com.example.gitcheckmobileapp.data.local.entities

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.User

@Entity(
    tableName = "Repositories",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RepositoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "repoId")
    val id: Long,

    @ColumnInfo(name = "repoName")
    val name: String,
    val ownerName: String,
    val ownerId: Long,
    val description: String,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int
)

fun RepositoryEntity.toRepository(): Repository{
    return Repository(
        id = this.id,
        name = this.name,
        owner = User(this.ownerId, this.ownerName),
        description = this.description,
        starsCount = this.starsCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount
    )
}

