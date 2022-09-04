package com.github.paperrose.navigator


internal class DestinationStack(val name: String) {
    val visibleEntries = arrayListOf<NavEntry>()
    val destinations = arrayListOf<String>()
    val allEntries = arrayListOf<NavEntry>()
}