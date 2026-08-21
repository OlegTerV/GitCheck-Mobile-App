package com.example.gitcheckmobileapp.ui.pullRequests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey
import com.example.gitcheckmobileapp.ui.theme.TopBarBackgroundColor
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.data.model.PullRequest
import com.example.gitcheckmobileapp.ui.core.TitleAndTextRow
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import java.nio.file.WatchEvent

@Composable
fun PullRequestView(
    viewModel: PullRequestViewModel,
    onBackItem: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalWindowInfo.current.containerSize
    val width = configuration.width
    val height = configuration.height
    val snackbarHost = remember{ SnackbarHostState() }

    Scaffold(
        topBar = {
            TopBar(
            title = uiState.repoName,
            onBackClick = onBackItem
        )},
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) {innerPadding ->
        if (uiState.isLoading) {
            Box (
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size((width / 6).dp),
                    color = MaterialTheme.colorScheme.TopBarBackgroundColor,
                    trackColor = MaterialTheme.colorScheme.TextColorLightGrey,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + (height * 0.01).dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = (width * 0.01).dp,
                    end = (width * 0.01).dp
                ),
                verticalArrangement = Arrangement.spacedBy((height * 0.007).dp)
            ) {
                items(uiState.pullRequests){ currentPullRequest ->
                    PullRequestCard(currentPullRequest, height, width)
                }
            }
        }
        LaunchedEffect(uiState.errorMessage) {
            uiState.errorMessage?.let{
                snackbarHost.showSnackbar(
                    message = uiState.errorMessage ?: "Something error",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                viewModel.closeErrorMessage()
            }
        }
    }
}

@Composable
fun PullRequestCard(
    currentPullRequest: PullRequest,
    screenHeight: Int,
    screenWidth: Int,
) {
    val dividerPaddings = screenWidth * 0.02
    val cardContentPaddings =  screenWidth * 0.02
    val titlePaddings = screenHeight * 0.005

    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors= CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.DefaultButtons
        )
    ) {
        Spacer(modifier = Modifier.height(titlePaddings.dp))
        TitleAndTextRow("Title: ", currentPullRequest.title, dividerPaddings, 0.0)
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            modifier = Modifier.padding(
                start = dividerPaddings.dp,
                end = dividerPaddings.dp
            ),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "#${currentPullRequest.number} opened on ${currentPullRequest.createdAt} by ${currentPullRequest.user.login} (${currentPullRequest.authorAssociation})",
            color = MaterialTheme.colorScheme.TextColorBlack,
            fontSize = 20.sp,
            modifier = Modifier.padding(
                start = dividerPaddings.dp,
                end = dividerPaddings.dp
            )
        )
        Spacer(modifier = Modifier.height(titlePaddings.dp))

    }
}