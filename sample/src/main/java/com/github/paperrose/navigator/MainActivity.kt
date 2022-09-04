package com.github.paperrose.navigator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.LOGIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.MAIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.READER
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.SPLASH
import com.github.paperrose.navigator.screens.*
import com.github.paperrose.navigator.ui.theme.ComposeNavigationXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigationXTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(onFinish = { finish() })
                }
            }
        }
    }
}

@Composable
fun Greeting(onFinish: () -> Unit) {
    NavigationHost(
        controllerName = "MainController",
        viewModelProvider = MainViewModelProvider,
        initialDestination = InitialDestination(SPLASH, hashMapOf()),
        finish = { onFinish() },
        backHandler = { result ->
            if (result == null)
                onFinish()
        }
    ) {
        composable(SPLASH) {
            Splash(
                viewModel = it.getNavViewModel(
                    vmType = SPLASH,
                    args = it.args
                ) as SplashViewModel
            )
        }
        composable(LOGIN) {
            Login(
                viewModel = it.getNavViewModel(
                    vmType = LOGIN,
                    args = it.args
                ) as LoginViewModel
            )
        }
        composable(MAIN) {
            Main(
                viewModel = it.getNavViewModel(
                    vmType = MAIN,
                    args = it.args
                ) as MainViewModel
            )
        }
        composable(READER) {
            Reader(
                viewModel = it.getNavViewModel(
                    vmType = READER,
                    args = it.args
                ) as ReaderViewModel
            )
        }
    }
}
