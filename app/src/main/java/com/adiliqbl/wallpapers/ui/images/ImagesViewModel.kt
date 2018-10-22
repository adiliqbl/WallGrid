package com.adiliqbl.wallpapers.ui.images

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.adiliqbl.wallpapers.data.Filter
import com.adiliqbl.wallpapers.data.Image
import com.adiliqbl.wallpapers.util.Status
import com.adiliqbl.wallpapers.web.Api
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

private const val PAGE_SIZE = 80

class ImagesViewModel
@Inject constructor(api: Api) : ViewModel() {
    private var activeFilter = Filter("backgrounds", null)
    var filters: List<Filter> = arrayListOf(
            Filter("nature", "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823_960_720.jpg"),
            Filter("science", "https://cdn.pixabay.com/photo/2016/11/09/15/27/dna-1811955_960_720.jpg"),
            Filter("education", "https://cdn.pixabay.com/photo/2014/09/05/18/32/old-books-436498_960_720.jpg"),
            Filter("backgrounds", "https://cdn.pixabay.com/photo/2018/09/19/23/03/sunset-3689760_960_720.jpg"),
            Filter("places", "https://cdn.pixabay.com/photo/2017/12/10/17/40/prague-3010407_960_720.jpg"),
            Filter("animals", "https://cdn.pixabay.com/photo/2018/08/12/16/59/ara-3601194_960_720.jpg"),
            Filter("industry", "https://cdn.pixabay.com/photo/2018/08/31/19/16/fan-3645379_960_720.jpg"),
            Filter("food", "https://cdn.pixabay.com/photo/2018/09/26/21/24/sweet-corn-3705687_960_720.jpg"),
            Filter("computer", "https://cdn.pixabay.com/photo/2014/05/02/21/49/home-office-336373_960_720.jpg"),
            Filter("transportation", "https://cdn.pixabay.com/photo/2017/06/26/08/43/ribblehead-viaduct-2443085_960_720.jpg"),
            Filter("travel", "https://cdn.pixabay.com/photo/2016/01/13/01/36/bagan-1137015_960_720.jpg"),
            Filter("buildings", "https://cdn.pixabay.com/photo/2015/11/17/18/59/architecture-1048092_960_720.jpg"),
            Filter("business", "https://cdn.pixabay.com/photo/2018/02/14/10/28/business-3152586_960_720.jpg"))
            .shuffled()
        private set

    private val compositeDisposable = CompositeDisposable()
    private val imagesSourceFactory: ImagesDataSourceFactory = ImagesDataSourceFactory(compositeDisposable, api, activeFilter)
    var imageList: LiveData<PagedList<Image>> = LivePagedListBuilder<Int, Image>(imagesSourceFactory,
            PagedList.Config.Builder()
                    .setEnablePlaceholders(true)
                    .setPageSize(PAGE_SIZE)
                    .setInitialLoadSizeHint(PAGE_SIZE)
                    .setPrefetchDistance(0)
                    .build()).build()

    fun onFilterChange(filter: Filter) {
        activeFilter.name = filter.name
        refresh()
    }

    fun getStatus(): LiveData<Status> = Transformations.switchMap<ImagesDataSource,
            Status>(imagesSourceFactory.imagesDataSourceLiveData, ImagesDataSource::state)

    fun refresh() {
        imagesSourceFactory.imagesDataSourceLiveData.value?.invalidate()
    }

    fun retry() {
        imagesSourceFactory.imagesDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return imageList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
