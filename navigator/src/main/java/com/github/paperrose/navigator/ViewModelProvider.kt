package com.github.paperrose.navigator

abstract class ViewModelProvider {
    abstract fun getViewModelByType(type: String, args: Map<String, Any>): NavViewModel?



    fun getNavViewModel(uniqueId: String, type: String, args: Map<String, Any>): NavViewModel? {
        return null
    }
}