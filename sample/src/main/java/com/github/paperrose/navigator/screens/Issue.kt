package com.github.paperrose.navigator.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.paperrose.navigator.MainViewModelProvider
import com.github.paperrose.navigator.NavViewModel
import com.github.paperrose.navigator.NavigationController
import kotlin.random.Random

class IssueViewModel(args: Map<String, Any>) : NavViewModel(args) {
    val color = getRandomColor()
    private val issueId = args["issueId"] as Int
    val name = "Issue $issueId"
    fun openReader() {
        val mainController = NavigationController.getController("MainController")
        mainController.add(
            MainViewModelProvider.ViewModelTypes.READER,
            hashMapOf("issueId" to issueId)
        )
    }
}

@Composable
fun Issue(viewModel: IssueViewModel) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = viewModel.color)
            .fillMaxSize()
    ) {

        Text(text = viewModel.name)
        Button(onClick = {
            val issueId = Random.nextInt(0, 256)
            Log.e("IssueId", "$issueId")
            viewModel.navAdd(
                MainViewModelProvider.ViewModelTypes.ISSUE,
                hashMapOf(
                    "issueId" to issueId
                )
            )
        }) {
            Text(text = "Open Issue")
        }
        Button(onClick = {
            viewModel.openReader()
        }) {
            Text(text = "Open Reader")
        }
    }
}