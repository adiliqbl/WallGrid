package com.adiliqbl.wallpapers.system.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.adiliqbl.wallpapers.web.Api
import com.adiliqbl.wallpapers.web.ImageService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val URL = "https://pixabay.com/api/"
const val IMAGE_API_KEY = "10412109-5f22a6181598c78e393d0dbd9"

@Module
object RepositoryModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("Accept", "application/json").build()
                    chain.proceed(request)
                }
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient)
                .baseUrl(URL)
                .build()
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideApi(imageService: ImageService): Api {
        return Api(imageService)
    }

    @Singleton
    @Provides
    @JvmStatic
    fun provideImageService(retrofit: Retrofit): ImageService {
        return retrofit.create(ImageService::class.java)!!
    }
}