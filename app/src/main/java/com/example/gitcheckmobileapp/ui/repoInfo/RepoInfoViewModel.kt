package com.example.gitcheckmobileapp.ui.repoInfo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.R
import com.example.gitcheckmobileapp.data.model.Repository
import com.example.gitcheckmobileapp.data.model.Resource
import com.example.gitcheckmobileapp.data.repository.NetworkRepository
import com.example.gitcheckmobileapp.navigation.RepoInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val repoName: String = ""
)

@HiltViewModel
class RepoInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository
): ViewModel(){
    private val repoDataStringJson = savedStateHandle.toRoute<RepoInfoScreen>().repoData

    private val _uiState = MutableStateFlow(RepoInfoUiState())
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

    fun getAllRepoData(): String {
        return repoDataStringJson
    }

    init {
        val repoData: Repository = Json.decodeFromString<Repository>(repoDataStringJson)
        _uiState.update { it.copy(isLoading = true, repoName = repoData.name) }

        viewModelScope.launch {
            try {
                val readmeData = networkRepository.loadReadme(repoData.owner.login, repoData.name)
                when (readmeData) {
                    is Resource.Success ->
                        _uiState.update { it.copy(readme = readmeData.apiData.decodedContent) }
                    is Resource.Error ->
                        _uiState.update { it.copy(readme = "Непредвиденная ошибка при загрузке файла") }
                }
            }
            finally {
                _uiState.update { it.copy(isLoading = false, about = repoData.description.takeIf { !it.isBlank()} ?: "Нет описания") }
            }
        }
    }
}