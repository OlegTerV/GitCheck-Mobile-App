package com.example.gitcheckmobileapp.ui.commits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.data.model.AuthorOrCommitter
import com.example.gitcheckmobileapp.data.model.OneCommit
import com.example.gitcheckmobileapp.data.network.dto.AuthorOrCommitterDTO
import com.example.gitcheckmobileapp.ui.core.TitleAndTextRow
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey
import com.example.gitcheckmobileapp.ui.theme.TopBarBackgroundColor

@Composable
fun CommitsView(
    viewModel: CommitsViewModel,
    onBackItem: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalWindowInfo.current.containerSize
    val width = configuration.width
    val height = configuration.height
    val snackbarHost = remember{ SnackbarHostState() }

    Scaffold(
        topBar = {
            TopBar(
                title = uiState.repoName,
                onBackClick = onBackItem,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { innerPadding ->
        if(uiState.isLoading){
            Box(
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
            LazyColumn(//TODO: заменить на адаптивную сетку (в контексте широких экранов/средних/ и т.д.)
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + (height * 0.01).dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = (width * 0.01).dp,
                    end = (width * 0.01).dp
                ),
                verticalArrangement = Arrangement.spacedBy((height * 0.007).dp)
            ) {
                items(uiState.commits) { currentCommit ->
                    CardCommit(currentCommit, height, width)
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
fun CardCommit(
    currentCommit: OneCommit,
    screenHeight: Int,
    screenWidth: Int,
) {
    val dividerPaddings = screenWidth * 0.02
    val cardContentPaddings = screenWidth * 0.02
    val titlePaddings = screenHeight * 0.005

    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.DefaultButtons
        ),
    ){
        AuthorOrCommitterView("Author", currentCommit.author, titlePaddings, dividerPaddings)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dividerPaddings.dp, 0.dp, dividerPaddings.dp, 0.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey,
        )
        AuthorOrCommitterView("Committer", currentCommit.committer, titlePaddings, dividerPaddings)
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dividerPaddings.dp, 0.dp, dividerPaddings.dp, 0.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey,
        )
        Text(
            text = "Message",
            color = MaterialTheme.colorScheme.TextColorBlack,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, titlePaddings.dp, 0.dp, titlePaddings.dp)
        )
        Text(
            text = currentCommit.message,
            color = MaterialTheme.colorScheme.TextColorBlack,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth().padding(
                    start = dividerPaddings.dp,
                    end = dividerPaddings.dp,
                    bottom = titlePaddings.dp
                ),
        )
    }
}

@Composable
fun AuthorOrCommitterView(
    title: String,
    authorOrCommitter: AuthorOrCommitter,
    titlePaddings: Double,
    dividerPaddings: Double
){
    Text(
        text = title,
        color = MaterialTheme.colorScheme.TextColorBlack,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, titlePaddings.dp, 0.dp, titlePaddings.dp)
    )

    TitleAndTextRow("Name: ", authorOrCommitter.name, dividerPaddings, 0.0)
    TitleAndTextRow("Email: ", authorOrCommitter.email , dividerPaddings, 0.0)
    TitleAndTextRow("Date: ", authorOrCommitter.date, dividerPaddings, titlePaddings)
}

