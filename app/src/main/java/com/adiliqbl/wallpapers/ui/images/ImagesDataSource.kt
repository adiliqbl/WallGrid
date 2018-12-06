package com.adiliqbl.wallpapers.ui.images

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.adiliqbl.wallpapers.data.Filter
import com.adiliqbl.wallpapers.data.Image
import com.adiliqbl.wallpapers.util.Status
import com.adiliqbl.wallpapers.web.Api
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ImagesDataSourceFactory(private val compositeDisposable: CompositeDisposable, private val api: Api, private val filter: Filter) : DataSource.Factory<Int, Image>() {

    val imagesDataSourceLiveData = MutableLiveData<ImagesDataSource>()

    override fun create(): DataSource<Int, Image> {
        val imagesDataSource = ImagesDataSource(compositeDisposable, api, filter)
        imagesDataSourceLiveData.postValue(imagesDataSource)
        return imagesDataSource
    }
}

class ImagesDataSource(private val compositeDisposable: CompositeDisposable, private val api: Api, private val filter: Filter) : PageKeyedDataSource<Int, Image>() {

    var state: MutableLiveData<Status> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Image>) {
        updateState(Status.LOADING)
        compositeDisposable.add(
                api.getImages(1, params.requestedLoadSize, filter.name)
                        .subscribeBy(
                                onNext = {
                                    if (it.isSuccessful) {
                                        updateState(Status.SUCCESS)
                                        callback.onResult(it.body()!!.hits, null, 2)
                                    } else {
                                        updateState(Status.ERROR)
                                        setRetry(Action { loadInitial(params, callback) })
                                    }
                                },
                                onError = {
                                    Timber.tag(ImagesDataSource::class.simpleName).e(it)

                                    updateState(Status.ERROR)
                                    setRetry(Action { loadInitial(params, callback) })
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
        updateState(Status.LOADING)
        compositeDisposable.add(
                api.getImages(params.key, params.requestedLoadSize, filter.name)
                        .subscribeBy(
                                onNext = {
                                    if (it.isSuccessful) {
                                        updateState(Status.SUCCESS)
                                        callback.onResult(it.body()!!.hits, params.key + 1)
                                    } else {
                                        updateState(Status.ERROR)
                                        setRetry(Action { loadAfter(params, callback) })
                                    }
                                },
                                onError = {
                                    Timber.tag(ImagesDataSource::class.simpleName).e(it)

                                    updateState(Status.ERROR)
                                    setRetry(Action { loadAfter(params, callback) })
                                }
                        )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
    }

    private fun updateState(state: Status) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}