package com.example.gitcheckmobileapp.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.gitcheckmobileapp.data.local.entities.CommitEntity
import com.example.gitcheckmobileapp.data.local.entities.PullRequestEntity
import com.example.gitcheckmobileapp.data.local.entities.ReadmeEntity
import com.example.gitcheckmobileapp.data.local.entities.RepositoryEntity

data class RepositoryDetails(
    @Embedded
    val repository: RepositoryEntity,

    @Relation(
        parentColumns = ["repoId"],
        entityColumns = ["repoId"]
    )
    val commits: List<CommitEntity>,

    @Relation(
        parentColumns = ["repoId"],
        entityColumns = ["repoId"]
    )
    val readme: ReadmeEntity,

    @Relation(
        parentColumns = ["repoId"],
        entityColumns = ["repoId"]
    )
    val pullRequests: List<PullRequestEntity>,
)