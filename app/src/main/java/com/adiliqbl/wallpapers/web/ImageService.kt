package com.adiliqbl.wallpapers.web

import com.adiliqbl.wallpapers.data.ImageResponse
import com.adiliqbl.wallpapers.system.di.IMAGE_API_KEY
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

    @GET("?key=$IMAGE_API_KEY&safesearch=true")
    fun getImages(@Query("page") page: Int,
                  @Query("per_page") limit: Int,
                  @Query("category") category: String,
                  @Query("order") order: String = "latest"): Observable<Response<ImageResponse>>

    @GET("?key=$IMAGE_API_KEY&safesearch=true")
    fun searchImages(@Query("q") searchQuery: String,
                     @Query("page") page: Int,
                     @Query("per_page") limit: Int,
                     @Query("category") category: String): Observable<Response<ImageResponse>>
}