package com.github.paperrose.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.github.paperrose.navigator.NavigationController.Companion.DEFAULT_STACK_NAME

data class InitialDestination(
    val route: String,
    val args: Map<String, Any>,
    val stackName: String = DEFAULT_STACK_NAME
)


@Composable
fun NavigationHost(
    controllerName: String,
    initialDestination: InitialDestination,
    viewModelProvider: ViewModelProvider,
    backHandler: (String?) -> Unit = {},
    finish: () -> Unit = {},
    builder: NavigationController.() -> Unit
) {
    val hasController = NavigationController.hasController(controllerName)
    val controller = NavigationController.getController(controllerName, finish)
    controller.viewModelProvider = viewModelProvider
    builder.invoke(controller)
    if (!hasController)
        controller.replaceStack(
            initialDestination.stackName,
            initialDestination.route,
            initialDestination.args
        )
    val entries by controller.entries.collectAsState()
    entries.forEach {
        it.content(it)
    }
    BackHandler {
        if (!controller.back()) {
            backHandler(null)
            controller.finish()
            controller.destroy()
        } else
            backHandler(controller.currentStack?.name)
    }
}


fun NavigationController.composable(
    route: String,
    content: @Composable (NavEntry) -> Unit
) {
    this.addDestination(Destination(route, content))
}