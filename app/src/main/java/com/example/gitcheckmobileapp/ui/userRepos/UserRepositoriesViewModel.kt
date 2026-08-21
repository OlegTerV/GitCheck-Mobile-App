package com.example.gitcheckmobileapp.ui.userRepos

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.UserRepositoriesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class UserRepositoriesUiState(
    val repos: List<Repository> = emptyList<Repository>(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val userNickname: String? = null
)

@HiltViewModel
class UserRepositoriesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository
): ViewModel( ){
    private val userNickname = savedStateHandle.toRoute<UserRepositoriesScreen>().userNickname
    private val _uiState = MutableStateFlow(UserRepositoriesUiState(userNickname = userNickname))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val dataReposResult = networkRepository.loadAllRepos(userNickname)
                when (dataReposResult) {
                    is Resource.Success ->
                        _uiState.update { it.copy(repos = dataReposResult.apiData) }
                    is Resource.Error ->
                        _uiState.update{ it.copy(errorMessage = dataReposResult.message) }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeErrorMessage(){
        _uiState.update { it ->
            it.copy(errorMessage = null)
        }
    }
}