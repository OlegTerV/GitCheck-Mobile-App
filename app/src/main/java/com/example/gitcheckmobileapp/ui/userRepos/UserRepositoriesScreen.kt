package com.example.gitcheckmobileapp.ui.userRepos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CallSplit
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.search.AllRepos
import com.example.gitcheckmobileapp.ui.search.repos
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey

@Composable
fun UserRepositoriesScreen(
    viewModel: UserRepositoriesViewModel,
    onItemClick: (Long) -> Unit,
    onBackClick: () -> Unit
){
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val screenHeight = configuration.height

    Scaffold(
        topBar = { TopBar("User repositories", onBackClick) }
    ) { innerPadding ->
        LazyColumn(//TODO: заменить на адаптивную сетку (в контексте широких экранов/средних/ и т.д.)
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + (screenHeight * 0.01).dp,
                bottom = innerPadding.calculateBottomPadding(),
                start = (screenWidth*0.01).dp,
                end = (screenWidth*0.01).dp
            ),
            verticalArrangement = Arrangement.spacedBy((screenHeight * 0.007).dp)
        ) {
            items(repos) { currentRepo ->
                CardShortRepoInfo(currentRepo, screenHeight, screenWidth, onItemClick)
            }
        }
    }
}

@Composable
fun CardShortRepoInfo(
    currentRepo: AllRepos,
    screenHeight: Int,
    screenWidth: Int,
    onItemClick: (Long) -> Unit
) {
    val dividerPaddings = screenWidth * 0.02
    val cardContentPaddings =  screenWidth * 0.02
    val titlePaddings = screenHeight * 0.005

    ElevatedCard(
        onClick = {onItemClick(currentRepo.id.toLong())},
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
        Text(
            text = currentRepo.name,
            color = MaterialTheme.colorScheme.TextColorBlack,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth().padding(0.dp, titlePaddings.dp, 0.dp, titlePaddings.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dividerPaddings.dp, 0.dp, dividerPaddings.dp ,0.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey,
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dividerPaddings.dp, titlePaddings.dp, dividerPaddings.dp, titlePaddings.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardWidget(Icons.Outlined.Star, "Number of stars", "12", screenHeight * 0.009)
            Spacer(modifier = Modifier.width((screenWidth*0.01).dp))
            CardWidget(Icons.Outlined.RemoveRedEye, "Number of subscribers", "12", screenHeight * 0.009)
            Spacer(modifier = Modifier.weight(1f))
            CardWidget(Icons.AutoMirrored.Outlined.CallSplit, "Number of forks", "12", screenHeight * 0.009)
        }
    }
}

@Composable
fun CardWidget(iconImage: ImageVector, desc: String, value: String, valueSize: Double){
    Icon(
        imageVector = iconImage,
        contentDescription = desc,
        modifier = Modifier
            .size(20.dp),
        tint = Color.DarkGray
    )
    Text(
        text = value,
        fontSize = 20.sp,
        color = Color.DarkGray
    )
}