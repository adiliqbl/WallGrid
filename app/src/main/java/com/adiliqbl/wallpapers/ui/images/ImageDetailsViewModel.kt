package com.adiliqbl.wallpapers.ui.images

import android.arch.lifecycle.ViewModel
import com.adiliqbl.wallpapers.data.Image
import javax.inject.Inject

class ImageDetailsViewModel @Inject constructor() : ViewModel() {
    var image: Image? = null

    fun setData(image: Image) {
        this.image = image
    }
}