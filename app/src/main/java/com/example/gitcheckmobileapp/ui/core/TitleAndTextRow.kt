package com.example.gitcheckmobileapp.ui.core

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack

@Composable
fun TitleAndTextRow(
    title: String,
    text: String,
    dividerPaddings: Double,
    bottomPadding: Double,
){
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(
                start = dividerPaddings.dp,
                end = dividerPaddings.dp,
                bottom = bottomPadding.dp
            ),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.TextColorBlack,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.TextColorBlack,
            fontSize = 20.sp,
        )
    }
}