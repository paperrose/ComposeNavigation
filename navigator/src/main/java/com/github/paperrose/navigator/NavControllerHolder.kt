package com.github.paperrose.navigator

internal object NavControllerHolder {
    private val controllers = hashMapOf<String, NavigationController>()

    fun has(key: String): Boolean {
        synchronized(controllers) {
            return controllers.contains(key)
        }
    }

    fun remove(key: String) {
        synchronized(controllers) {
            controllers.remove(key)
        }
    }

    fun getOrPut(
        key: String,
        default: () -> NavigationController
    ): NavigationController {
        synchronized(controllers) {
            controllers[key]?.let {
                return it
            }
            val result = default()
            controllers[key] = result
            return result
        }
    }
}