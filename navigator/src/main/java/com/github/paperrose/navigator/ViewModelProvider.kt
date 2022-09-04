package com.github.paperrose.navigator

abstract class ViewModelProvider {
    abstract fun getViewModelByType(type: String, args: Map<String, Any>): NavViewModel

    private val viewModels = hashMapOf<String, NavViewModel>()

    fun removeNavViewModel(uniqueId: String) {
        viewModels.remove(uniqueId)
    }

    fun getNavViewModel(uniqueId: String, type: String, args: Map<String, Any>): NavViewModel {
        viewModels[uniqueId]?.let { return it }
        val vm = getViewModelByType(type, args)
        viewModels[uniqueId] = vm
        return vm
    }
}