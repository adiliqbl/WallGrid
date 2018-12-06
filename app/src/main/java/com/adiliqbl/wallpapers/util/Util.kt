package com.adiliqbl.wallpapers.util

fun String.properCase(): String {
    return this.substring(0, 1).toUpperCase() + this.substring(1).toLowerCase()
}