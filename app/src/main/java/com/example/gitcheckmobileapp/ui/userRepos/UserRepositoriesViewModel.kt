package com.example.gitcheckmobileapp.ui.userRepos

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.data.local.entities.toRepository
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.UserRepositoriesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    private val userNickname = savedStateHandle.toRoute<UserRepositoriesScreen>().userName
    private val userId = savedStateHandle.toRoute<UserRepositoriesScreen>().userId

    private val _refreshError = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(UserRepositoriesUiState(userNickname = userNickname))
    /*
    private val _uiState: StateFlow<UserRepositoriesUiState> = networkRepository.getRepos(userId).
        combine(
            _refreshError.asStateFlow(),
        ) { dbItems, error ->
            UserRepositoriesUiState(
                userNickname = userNickname,
                isLoading = _isLoading.value,
                repos = dbItems.map{ it.toRepository() },
                errorMessage = error
            )
        }
        .flowOn(Dispatchers.Default)
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserRepositoriesUiState(userNickname = userNickname, isLoading = true)
        )*/

    val uiState = _uiState

/*
    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val dataReposResult = networkRepository.loadAllRepos(userNickname)
                when (dataReposResult) {
                    is Resource.Success ->
                        _uiState.update { it.copy(repos = dataReposResult.data) }
                    is Resource.Error ->
                        _uiState.update{ it.copy(errorMessage = dataReposResult.message) }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }*/

    init{
        viewModelScope.launch {
            networkRepository.getRepos(userId)
            .combine(_refreshError) {dbItems, error -> dbItems to error}
            .collect { (dbItems, error) ->
                _uiState.update { it.copy(
                    repos = dbItems.map{ it.toRepository() },
                    errorMessage = error
                ) }
            }
        }
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        _refreshError.value = null
        viewModelScope.launch {
            try {
                networkRepository.refreshRepos(userNickname)
            } catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _refreshError.value = "Ошибка при получении данных из сети. " + e.message
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun closeErrorMessage(){
        _uiState.update { it.copy(errorMessage = null) }
    }
}