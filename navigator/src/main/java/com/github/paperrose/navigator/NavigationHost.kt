package com.github.paperrose.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun NavigationHost(
    navigationController: NavigationController,
    builder: NavigationController.() -> Unit
) {
    //navigationController.setDestinations(destinations)
    builder.invoke(navigationController)
    val entries by navigationController.entries.collectAsState()
    entries.forEach {
        it.content(it.destinationWrapper)
    }
}

@Composable
fun Test() {
    val controller = NavigationController()
    controller.viewModelProvider = object : ViewModelProvider() {
        override fun getViewModelByType(type: String, args: Map<String, Any>): NavViewModel {
            return NavViewModel(args)
        }
    }
    NavigationHost(navigationController = controller) {
        composable("test") {
            Screen1(it)
        }
        composable("test2") {
            Screen1(it)
        }
        composable("test3") {
            Screen1(it)
        }
        composable("test4") {
            Screen1(it)
        }
    }
}

@Composable
fun Screen1(
    destinationWrapper: DestinationWrapper
) {
}

fun NavigationController.composable(
    route: String,
    content: @Composable (DestinationWrapper) -> Unit
) {
    this.addDestination(Destination(route, content))
}