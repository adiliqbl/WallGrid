package com.adiliqbl.wallpapers.system.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.adiliqbl.wallpapers.system.ViewModelFactory
import com.adiliqbl.wallpapers.system.di.qualifiers.ViewModelKey
import com.adiliqbl.wallpapers.ui.images.ImageDetailsViewModel
import com.adiliqbl.wallpapers.ui.images.ImagesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ImagesViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ImagesViewModel::class)
    abstract fun bindImagesViewModel(viewModel: ImagesViewModel): ViewModel

    /**
     * The ViewModels are created by Dagger in a map. Via the @ViewModelKey, we define that we
     * want to get a [ImageDetailsViewModel] class.
     */
    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailsViewModel::class)
    abstract fun bindImageDetailsViewModel(viewModel: ImageDetailsViewModel): ViewModel
}