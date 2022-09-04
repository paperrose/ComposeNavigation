package com.github.paperrose.navigator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.LOGIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.MAIN
import com.github.paperrose.navigator.NavViewModel
import java.util.*

class LoginViewModel(args: Map<String, Any>) : NavViewModel(args) {
    val color = getRandomColor()
    val name = "${args["userId"]} ${args["userName"]}"
}

@Composable
fun Login(viewModel: LoginViewModel) {
    Box(
        modifier = Modifier
            .background(color = viewModel.color)
            .fillMaxSize()
    ) {
        Button(onClick = {
            viewModel.navReplaceSingle(
                MAIN,
                hashMapOf()
            )
        }) {
            Text(text = viewModel.name)
        }
    }
}