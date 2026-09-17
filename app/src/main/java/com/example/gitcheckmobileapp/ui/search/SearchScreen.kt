package com.example.gitcheckmobileapp.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gitcheckmobileapp.data.model.User
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey
import com.example.gitcheckmobileapp.ui.theme.TopBarBackgroundColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onItemClick: (Long, String) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val screenHeight = configuration.height
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopBar("UserSearch", onBackClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    (screenWidth * 0.01).dp,
                    innerPadding.calculateTopPadding() + (screenHeight * 0.01).dp,
                    (screenWidth * 0.01).dp,
                    innerPadding.calculateBottomPadding()
                )
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(15.dp)
                )
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) viewModel.changeSearchState(true)
                    else viewModel.changeSearchState(false)
                },
            value = uiState.searchQuery,
            onValueChange = {viewModel.changeSearchQuery(it)},
            textStyle = TextStyle(fontSize = 20.sp),
            placeholder = { Text(text = "Enter username", fontSize = 20.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.DefaultButtons,
                unfocusedContainerColor = MaterialTheme.colorScheme.DefaultButtons,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor= Color.Transparent,
                disabledBorderColor= Color.Transparent,
                errorBorderColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        LaunchedEffect(uiState.errorMessage) {
            uiState.errorMessage?.let {
                snackbarHostState.showSnackbar(
                    uiState.errorMessage ?: "Something error",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
                viewModel.closeErrorMessage()
            }
        }

        if ((uiState.usersList.isNotEmpty()) && (!uiState.isLoading)){
            ElevatedCard(
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        (screenWidth * 0.01).dp,
                        innerPadding.calculateTopPadding() * 2 + (screenHeight * 0.003).dp,
                        (screenWidth * 0.01).dp,
                        innerPadding.calculateBottomPadding()
                    )
                    .imePadding(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.DefaultButtons
                ),
            ) {
                LazyColumn( ) {
                    items(uiState.usersList) { currentRepo ->
                        RepoRow(currentRepo, uiState.usersList.last().id != currentRepo.id, onItemClick)
                    }
                }
            }
        }
        else if ((uiState.usersList.isEmpty()) && (!uiState.isLoading)) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "View the repository",
                modifier = Modifier
                    .fillMaxHeight()
                    .size((screenWidth * 0.45).dp)
                    .padding(0.dp, (screenHeight * 0.05).dp, 0.dp, 0.dp),
                tint = MaterialTheme.colorScheme.TextColorLightGrey
            )
        }
        else if (uiState.isLoading) {
            Box (
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size((screenWidth / 6).dp),
                    color = MaterialTheme.colorScheme.TopBarBackgroundColor,
                    trackColor = MaterialTheme.colorScheme.TextColorLightGrey,
                    strokeWidth = 12.dp
                )
            }
        }
    }
}

@Composable
fun RepoRow(currentUser: User, dividerFlag: Boolean, onItemClick: (Long, String) -> Unit){
    Row(
        modifier = Modifier
            .clickable { onItemClick(currentUser.id, currentUser.login) }
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = currentUser.login,
            color = MaterialTheme.colorScheme.TextColorBlack,
            fontSize = 20.sp
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = "View the repository",
            modifier = Modifier
                .size(25.dp)
                .padding(0.dp),
            tint = MaterialTheme.colorScheme.TextColorBlack
        )
    }
    if (dividerFlag) {
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey
        )
    }
}