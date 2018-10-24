package com.adiliqbl.wallpapers.util

class Response<T>(var status: Status?, var data: T?, var error: Throwable?)

fun <T> Loading(): Response<T> {
    return Response(Status.LOADING, null, null)
}

fun <T> Success(data: T): Response<T> {
    return Response(Status.SUCCESS, data, null)
}

fun <T> Error(error: Throwable): Response<T> {
    return Response(Status.ERROR, null, error)
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}