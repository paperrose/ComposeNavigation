package com.github.paperrose.navigator

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavEntry(
    val destinationWrapper: DestinationWrapper,
    val content: @Composable (DestinationWrapper) -> Unit
)

class NavigationController {
    private val visibleEntries =
        MutableStateFlow(
            emptyList<NavEntry>()
        )
    val entries = visibleEntries.asStateFlow()
    val destinationStack = arrayListOf<String>()
    val allEntries = arrayListOf<NavEntry>()

    lateinit var viewModelProvider: ViewModelProvider

    private val destinations = hashMapOf<String, Destination>()

    /*internal fun setDestinations(destinations: Map<String, Destination>) {
        synchronized(destinations) {
            if (this.destinations.isNotEmpty()) return
            if (destinations.isEmpty()) throw Exception("NavigationHost must contain at least one destination")
            this.destinations.putAll(destinations)
        }
    }*/

    fun addEntry() {

    }

    fun addDestination(destination: Destination) {
        destination.navController = this
        destinations[destination.route] = destination
    }

    fun navigate(
        route: String,
        args: Map<String, Any>
    ) {
        destinations[route]?.apply {
            val entry = NavEntry(DestinationWrapper(this, args), this.content)
            allEntries.add(entry)
            visibleEntries.update {
                arrayListOf(entry)
            }
        }
    }

    fun back() {
        val entry = allEntries.lastOrNull()
        entry?.let { entry ->
            visibleEntries.update {
                arrayListOf(entry)
            }
            allEntries.remove(entry)
        }
    }
}