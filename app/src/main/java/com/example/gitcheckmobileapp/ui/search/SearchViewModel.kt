package com.example.gitcheckmobileapp.ui.search

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.model.User
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
data class SearchUiState(
    val searchQuery: String ="",
    val searchState: Boolean = false,
    val usersList: List<User> = emptyList<User>(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    private val _searchQuery = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    init {
        trackSearchQueryChanges()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun trackSearchQueryChanges(){
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .onEach {
                    _uiState.update { it ->
                        it.copy(isLoading = true)
                    } }
                .flatMapLatest { it ->
                    if (it.isNotBlank()) {
                        flow{ emit(networkRepository.searchUsers(it))}
                    } else {
                        flow{ emit(Resource.Success(emptyList()))}
                    }
                }
                .collect { apiResponse ->
                    when (apiResponse) {
                        is Resource.Success -> {
                            _uiState.update { it ->
                                it.copy(
                                    usersList = apiResponse.apiData,
                                    errorMessage = null,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update { it ->
                                it.copy(
                                    usersList = emptyList(),
                                    errorMessage = apiResponse.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    fun changeSearchQuery(incomingSearchQuery: String){
        _uiState.update { it ->
            it.copy(searchQuery = incomingSearchQuery)
        }
        _searchQuery.update {it -> incomingSearchQuery }
    }

    fun changeSearchState(incomingSearchSate: Boolean) {
        _uiState.update { it ->
            it.copy(searchState = incomingSearchSate)
        }
    }

    fun closeErrorMessage(){
        _uiState.update { it ->
            it.copy(errorMessage = null)
        }
    }

    fun sendErrorMessage(message: String){
        _uiState.update { it ->
            it.copy(errorMessage = message)
        }
    }
}