package com.github.paperrose.navigator.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.paperrose.navigator.NavViewModel
import com.github.paperrose.navigator.NavigationController

class ReaderViewModel(args: Map<String, Any>) : NavViewModel(args) {
    val color = getRandomColor()
    val name = "Reader ${args["issueId"]}"
}

@Composable
fun Reader(viewModel: ReaderViewModel) {
    Box(
        modifier = Modifier
            .background(color = viewModel.color)
            .fillMaxSize()
    ) {
        Text(text = viewModel.name)
    }
    BackHandler {
        NavigationController.getController("MainController").back()
    }
}