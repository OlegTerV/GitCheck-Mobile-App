package com.example.gitcheckmobileapp.ui.commits

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.CommitsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
    private val repoDataJsonString = savedStateHandle.toRoute<CommitsScreen>().repoData

    private val _uiState = MutableStateFlow(CommitsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val repo: Repository = Json.decodeFromString(repoDataJsonString)
        _uiState.update { it.copy(isLoading = true, repoName = repo.name) }
        viewModelScope.launch {
            try {
                val commitsData = networkRepository.getCommits(repo.owner.login, repo.name)
                when (commitsData) {
                    is Resource.Success ->
                        _uiState.update { it.copy(
                            commits = commitsData.apiData,
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
    }

    fun closeErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}