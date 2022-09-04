package com.github.paperrose.navigator

import androidx.compose.foundation.gestures.ScrollableState
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
open class NavViewModel(protected val args: Map<String, Any>) : ViewModel() {
    var navigationController: NavigationController? = null
    val scrollStates = hashMapOf<String, ScrollableState>()
    val pagerStates = hashMapOf<String, PagerState>()

    fun navReplaceStack(name: String, route: String, args: Map<String, Any>) {
        navigationController?.replaceStack(name, route, args)
    }

    fun navAdd(route: String, args: Map<String, Any>) {
        navigationController?.add(route, args)
    }

    fun navBack(): Boolean {
        return navigationController?.back() ?: false
    }

    fun navReplace(route: String, args: Map<String, Any>, createNew: Boolean = true) {
        navigationController?.replace(route, args, createNew)
    }

    fun navReplaceSingle(route: String, args: Map<String, Any>, createNew: Boolean = true) {
        navigationController?.replaceSingle(route, args, createNew)
    }

    fun navBackToFirst() {
        navigationController?.backToFirst()
    }
}