package com.example.gitcheckmobileapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gitcheckmobileapp.ui.commits.CommitsView
import com.example.gitcheckmobileapp.ui.commits.CommitsViewModel
import com.example.gitcheckmobileapp.ui.home.HomeScreen
import com.example.gitcheckmobileapp.ui.home.HomeViewModel
import com.example.gitcheckmobileapp.ui.pullRequests.PullRequestView
import com.example.gitcheckmobileapp.ui.pullRequests.PullRequestViewModel
import com.example.gitcheckmobileapp.ui.repoInfo.RepoInfoScreen
import com.example.gitcheckmobileapp.ui.repoInfo.RepoInfoViewModel
import com.example.gitcheckmobileapp.ui.search.SearchScreen
import com.example.gitcheckmobileapp.ui.search.SearchViewModel
import com.example.gitcheckmobileapp.ui.userRepos.UserRepositoriesViewModel
import com.example.gitcheckmobileapp.ui.userRepos.UserRepositoriesScreen

@RequiresApi(Build.VERSION_CODES.O)
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
        userRepositoriesGraph(navController)
        commitsGraph(navController)
        pullRequestsGraph(navController)
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
            checkCommits = {ownerName, repoName, repoId ->
                navController.navigate(CommitsScreen(ownerName, repoName, repoId))
            },
            checkPullRequests = {ownerName, repoName, repoId ->
                navController.navigate(PullRequestsScreen(ownerName, repoName, repoId))
            },
            onBackClick = {navController.popBackStack()}
        )
    }
}

fun NavGraphBuilder.searchGraph(navController: NavController) {
    composable<SearchScreen> {
        val viewModel: SearchViewModel = hiltViewModel()
        SearchScreen(
            viewModel = viewModel,
            onItemClick = { userId, userName ->
                navController.navigate(UserRepositoriesScreen(userId, userName)) },
            onBackClick = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.userRepositoriesGraph(navController: NavController) {
    composable<UserRepositoriesScreen> { backStackEntry ->
        val viewModel: UserRepositoriesViewModel = hiltViewModel()
        UserRepositoriesScreen(
            viewModel = viewModel,
            onItemClick = { ownerName, repoName, repoId, repoAbout ->
                navController.navigate(RepoInfoScreen(
                    ownerName = ownerName,
                    repoName = repoName,
                    repoId = repoId,
                    repoAbout = repoAbout)) },
            onBackClick = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.commitsGraph(navController: NavController){
    composable<CommitsScreen> {
        val viewModel: CommitsViewModel = hiltViewModel()
        CommitsView(
            viewModel = viewModel,
            onBackItem = { navController.popBackStack() }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.pullRequestsGraph(navController: NavController){
    composable<PullRequestsScreen> {
        val viewModel: PullRequestViewModel = hiltViewModel()
        PullRequestView(
            viewModel = viewModel,
            onBackItem = { navController.popBackStack() }
        )
    }
}