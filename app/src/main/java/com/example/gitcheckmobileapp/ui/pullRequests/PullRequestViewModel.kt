package com.example.gitcheckmobileapp.ui.pullRequests

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.network.dto.UsersDTO
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.PullRequestsScreen
import com.example.gitcheckmobileapp.navigation.RepoInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Immutable
data class PullRequestsUiState(
    val isLoading: Boolean = false,
    val repoName: String = "",
    val pullRequests: List<PullRequest> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class PullRequestViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val repoOwner = savedStateHandle.toRoute<PullRequestsScreen>().ownerName
    private val repoName = savedStateHandle.toRoute<PullRequestsScreen>().repoName
    private val repoId = savedStateHandle.toRoute<PullRequestsScreen>().repoId

    private val _refreshDataError = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(PullRequestsUiState(repoName = repoName))
    val uiState = _uiState.asStateFlow()

    /*
    init {
        _uiState.update { it.copy(isLoading = true, repoName = repoName) }
        viewModelScope.launch {
            try {
                val result = networkRepository.getPullRequests(repoOwner, repoName)
                when (result) {
                    is Resource.Success ->
                        _uiState.update { it.copy(
                            pullRequests = result.data,
                            errorMessage = null
                        ) }
                    is Resource.Error ->
                        _uiState.update { it.copy(
                            errorMessage = result.message
                        ) }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
*/

    init{
        viewModelScope.launch{
            networkRepository.getPullRequests(repoId)
                .combine(_refreshDataError.asStateFlow()) {dbItems, error -> dbItems to error}
                .collect{ (dbItems, error) ->
                    _uiState.update{it.copy(
                        pullRequests = dbItems.map{
                            PullRequest(
                                title = it.title,
                                number = it.number,
                                createdAt = it.createdAt,
                                user = UsersDTO(repoOwner, it.userId),
                                authorAssociation = it.authorAssociation
                            )},
                        errorMessage = error
                    )}
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        _refreshDataError.value = null
        viewModelScope.launch{
            try {
                networkRepository.refreshPullRequests(repoOwner, repoName, repoId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _refreshDataError.value = "Ошибка сети. " + e.message
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeErrorMessage(){
        _uiState.update { it.copy(errorMessage = null) }
    }
}