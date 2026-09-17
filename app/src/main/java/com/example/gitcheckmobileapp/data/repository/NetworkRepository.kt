package com.example.gitcheckmobileapp.data.repository

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.ui.window.Dialog
import androidx.room3.ColumnInfo
import com.example.gitcheckmobileapp.data.local.dao.GithubDAO
import com.example.gitcheckmobileapp.data.local.entities.AuthorOrCommitterData
import com.example.gitcheckmobileapp.data.local.entities.CommitEntity
import com.example.gitcheckmobileapp.data.local.entities.PullRequestEntity
import com.example.gitcheckmobileapp.data.local.entities.ReadmeEntity
import com.example.gitcheckmobileapp.data.local.entities.RepositoryEntity
import com.example.gitcheckmobileapp.data.local.entities.UserEntity
import com.example.gitcheckmobileapp.data.model.DataSource
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.data.model.Readme
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.model.User
import com.example.gitcheckmobileapp.data.network.ApiService
import com.example.gitcheckmobileapp.data.network.HttpException
import com.example.gitcheckmobileapp.data.network.dto.CommitDTO
import com.example.gitcheckmobileapp.data.network.dto.CommitsDTO
import com.example.gitcheckmobileapp.data.network.dto.RepositoryDTO
import com.example.gitcheckmobileapp.data.network.dto.FoundUsersDTO
import com.example.gitcheckmobileapp.data.network.dto.PullRequestDTO
import com.example.gitcheckmobileapp.data.network.dto.ReadmeDTO
import com.example.gitcheckmobileapp.data.network.dto.toOneCommit
import com.example.gitcheckmobileapp.data.network.dto.toPullRequest
import com.example.gitcheckmobileapp.data.network.dto.toRepository
import com.example.gitcheckmobileapp.data.network.dto.toUser
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import kotlin.Long
import kotlin.String
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ApiService,
        private val githubDAO: GithubDAO
) {
/*
    suspend fun searchUsers(nickName: String): Resource<List<UsersDTO>> = withContext(Dispatchers.IO){
        try {
            val foundUsers = apiService.searchUser(nickName)
            val users =Json.decodeFromString<FoundUsersDTO>(foundUsers)
            Resource.Success<List<UsersDTO>>(users.items)
        } catch (e: CancellationException) {
            throw e
        }
        catch (e: Exception) {
            Resource.Error(e, e.message ?: "")
        }
    }

    suspend fun loadAllRepos(userNickname: String): Resource<List<AllUserRepositoriesDTO>> = withContext(Dispatchers.IO){
        try {
            val result = apiService.getAllReposForSelectedUser(userNickname)
            val repos = Json.decodeFromString<List<AllUserRepositoriesDTO>>(result)
            Resource.Success<List<AllUserRepositoriesDTO>>(repos)
        } catch (e: CancellationException) {
            throw e
        }
        catch (e: Exception) {
            Resource.Error(e, e.message ?: "")
        }
    }
*/

    suspend fun searchUsers(nickName: String): Resource<List<User>> = withContext(Dispatchers.IO) {
        responseHandler {
            val foundUsers = apiService.getDataFromService("search/users?q=$nickName")
            Json.decodeFromString<FoundUsersDTO>(foundUsers).items.map {it.toUser()}
        }
    }

    suspend fun searchUsersWithDB(nickName: String): Resource<List<User>> = withContext(Dispatchers.IO) {
        var errorMessage = ""
        var foundUsers = responseHandler {
            val foundUsers = apiService.getDataFromService("search/users?q=$nickName")
            Json.decodeFromString<FoundUsersDTO>(foundUsers).items.map { it.toUser() }
        }

        when (foundUsers) {
            is Resource.Success -> return@withContext foundUsers
            is Resource.Error -> errorMessage += foundUsers.message
        }

        val localUsers = githubDAO.getUser(nickName).map { User(it.id, it.login) }

        return@withContext if (localUsers.isEmpty()) {
            Resource.Error(null, "$errorMessage. Нет данных в локальной памяти")
        } else {
            Resource.Success(
                githubDAO.getUser(nickName).map { User(it.id, it.login) },
                DataSource.Db
            )
        }
    }

    suspend fun loadAllRepos(userNickname: String): Resource<List<Repository>> = withContext(Dispatchers.IO){
        responseHandler {
            val result = apiService.getDataFromService("users/$userNickname/repos")
            Json.decodeFromString<List<RepositoryDTO>>(result).map {it.toRepository()}
        }
    }

    fun getRepos(userId: Long): Flow<List<RepositoryEntity>>{
        return githubDAO.getReposOfTheUser(userId)
    }
    suspend fun refreshRepos(userNickname: String) = withContext(Dispatchers.IO) {
        val repos = apiService.getDataFromService("users/$userNickname/repos")
        val data = Json.decodeFromString<List<RepositoryDTO>>(repos).map { it.toRepository() }
        githubDAO.saveUserWithRepositories(
            UserEntity(data[0].owner.id, data[0].owner.login),
            data.map {
                RepositoryEntity(
                    id = it.id,
                    name = it.name,
                    ownerName = it.owner.login,
                    ownerId = it.owner.id,
                    description = it.description,
                    starsCount = it.starsCount,
                    watchersCount = it.watchersCount,
                    forksCount = it.forksCount
                )
            }
        )
    }

    fun getReadme(repoId: Long): Flow<ReadmeEntity?> {
        return githubDAO.getReadmeOfTheRepo(repoId)
    }
    suspend fun refreshReadme(userNickname: String, repoName: String, repoId: Long) = withContext(
        Dispatchers.IO) {
        val data = apiService.getDataFromService("repos/$userNickname/$repoName/readme")
        val readmeApi = Json.decodeFromString<ReadmeDTO>(data)
        val decodedContent = Base64.decode(readmeApi.content, Base64.DEFAULT).decodeToString()
        githubDAO.insertReadme(ReadmeEntity(
            repoId = repoId,
            name = readmeApi.name,
            content = readmeApi.content,
            decodedContent = decodedContent,
            encoding = readmeApi.encoding
        ))
    }

    fun getPullRequests(repoId: Long): Flow<List<PullRequestEntity>> {
        return githubDAO.getPullRequestsOfTheRepo(repoId)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshPullRequests(userNickname: String, repoName: String, repoId: Long) = withContext(Dispatchers.IO) {
        val data = apiService.getDataFromService("repos/$userNickname/$repoName/pulls")
        val pullRequests = Json.decodeFromString<List<PullRequestDTO>>(data)
            .map{it.toPullRequest()}
            .map{
                PullRequestEntity(
                    repoId = repoId,
                    title = it.title,
                    number = it.number,
                    createdAt = it.createdAt,
                    userId = it.user.id,
                    authorAssociation = it.authorAssociation
            ) }
        githubDAO.insertPullRequests(pullRequests)
    }

    fun getCommits(repoId: Long): Flow<List<CommitEntity>>{
        return githubDAO.getCommitsOfTheRepo(repoId)
    }
    suspend fun refreshCommits(userNickname: String, repoName: String, repoId: Long) = withContext(Dispatchers.IO) {
        val data = apiService.getDataFromService("repos/$userNickname/$repoName/commits")
        val commits = Json.decodeFromString<List<CommitsDTO>>(data)
            .map{it.commit.toOneCommit()}
            .map{
                CommitEntity(
                    repoId = repoId,
                    author = AuthorOrCommitterData(
                        name = it.author.name,
                        email = it.author.email,
                        date = it.author.date
                    ),
                    committer = AuthorOrCommitterData(
                        name = it.committer.name,
                        email = it.committer.email,
                        date = it.committer.date
                    ),
                    message = it.message
                )
            }
        githubDAO.insertCommits(commits)
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun loadReadme(userNickname: String, repoName: String): Resource<Readme> = withContext(Dispatchers.IO){
        responseHandler {
            val result = apiService.getDataFromService("repos/$userNickname/$repoName/readme")
            val readmeApi = Json.decodeFromString<ReadmeDTO>(result)
            val decodedContent = Base64.decode(readmeApi.content, Base64.DEFAULT).decodeToString()
            Readme(
                name = readmeApi.name,
                content = readmeApi.content,
                decodedContent = decodedContent,
                encoding = readmeApi.encoding
            )
        }
    }

    suspend fun getCommits(userNickname: String, repoName: String): Resource<List<OneCommit>> = withContext(Dispatchers.IO) {
        responseHandler{
            val result = apiService.getDataFromService("repos/$userNickname/$repoName/commits")
            Json.decodeFromString<List<CommitsDTO>>(result).map{ it.commit.toOneCommit()}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPullRequests(userNickname: String, repoName: String): Resource<List<PullRequest>> = withContext(Dispatchers.IO) {
        responseHandler {
            val result = apiService.getDataFromService("repos/$userNickname/$repoName/pulls")
            Json.decodeFromString<List<PullRequestDTO>>(result). map {it.toPullRequest()}
        }
    }

    private inline fun <T> responseHandler(block: () -> T): Resource<T> {
        return try {
            Resource.Success<T>(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e, e.message ?: "")
        }
    }
}