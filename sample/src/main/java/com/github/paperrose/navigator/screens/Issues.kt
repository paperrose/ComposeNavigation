package com.github.paperrose.navigator.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.paperrose.navigator.MainViewModelProvider
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.LOGIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.MAIN
import com.github.paperrose.navigator.NavViewModel
import com.github.paperrose.navigator.NavigationController
import java.util.*
import kotlin.random.Random

class IssuesViewModel(args: Map<String, Any>) : NavViewModel(args) {
    val color = getRandomColor()
}

@Composable
fun Issues(viewModel: IssuesViewModel) {
    Column(
        modifier = Modifier
            .background(color = viewModel.color)
            .fillMaxSize()
    ) {
        Text(text = "Issues")
        Button(onClick = {

            val issueId = Random.nextInt(0, 256)
            Log.e("IssueId", "$issueId")
            viewModel.navReplace(
                MainViewModelProvider.ViewModelTypes.ISSUE,
                hashMapOf(
                    "issueId" to issueId
                )
            )
        }) {
            Text(text = "Open Issue")
        }
    }
}