package com.github.paperrose.navigator

import androidx.compose.runtime.Composable
import java.util.*

class NavEntry(
    val destination: Destination,
    val args: Map<String, Any>,
    val content: @Composable (NavEntry) -> Unit,
    val uniqueId: String = UUID.randomUUID().toString()
) {
    private fun navController() = destination.navController
    private fun viewModelProvider() = navController().viewModelProvider
    private var viewModelId: String? = null

    internal fun removeNavViewModel() {
        viewModelId?.let {
            viewModelProvider().removeNavViewModel(it)
        }
    }

    fun getNavViewModel(
        vmUniqueId: String? = null,
        vmType: String,
        args: Map<String, Any>
    ): NavViewModel {
        viewModelId = vmUniqueId ?: vmType
        return viewModelProvider().getNavViewModel(viewModelId!!, vmType, args)
            .also { it.navigationController = navController() }
    }
}