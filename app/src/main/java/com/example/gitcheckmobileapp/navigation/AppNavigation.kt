package com.example.gitcheckmobileapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.gitcheckmobileapp.ui.home.HomeScreen
import com.example.gitcheckmobileapp.ui.home.HomeViewModel
import com.example.gitcheckmobileapp.ui.repoInfo.RepoInfoScreen
import com.example.gitcheckmobileapp.ui.repoInfo.RepoInfoViewModel
import com.example.gitcheckmobileapp.ui.search.SearchScreen
import com.example.gitcheckmobileapp.ui.search.SearchViewModel
import com.example.gitcheckmobileapp.ui.userRepos.UserRepositoriesViewModel
import com.example.gitcheckmobileapp.ui.userRepos.UserRepositoriesScreen
import com.google.android.gms.common.zzq

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        homeGraph(navController)
        repoInfoGraph(navController)
        searchGraph(navController)
        userRepositoriesGraph((navController))
    }
}

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable<HomeScreen> {
    val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(
            viewModel = viewModel,
            onItemClick = { navController.navigate(SearchScreen) }
        )
    }
}

fun NavGraphBuilder.repoInfoGraph(navController: NavController) {
    composable<RepoInfoScreen>{ backStackEntry ->
        val viewModel: RepoInfoViewModel = hiltViewModel()
        RepoInfoScreen(
            viewModel = viewModel,
            onBackClick = {navController.popBackStack()}
        )
    }
}

fun NavGraphBuilder.searchGraph(navController: NavController) {
    composable<SearchScreen> {
        val viewModel: SearchViewModel = hiltViewModel()
        SearchScreen(
            viewModel = viewModel,
            onItemClick = { userNickname ->
                navController.navigate(UserRepositoriesScreen(userNickname)) },
            onBackClick = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.userRepositoriesGraph(navController: NavController) {
    composable<UserRepositoriesScreen> { backStackEntry ->
        val viewModel: UserRepositoriesViewModel = hiltViewModel()
        UserRepositoriesScreen(
            viewModel = viewModel,
            onItemClick = { repoId ->
                navController.navigate(RepoInfoScreen(repoId)) },
            onBackClick = { navController.popBackStack() }
        )
    }
}