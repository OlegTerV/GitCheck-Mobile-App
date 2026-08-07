package com.example.gitcheckmobileapp.ui.repoInfo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.R
import com.example.gitcheckmobileapp.navigation.RepoInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RepoInfoUiState(
    val isLoading: Boolean = false,
    val starButtonText: String = "",
    val starButtonIcon: Int = R.drawable.ic_star_shine,
    val subscribeButtonText: String = "",
    val subscribeButtonIcon: ImageVector = Icons.Outlined.Visibility,
    val aboutRepoModal: Boolean = false,
    val readmeRepoModal: Boolean = false
)

@HiltViewModel
class RepoInfoViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle): ViewModel(){
    private val repoId = savedStateHandle.toRoute<RepoInfoScreen>().repoId
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

    init {
        _uiState.update { it ->
            it.copy(
                false,
                "Add star",
                R.drawable.ic_star_shine,
                "Subscribe",
                Icons.Outlined.Visibility,
                false,
                false
            )
        }
    }
}