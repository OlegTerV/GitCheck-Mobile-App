package com.example.gitcheckmobileapp.ui.commits

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.model.AuthorOrCommitter
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.CommitsScreen
import com.example.gitcheckmobileapp.navigation.RepoInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException

@Immutable
data class CommitsUiState(
    val isLoading: Boolean = false,
    val repoName: String = "",
    val commits: List<OneCommit> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class CommitsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository
) : ViewModel () {
    private val repoOwner = savedStateHandle.toRoute<CommitsScreen>().ownerName
    private val repoName = savedStateHandle.toRoute<CommitsScreen>().repoName
    private val repoId = savedStateHandle.toRoute<CommitsScreen>().repoId

    private val _refreshError = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(CommitsUiState(repoName = repoName))
    val uiState = _uiState.asStateFlow()

    /*
    init {
        _uiState.update { it.copy(isLoading = true, repoName = repoName) }
        viewModelScope.launch {
            try {
                val commitsData = networkRepository.getCommits(repoOwner, repoName)
                when (commitsData) {
                    is Resource.Success ->
                        _uiState.update { it.copy(
                            commits = commitsData.data,
                            errorMessage = null
                        ) }
                    is Resource.Error ->
                        _uiState.update { it.copy(
                            errorMessage = commitsData.message
                        ) }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }*/

    init {
        viewModelScope.launch{
            networkRepository.getCommits(repoId)
            .combine(_refreshError.asStateFlow()) {dbItems, error -> dbItems to error}
            .collect{ (dbItems, error) ->
                _uiState.update{it.copy(
                    commits = dbItems.map {
                        OneCommit(
                            author =  AuthorOrCommitter(
                                name = it.author.name,
                                email = it.author.email,
                                date = it.author.date
                            ),
                            committer = AuthorOrCommitter(
                                name = it.committer.name,
                                email = it.committer.email,
                                date = it.committer.date
                            ),
                            message = it.message
                        )
                    },
                    errorMessage = error
                )}
            }
        }
    }

    fun loadData(){
        _uiState.update{it.copy(isLoading = true)}
        _refreshError.value=null
        viewModelScope.launch{
            try {
                networkRepository.refreshCommits(repoOwner, repoName, repoId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _refreshError.value = "Ошибка сети. " + e.message
            } finally {
                _uiState.update{it.copy(isLoading = false)}
            }
        }
    }

    fun closeErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}