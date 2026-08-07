package com.example.gitcheckmobileapp.ui.userRepos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.navigation.UserRepositoriesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class UserRepositoriesViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle): ViewModel( ){

}