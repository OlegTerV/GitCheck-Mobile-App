package com.example.gitcheckmobileapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gitcheckmobileapp.R
import com.example.gitcheckmobileapp.ui.theme.DefaultButtons
import com.example.gitcheckmobileapp.ui.theme.MainColor
import com.example.gitcheckmobileapp.ui.theme.TextColorBlack

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onItemClick: () -> Unit
) {
    val configuration = LocalWindowInfo.current.containerSize
    val screenWidth = configuration.width
    val screenHeight = configuration.height

    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.MainColor),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = "GitCheck",
            fontFamily = MaterialTheme.typography.headlineLarge.fontFamily,
            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
            fontSize = (screenWidth * 0.065).sp
        )
        Image (
            painter = painterResource(id = R.drawable.git_logo),
            contentDescription = stringResource(id = R.string.logo_name),
            modifier = Modifier.size((screenWidth * 0.3).dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy((screenHeight*0.005).dp)
        ){
            Button(
                onClick = {  },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(MaterialTheme.colorScheme.TextColorBlack.toArgb()),
                    containerColor = Color(MaterialTheme.colorScheme.DefaultButtons.toArgb())),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp
                ),
                modifier = Modifier.width((screenWidth * 0.25).dp),
                enabled = false
            ) {
                Text(
                    text = "Sign In",
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = { onItemClick() },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(MaterialTheme.colorScheme.TextColorBlack.toArgb()),
                    containerColor = Color(MaterialTheme.colorScheme.DefaultButtons.toArgb())),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 3.dp
                ),
                modifier = Modifier.width((screenWidth * 0.25).dp)
            ) {
                Text(
                    text = "Skip for Now",
                    fontSize = 20.sp
                )
            }
        }
    }
}