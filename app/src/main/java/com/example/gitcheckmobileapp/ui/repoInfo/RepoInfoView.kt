package com.example.gitcheckmobileapp.ui.repoInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Commit
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey
import com.example.gitcheckmobileapp.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.window.Dialog
import com.example.gitcheckmobileapp.ui.theme.MainColor
import com.example.gitcheckmobileapp.ui.theme.TopBarBackgroundColor

@Composable
fun RepoInfoScreen(
    viewModel: RepoInfoViewModel,
    checkCommits: (String, String, Long) -> Unit,
    checkPullRequests: (String, String, Long) -> Unit,
    onBackClick: () -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val screenHeight = configuration.height
    val minSizeCell = screenWidth * 0.17
    val paddingValue = (screenHeight*0.01)
    val repoData = viewModel.getAllRepoData()

    Scaffold(
        topBar = { TopBar(uiState.repoName, onBackClick) }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box (
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size((screenWidth / 6).dp),
                    color = MaterialTheme.colorScheme.TopBarBackgroundColor,
                    trackColor = MaterialTheme.colorScheme.TextColorLightGrey,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round
                )
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        bottom = 0.dp,
                        start = (screenWidth * 0.01).dp,
                        end = (screenWidth * 0.01).dp,
                    ),
                columns = GridCells.Adaptive(minSize = minSizeCell.dp),
                horizontalArrangement = Arrangement.spacedBy((screenWidth * 0.015).dp),
                verticalArrangement = Arrangement.spacedBy((screenHeight * 0.007).dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height((screenHeight * 0.003).dp))
                }
                item {
                    ActionCard(
                        { viewModel.changeStateAboutModal() },
                        "About",
                        Icons.Outlined.Info,
                        screenWidth,
                        screenHeight
                    )
                }
                item {
                    ActionCard(
                        { viewModel.changeStateReadmeModal() },
                        "Readme",
                        Icons.Outlined.Description,
                        screenWidth,
                        screenHeight
                    )
                }
                item {
                    ActionCard(
                        { checkCommits(repoData.first, repoData.second, repoData.third) },
                        "Commits",
                        Icons.Outlined.Commit,
                        screenWidth,
                        screenHeight)
                }
                item {
                    ActionCard(
                        {checkPullRequests(repoData.first, repoData.second, repoData.third)},
                        "Pull requests",
                        ImageVector.vectorResource(R.drawable.ic_rebase),
                        screenWidth,
                        screenHeight
                    )
                }
          /*      item {
                    ActionCard(
                        { viewModel.changeStarState() },
                        uiState.starButtonText,
                        ImageVector.vectorResource(uiState.starButtonIcon),
                        screenWidth,
                        screenHeight,
                    )
                }
                item {
                    ActionCard(
                        { viewModel.changeSubscribeState() },
                        uiState.subscribeButtonText,
                        uiState.subscribeButtonIcon,
                        screenWidth,
                        screenHeight,
                    )
                }*/
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
                }
            }

            if (uiState.aboutRepoModal) {
                ModalCard(
                    "About",
                    uiState.about,
                    screenWidth,
                    screenHeight
                ) { viewModel.changeStateAboutModal() }
            }

            if (uiState.readmeRepoModal) {
                ModalCard(
                    "Readme",
                    uiState.readme,
                    screenWidth,
                    screenHeight
                ) { viewModel.changeStateReadmeModal() }
            }
        }
    }
}

@Composable
fun ActionCard(
    onItemClick: (() -> Unit)? = null,
    actionText: String,
    iconImage: ImageVector,
    screenWidth: Int,
    screenHeight: Int,
    enabled: Boolean = true
){
    val iconSize = screenWidth*0.07

    ElevatedCard(
        onClick = { onItemClick?.invoke() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.DefaultButtons
        ),
        enabled = enabled
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding((screenWidth * 0.01).dp)
        ) {
            Icon(
                iconImage,
                //Icons.Outlined.Info,
                contentDescription = actionText,
                modifier = Modifier.size(iconSize.dp),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.height((screenHeight*0.01).dp))
            Text(
                text = actionText,
                color = MaterialTheme.colorScheme.TextColorBlack,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
fun ModalCard(
    title: String,
    text: String,
    screenWidth: Int,
    screenHeight: Int,
    onDismissRequest: () -> Unit
) {
    val titlePaddings = screenHeight * 0.005
    val dividerPaddings = screenWidth * 0.08
    val scrollState = rememberScrollState()

    Dialog (
        onDismissRequest = { onDismissRequest() }
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.DefaultButtons
            ),
            modifier = Modifier
                .padding(
                    start = (screenWidth * 0.01).dp,
                    end = (screenWidth * 0.01).dp
                )
                .verticalScroll(scrollState)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.TextColorBlack,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, titlePaddings.dp, 0.dp, titlePaddings.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dividerPaddings.dp, 0.dp, dividerPaddings.dp, 0.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.TextColorLightGrey,
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.TextColorBlack,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(titlePaddings.dp)
            )
            Button (
                onClick = { onDismissRequest() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.MainColor
                ),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp
                ),
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = "Dismiss",
                    color = MaterialTheme.colorScheme.TopBarBackgroundColor,
                    fontSize = 20.sp
                )
            }
        }
    }
}