package com.example.gitcheckmobileapp.ui.core

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.ui.theme.TextColorWhite
import com.example.gitcheckmobileapp.ui.theme.TopBarBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
            )
        },
        navigationIcon = {
            IconButton({ onBackClick() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(25.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.TopBarBackgroundColor,
            titleContentColor = MaterialTheme.colorScheme.TextColorWhite,
            navigationIconContentColor = MaterialTheme.colorScheme.TextColorWhite,
        )
    )
}