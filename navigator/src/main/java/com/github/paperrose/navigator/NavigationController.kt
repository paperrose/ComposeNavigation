package com.github.paperrose.navigator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class NavigationController private constructor(
    id: String,
    private val finishHandler: () -> Unit = {}
) {
    private val id: String = id

    companion object {

        internal const val DEFAULT_STACK_NAME = "_DefaultStack"

        fun hasController(id: String): Boolean {
            return NavControllerHolder.has(id)
        }

        fun remove(id: String) {
            NavControllerHolder.remove(id)
        }

        fun getController(
            id: String, finishHandler: () -> Unit = {}
        ): NavigationController {
            return NavControllerHolder.getOrPut(id) {
                NavigationController(id, finishHandler)
            }
        }
    }

    private val visibleEntries =
        MutableStateFlow(
            emptyList<NavEntry>()
        )

    internal val entries = visibleEntries.asStateFlow()


    private var _destroyed =
        MutableStateFlow(
            false
        )

    internal val destroyed = _destroyed.asStateFlow()

    private val stacks = arrayListOf<DestinationStack>()
    private val stackNavigations = arrayListOf<String>()
    internal var currentStack: DestinationStack? = null


    private fun checkStacks() {
        if (currentStack == null && stacks.isEmpty()) {
            DestinationStack(DEFAULT_STACK_NAME).apply {
                currentStack = this
                stacks.add(this)
                stackNavigations.add(this.name)
            }
        }
    }

    lateinit var viewModelProvider: ViewModelProvider

    private val destinations = hashMapOf<String, Destination>()

    internal fun addDestination(destination: Destination) {
        destination.navController = this
        destinations[destination.route] = destination
    }

    fun add(
        route: String,
        args: Map<String, Any>
    ) {
        checkStacks()
        destinations[route]?.apply {
            val stack = currentStack!!
            val entry = NavEntry(destination = this, args = args, content = this.content)
            stack.allEntries.add(entry)
            stack.destinations.add(entry.uniqueId)
            stack.visibleEntries.apply {
                add(entry)
            }
            updateVisibleEntries()
            return
        }
        throw Exception("Unknown route $route")
    }

    private fun updateVisibleEntries() {
        visibleEntries.update {
            ArrayList(currentStack?.visibleEntries ?: emptyList())
        }
    }

    private fun findAndReplace(
        route: String,
        args: Map<String, Any>,
        replaceSingle: Boolean = false
    ) {
        synchronized(this) {
            destinations[route]?.apply {

                val stack = currentStack!!
                val ind =
                    stack.allEntries.indexOfLast {
                        it.destination.route == route
                                && it.args == args
                    }
                val entry = if (ind > -1) {
                    stack.allEntries.removeAt(ind)
                } else {
                    NavEntry(destination = this, args = args, content = this.content)
                }
                if (replaceSingle) {
                    stack.allEntries.clear()
                    stack.destinations.clear()
                    stack.visibleEntries.clear()
                }
                stack.allEntries.add(entry)
                stack.destinations.add(entry.uniqueId)
                stack.visibleEntries.apply {
                    removeLastOrNull()
                    add(entry)
                }
                updateVisibleEntries()
                return
            }
        }
        throw Exception("Unknown route $route")
    }


    fun replaceStack(
        name: String,
        route: String,
        args: Map<String, Any>
    ) {
        synchronized(this) {
            stacks.find { it.name == name }?.let {
                stackNavigations.remove(it.name)
                stackNavigations.add(it.name)
                currentStack = it
                updateVisibleEntries()
                return
            }
            DestinationStack(name).apply {
                stacks.add(this)
                stackNavigations.add(this.name)
                currentStack = this
                add(route, args)
            }
        }
    }

    fun backToFirst() {
        synchronized(this) {
            val stack = currentStack!!
            if (stack.destinations.isEmpty()) return
            val firstId = stack.destinations.first()
            stack.destinations.clear()
            stack.visibleEntries.clear()
            val entry = stack.allEntries.find { it.uniqueId == firstId }
            stack.allEntries.clear()
            entry?.let {
                stack.allEntries.add(it)
                stack.destinations.add(it.uniqueId)
                stack.visibleEntries.add(it)
                updateVisibleEntries()
            }
        }
    }

    fun replaceSingle(
        route: String,
        args: Map<String, Any>,
        createNew: Boolean = true,
        withStacks: Boolean = false
    ) {

        synchronized(this) {
            if (withStacks) {
                stacks.clear()
                stackNavigations.clear()
            }
            checkStacks()
            if (!createNew) {
                findAndReplace(route, args, true)
                return
            }
            destinations[route]?.apply {
                val stack = currentStack!!
                stack.allEntries.forEach {
                    it.removeNavViewModel()
                }
                val entry = NavEntry(destination = this, args = args, content = this.content)
                stack.allEntries.apply {
                    clear()
                    add(entry)
                }
                stack.destinations.apply {
                    clear()
                    add(entry.uniqueId)
                }
                stack.visibleEntries.apply {
                    clear()
                    add(entry)
                }
                updateVisibleEntries()
                return
            }
        }
        throw Exception("Unknown route $route")
    }

    fun destroy() {
        stacks.forEach { destinationStack ->
            destinationStack.allEntries.forEach {
                it.removeNavViewModel()
            }
        }
        stacks.clear()
        _destroyed.update {
            true
        }
    }

    fun replace(
        route: String,
        args: Map<String, Any>,
        createNew: Boolean = true,
    ) {

        synchronized(this) {
            checkStacks()
            if (!createNew) {
                findAndReplace(route, args)
                return
            }
            destinations[route]?.apply {
                val stack = currentStack!!
                val entry = NavEntry(destination = this, args = args, content = this.content)
                stack.allEntries.add(entry)
                stack.destinations.add(entry.uniqueId)
                stack.visibleEntries.apply {
                    removeLastOrNull()
                    add(entry)
                }
                updateVisibleEntries()
                return
            }
        }
        throw Exception("Unknown route $route")
    }

    fun finish() = finishHandler.invoke()

    fun back(): Boolean {
        synchronized(this) {
            val stack = currentStack ?: return false
            if (stack.destinations.size <= 1) {
                return if (stackNavigations.size <= 1) {
                    remove(id)
                    false
                } else {
                    stackNavigations.removeLastOrNull()
                    currentStack = stacks.firstOrNull { it.name == stackNavigations.lastOrNull() }
                    updateVisibleEntries()
                    true
                }
            }
            val lastEntryId = stack.destinations.removeLastOrNull()
            val prevEntryId = stack.destinations.lastOrNull()
            var prevEntry: NavEntry? = null
            prevEntryId?.let { uniqueId ->
                prevEntry = stack.allEntries.findLast { it.uniqueId == uniqueId }
            }
            if (!stack.destinations.contains(lastEntryId)) {
                val ind = stack.allEntries.indexOfLast { it.uniqueId == lastEntryId }
                if (ind > -1) {
                    stack.allEntries.removeAt(ind).removeNavViewModel()
                }
            }
            stack.visibleEntries.apply {
                removeLastOrNull()
                prevEntryId?.let { id ->
                    if (lastOrNull() == null || last().uniqueId != id) {
                        prevEntry?.let { entry ->
                            add(entry)
                        }
                    }
                }
            }
            updateVisibleEntries()
            return true
        }
    }
}