package com.adiliqbl.wallpapers.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlin.reflect.KClass

fun <T> getListFromJson(json: String, clazz: KClass<*>): MutableList<T> {
    return Moshi.Builder().build()
            .adapter<MutableList<T>>(Types.newParameterizedType(MutableList::class.java, clazz.java))
            .fromJson(json)!!
}

fun toProperCase(s: String): String {
    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()
}