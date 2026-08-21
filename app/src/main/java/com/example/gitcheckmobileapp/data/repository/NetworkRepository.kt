package com.example.gitcheckmobileapp.data.repository

import android.util.Base64
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.data.model.Readme
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.model.User
import com.example.gitcheckmobileapp.data.network.ApiService
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
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import kotlin.String
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ApiService
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

    suspend fun loadAllRepos(userNickname: String): Resource<List<Repository>> = withContext(Dispatchers.IO){
        responseHandler {
            val result = apiService.getDataFromService("users/$userNickname/repos")
            Json.decodeFromString<List<RepositoryDTO>>(result).map {it.toRepository()}
        }
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