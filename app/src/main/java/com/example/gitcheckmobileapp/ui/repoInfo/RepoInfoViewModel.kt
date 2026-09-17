package com.example.gitcheckmobileapp.ui.repoInfo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.R
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
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
data class RepoInfoUiState(
    val isLoading: Boolean = false,
    val starButtonText: String = "Add Star",
    val starButtonIcon: Int = R.drawable.ic_star_shine,
    val subscribeButtonText: String = "Subscribe",
    val subscribeButtonIcon: ImageVector = Icons.Outlined.Visibility,
    val aboutRepoModal: Boolean = false,
    val readmeRepoModal: Boolean = false,
    val about: String = "",
    val readme: String = "",
    val repoName: String = "",
    val errorMessage: String? = null
)

@HiltViewModel
class RepoInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository
): ViewModel(){
    private val repoOwner = savedStateHandle.toRoute<RepoInfoScreen>().ownerName
    private val repoName = savedStateHandle.toRoute<RepoInfoScreen>().repoName
    private val repoId = savedStateHandle.toRoute<RepoInfoScreen>().repoId
    private val repoAbout = savedStateHandle.toRoute<RepoInfoScreen>().repoAbout

    private val _refreshReadmeError = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(RepoInfoUiState(repoName = repoName, about = repoAbout))
    val uiState = _uiState.asStateFlow()

    fun changeStarState() {
        _uiState.update { it->
            it.copy(
                starButtonText = if (it.starButtonText == "Delete star") "Add star" else "Delete star",
                starButtonIcon = if (it.starButtonText == "Delete star") R.drawable.ic_star_shine else R.drawable.ic_star
            )
        }
    }

    fun changeSubscribeState() {
        _uiState.update { it ->
            it.copy(
                subscribeButtonText = if (it.subscribeButtonText == "Unsubscribe") "Subscribe" else "Unsubscribe",
                subscribeButtonIcon = if (it.subscribeButtonText == "Unsubscribe") Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            )
        }
    }

    fun changeStateAboutModal() {
        _uiState.update { it ->
            it.copy(
                aboutRepoModal = !it.aboutRepoModal
            )
        }
    }

    fun changeStateReadmeModal() {
        _uiState.update { it ->
            it.copy(
                readmeRepoModal = !it.readmeRepoModal
            )
        }
    }

    fun getAllRepoData(): Triple<String, String, Long> {
        return Triple(repoOwner, repoName, repoId)
    }

/*
    init {
        val repoData: Repository = Json.decodeFromString<Repository>(repoDataStringJson)
        _uiState.update { it.copy(isLoading = true, repoName = repoData.name) }

        viewModelScope.launch {
            try {
                val readmeData = networkRepository.loadReadme(repoData.owner.login, repoData.name)
                when (readmeData) {
                    is Resource.Success ->
                        _uiState.update { it.copy(readme = readmeData.data.decodedContent) }
                    is Resource.Error ->
                        _uiState.update { it.copy(readme = "Непредвиденная ошибка при загрузке файла") }
                }
            }
            finally {
                _uiState.update { it.copy(isLoading = false, about = repoData.description.takeIf { !it.isBlank()} ?: "Нет описания") }
            }
        }
    }
    */

    init{
        viewModelScope.launch {
            networkRepository.getReadme(repoId)
            .combine(
                _refreshReadmeError.asStateFlow()
            ) {dbItem, error -> dbItem to error}
            .collect { (dbItem, error) ->
                _uiState.update{ it.copy(
                    readme = dbItem?.decodedContent ?: "Непредвиденная ошибка при загрузке файла",
                    errorMessage = error
                )}
            }
        }
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        _refreshReadmeError.value = null
        viewModelScope.launch{
            try {
                networkRepository.refreshReadme(repoOwner, repoName, repoId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _refreshReadmeError.value = "Ошибка сети. " + e.message
            } finally {
                _uiState.update{it.copy(isLoading = false)}
            }
        }
    }
}