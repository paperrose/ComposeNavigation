package com.github.paperrose.navigator

import androidx.compose.runtime.Composable


class Destination(
    val route: String,
    val content: @Composable (NavEntry) -> Unit
) {
    lateinit var navController: NavigationController
}