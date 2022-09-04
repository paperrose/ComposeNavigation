package com.github.paperrose.navigator.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.paperrose.navigator.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(args: Map<String, Any>) : NavViewModel(args) {
    val color = getRandomColor()
    private var _currentTab = MutableStateFlow(0)
    val currentTab = _currentTab.asStateFlow()
    val tabNames = arrayListOf(
        MainViewModelProvider.ViewModelTypes.HOME,
        MainViewModelProvider.ViewModelTypes.ISSUES,
        MainViewModelProvider.ViewModelTypes.ARTICLES
    )

    fun openTab(tabName: String) {
        _currentTab.update {
            tabNames.indexOf(tabName)
        }
    }
}

@Composable
fun Main(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    Column(
        modifier = Modifier
            .background(color = viewModel.color)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            NavigationHost(
                controllerName = "TabController",
                initialDestination = InitialDestination(
                    MainViewModelProvider.ViewModelTypes.HOME,
                    hashMapOf(),
                    MainViewModelProvider.ViewModelTypes.HOME,
                ),
                viewModelProvider = MainViewModelProvider,
                backHandler = { tabName ->
                    tabName?.let {
                        viewModel.openTab(it)
                    }
                },
                finish = {
                    if (NavigationController.hasController("MainController")) {
                        val mainController =
                            NavigationController.getController("MainController")
                        if (!mainController.back()) {
                            mainController.finish()
                        }
                    }
                }
            ) {
                composable(MainViewModelProvider.ViewModelTypes.HOME) {
                    Home(
                        viewModel = it.getNavViewModel(
                            vmType = MainViewModelProvider.ViewModelTypes.HOME,
                            args = it.args
                        ) as HomeViewModel
                    )
                }
                composable(MainViewModelProvider.ViewModelTypes.ISSUES) {
                    Issues(
                        viewModel = it.getNavViewModel(
                            vmType = MainViewModelProvider.ViewModelTypes.ISSUES,
                            args = it.args
                        ) as IssuesViewModel
                    )
                }
                composable(MainViewModelProvider.ViewModelTypes.ARTICLES) {
                    Articles(
                        viewModel = it.getNavViewModel(
                            vmType = MainViewModelProvider.ViewModelTypes.ARTICLES,
                            args = it.args
                        ) as ArticlesViewModel
                    )
                }
                composable(MainViewModelProvider.ViewModelTypes.ISSUE) {
                    Issue(
                        viewModel = it.getNavViewModel(
                            vmUniqueId = "Issue ${it.args["issueId"] as Int}",
                            vmType = MainViewModelProvider.ViewModelTypes.ISSUE,
                            args = it.args
                        ) as IssueViewModel
                    )
                }
            }
        }
        Row(modifier = Modifier.wrapContentHeight()) {
            Button(onClick = {
                val tabController = NavigationController.getController("TabController")
                if (currentTab != 0) {
                    viewModel.openTab(viewModel.tabNames[0])
                    tabController.replaceStack(
                        viewModel.tabNames[0],
                        viewModel.tabNames[0],
                        hashMapOf()
                    )
                } else {
                    tabController.backToFirst()
                }
            }) {
                Text(
                    text = "Home",
                    color = if (currentTab == 0) Color.Red else Color.Blue
                )
            }
            Button(onClick = {
                val tabController = NavigationController.getController("TabController")

                if (currentTab != 1) {
                    viewModel.openTab(viewModel.tabNames[1])
                    tabController.replaceStack(
                        viewModel.tabNames[1],
                        viewModel.tabNames[1],
                        hashMapOf()
                    )
                } else {
                    tabController.backToFirst()
                }
            }) {
                Text(
                    text = "Issues",
                    color = if (currentTab == 1) Color.Red else Color.Blue
                )
            }
            Button(onClick = {
                val tabController = NavigationController.getController("TabController")
                if (currentTab != 2) {
                    viewModel.openTab(viewModel.tabNames[2])
                    tabController.replaceStack(
                        viewModel.tabNames[2],
                        viewModel.tabNames[2],
                        hashMapOf()
                    )
                } else {
                    tabController.backToFirst()
                }
            }) {
                Text(
                    text = "Articles",
                    color = if (currentTab == 2) Color.Red else Color.Blue
                )
            }
        }
    }
}