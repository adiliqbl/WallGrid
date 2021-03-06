package com.adiliqbl.wallpapers.system.di

import android.content.Context
import com.adiliqbl.wallpapers.system.Application
import dagger.Binds
import dagger.Module
import javax.inject.Singleton
import dagger.Provides



/**
 * This is a Dagger module. We use this to bind our Application class as a Context in the AppComponent
 * By using Dagger Android we do not need to pass our Application instance to any module,
 * we simply need to expose our Application as Context.
 */
@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}