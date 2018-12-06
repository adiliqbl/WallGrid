package com.adiliqbl.wallpapers.data

import android.support.v7.util.DiffUtil
import com.squareup.moshi.Json
import java.io.Serializable

class Image : Serializable {
    @Json(name = "id")
    lateinit var id: String

    @Json(name = "largeImageURL")
    var largeImageURL: String? = null

    @Json(name = "likes")
    var likes: Int = 0

    @Json(name = "comments")
    var comments: Int = 0

    @Json(name = "type")
    lateinit var type: String

    @Json(name = "views")
    lateinit var views: String

    @Json(name = "previewURL")
    var previewURL: String? = null

    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<Image> = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}