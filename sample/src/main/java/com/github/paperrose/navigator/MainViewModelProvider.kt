package com.github.paperrose.navigator

import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.ARTICLES
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.AUTH
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.HOME
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.ISSUE
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.ISSUES
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.LOGIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.MAIN
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.PLAYER
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.READER
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.SHELF
import com.github.paperrose.navigator.MainViewModelProvider.ViewModelTypes.SPLASH
import com.github.paperrose.navigator.screens.*

object MainViewModelProvider : ViewModelProvider() {

    object ViewModelTypes {
        const val SPLASH = "splash"
        const val LOGIN = "login"
        const val AUTH = "auth"
        const val PASS = "pass"
        const val MAIN = "main"
        const val HOME = "home"
        const val ISSUES = "issues"
        const val ARTICLES = "articles"
        const val SHELF = "shelf"
        const val ISSUE = "issue"
        const val PLAYER = "player"
        const val READER = "reader"
    }

    override fun getViewModelByType(type: String, args: Map<String, Any>): NavViewModel {
        return when (type) {
            SPLASH -> SplashViewModel(args)
            LOGIN -> LoginViewModel(args)
            MAIN -> MainViewModel(args)
            AUTH -> NavViewModel(args)
            HOME -> HomeViewModel(args)
            ISSUES -> IssuesViewModel(args)
            ISSUE -> IssueViewModel(args)
            ARTICLES -> ArticlesViewModel(args)
            SHELF -> NavViewModel(args)
            PLAYER -> NavViewModel(args)
            READER -> ReaderViewModel(args)
            else -> NavViewModel(args)
        }
    }
}