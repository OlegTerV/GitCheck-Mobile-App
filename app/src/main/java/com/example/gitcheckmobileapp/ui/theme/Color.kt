package com.example.gitcheckmobileapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val Pink40 = Color(0xFF7D5260)

val MainColor = Color(0xFFEFE5FF) //фон
val DefaultButtons = Color(0xFFFFFCE2) //карточки/кнопки
val TextColorBlack = Color(0xFF000000)
val TextColorWhite = Color(0xFFFFFFFF)
val TextColorLightGrey = Color(0xFFB6B1BF)

val ColorScheme.TextColorWhite get() = this.primary
val ColorScheme.TextColorBlack get() = this.secondary
val ColorScheme.DefaultButtons get() = this.tertiary
val ColorScheme.MainColor get() = this.background
val ColorScheme.TopBarBackgroundColor get() = this.surface
val ColorScheme.TextColorLightGrey get() = this.onTertiary