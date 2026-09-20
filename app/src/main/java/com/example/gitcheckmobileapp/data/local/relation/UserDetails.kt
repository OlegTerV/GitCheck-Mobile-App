package com.example.gitcheckmobileapp.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.gitcheckmobileapp.data.local.entities.RepositoryEntity
import com.example.gitcheckmobileapp.data.local.entities.UserEntity

data class UserDetails(
    @Embedded
    val user: UserEntity,

    @Relation(
        entity = RepositoryEntity::class,
        parentColumns = ["userId"],
        entityColumns = ["repoId"]
    )
    val repos: List<RepositoryDetails>
)