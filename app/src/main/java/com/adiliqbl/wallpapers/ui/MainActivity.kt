package com.adiliqbl.wallpapers.ui

import android.os.Bundle
import com.adiliqbl.wallpapers.R
import com.adiliqbl.wallpapers.ui.images.ImagesFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ImagesFragment.newInstance()).commitNow()
        }
    }
}
