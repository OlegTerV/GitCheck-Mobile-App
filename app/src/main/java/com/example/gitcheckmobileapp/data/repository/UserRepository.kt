package com.example.gitcheckmobileapp.data.repository

import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.network.ApiService
import com.example.gitcheckmobileapp.data.network.HttpException
import com.example.gitcheckmobileapp.data.network.dto.AllUserRepositoriesDTO
import com.example.gitcheckmobileapp.data.network.dto.FoundUsersDTO
import com.example.gitcheckmobileapp.data.network.dto.UsersDTO
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import java.io.IOException

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

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

    suspend fun loadAllRepos(userNickname: String) = withContext(Dispatchers.IO){
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

}