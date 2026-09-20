package com.example.gitcheckmobileapp.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.example.gitcheckmobileapp.data.local.entities.CommitEntity
import com.example.gitcheckmobileapp.data.local.entities.PullRequestEntity
import com.example.gitcheckmobileapp.data.local.entities.ReadmeEntity
import com.example.gitcheckmobileapp.data.local.entities.RepositoryEntity
import com.example.gitcheckmobileapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDAO {
    @Upsert
    suspend fun insertUser(user: UserEntity)

    @Upsert
    suspend fun insertRepositories(repos: List<RepositoryEntity>)

    @Upsert
    suspend fun insertCommits(commits: List<CommitEntity>)

    @Upsert
    suspend fun insertReadme(readme: ReadmeEntity)

    @Upsert
    suspend fun insertPullRequests(pullRequests: List<PullRequestEntity>)

    @Transaction
    suspend fun saveUserWithRepositories(user: UserEntity, repos: List<RepositoryEntity>){
        insertUser(user)
        insertRepositories(repos)
    }

    @Query("SELECT * FROM Users WHERE userNickname LIKE :userNickname || '%'")
    suspend fun getUser(userNickname: String): List<UserEntity>

    @Query("SELECT * FROM Repositories WHERE ownerId = :userId")
    fun getReposOfTheUser(userId: Long): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM Readme WHERE repoId = :repoId")
    fun getReadmeOfTheRepo(repoId: Long): Flow<ReadmeEntity?>

    @Query("SELECT * FROM PullRequests WHERE repoId = :repoId")
    fun getPullRequestsOfTheRepo(repoId: Long): Flow<List<PullRequestEntity>>

    @Query("SELECT * FROM Commits WHERE repoId = :repoId")
    fun getCommitsOfTheRepo(repoId: Long): Flow<List<CommitEntity>>
}