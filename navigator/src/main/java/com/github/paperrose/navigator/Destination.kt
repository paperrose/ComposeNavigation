package com.github.paperrose.navigator

import androidx.compose.runtime.Composable

class DestinationWrapper(
    private val destination: Destination,
    val args: Map<String, Any>,
) {
    fun viewModelProvider() = destination.navController.viewModelProvider
    fun navController() = destination.navController
}

class Destination(
    val route: String,
    val content: @Composable (DestinationWrapper) -> Unit
) {
    lateinit var navController: NavigationController
}