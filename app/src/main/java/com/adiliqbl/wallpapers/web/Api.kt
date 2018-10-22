package com.adiliqbl.wallpapers.web

import com.adiliqbl.wallpapers.data.ImageResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class Api(private var imageService: ImageService) {

    fun getImages(page: Int, limit: Int, category: String = "background"): Observable<Response<ImageResponse>> {
        return imageService.getImages(page = page, limit = limit, category = category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}