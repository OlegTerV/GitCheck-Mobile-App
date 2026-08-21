package com.example.gitcheckmobileapp.ui.pullRequests

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.PullRequestsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val repoDataJsonString = savedStateHandle.toRoute<PullRequestsScreen>().repoData

    private val _uiState = MutableStateFlow(PullRequestsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val repoData = Json.decodeFromString<Repository>(repoDataJsonString)

        _uiState.update { it.copy(isLoading = true, repoName = repoData.name) }
        viewModelScope.launch {
            try {
                val result = networkRepository.getPullRequests(repoData.owner.login, repoData.name)
                when (result) {
                    is Resource.Success ->
                        _uiState.update { it.copy(
                            pullRequests = result.apiData,
                            errorMessage = null
                        ) }
                    is Resource.Error ->
                        _uiState.update { it.copy(
                            errorMessage = result.message
                        ) } //TODO выдать сообщение на экран
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeErrorMessage(){
        _uiState.update { it.copy(
            errorMessage = null
        ) }
    }
}