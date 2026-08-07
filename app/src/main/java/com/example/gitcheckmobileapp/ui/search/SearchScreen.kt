package com.example.gitcheckmobileapp.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.ui.core.TopBar
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack
import com.example.gitcheckmobileapp.ui.theme.TextColorLightGrey

data class AllRepos(val id: Int, val name: String, val desc: String, val readme: String)
val repos = listOf(
    AllRepos(12, "IOS labs", "Лабы в рамках курса ios-разработки", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(13, "Android labs", "Лабы в рамках курса android-разработки", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(14, "Assembler labs", "Лабы в рамках курса самоубийства", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(15, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(16, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(17, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(18, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(19, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(20, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(21, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(22, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(23, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(24, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
    AllRepos(25, "C++ labs", "Лабы в рамках курса мазохизма", "readme info we wer wer werwer we dfwae wefaw efwaefawe fwf wdf "),
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onItemClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val screenHeight = configuration.height
    var searchQuery by remember { mutableStateOf("") }
    var searchState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar("UserSearch", onBackClick) }
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
                .onFocusChanged{focusState ->
                    if (focusState.isFocused) searchState = true
                    else searchState = false
                },
            value = searchQuery,
            onValueChange = {searchQuery = it},
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
        )

        if (searchState) {
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
                    ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.DefaultButtons
                ),
            ) {
                LazyColumn( ) {
                    items(repos) { currentRepo ->
                        RepoRow(currentRepo, repos.last().id != currentRepo.id, onItemClick)
                    }
                }
            }
        }
        else{
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "View the repository",
                modifier = Modifier
                    .fillMaxHeight()
                    .size((screenWidth*0.45).dp)
                    .padding(0.dp, (screenHeight*0.05).dp, 0.dp, 0.dp),
                tint = MaterialTheme.colorScheme.TextColorLightGrey
            )
        }
    }
}

@Composable
fun RepoRow(currentRepo: AllRepos, dividerFlag: Boolean, onItemClick: (Long) -> Unit){
    Row(
        modifier = Modifier.clickable{ onItemClick(currentRepo.id.toLong()) }.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = currentRepo.name,
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
                .padding(16.dp, 0.dp, 16.dp ,0.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.TextColorLightGrey
        )
    }
}