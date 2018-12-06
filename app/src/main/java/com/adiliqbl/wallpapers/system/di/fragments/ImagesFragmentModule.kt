package com.adiliqbl.wallpapers.system.di.fragments

import com.adiliqbl.wallpapers.system.di.scopes.FragmentScope
import com.adiliqbl.wallpapers.ui.images.ImagesFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides

/**
 * This is a Dagger module. We use this to bind our Application class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 */
@Module
class ImagesFragmentModule {

    @Provides
    @FragmentScope
    fun provideGlide(fragment: ImagesFragment): RequestManager {
        return Glide.with(fragment)
    }
}