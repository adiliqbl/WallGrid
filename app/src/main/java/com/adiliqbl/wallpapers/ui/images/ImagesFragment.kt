package com.adiliqbl.wallpapers.ui.images

import android.arch.lifecycle.Observer
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.adiliqbl.wallpapers.R
import com.adiliqbl.wallpapers.data.Filter
import com.adiliqbl.wallpapers.data.Image
import com.adiliqbl.wallpapers.ui.base.BaseFragment
import com.adiliqbl.wallpapers.util.Status
import com.adiliqbl.wallpapers.util.toProperCase
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_images.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

class ImagesFragment : BaseFragment<ImagesViewModel>() {

    companion object {
        fun newInstance() = ImagesFragment()
    }

    private lateinit var filtersAdapter: FiltersAdapter
    private lateinit var imagesAdapter: ImagesPagedAdapter

    @Inject
    lateinit var glide: RequestManager

    override fun getLayoutRes(): Int {
        return R.layout.fragment_images
    }

    override fun getViewModelClass(): KClass<ImagesViewModel> {
        return ImagesViewModel::class
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapters
        val rv = list
        val filterRv = filters

        // Lists
        filtersAdapter = FiltersAdapter(viewModel.filters)
        imagesAdapter = ImagesPagedAdapter(glide, object : ImagesPagedAdapter.ImagesListener {
            override fun onClick(sharedView: ImageView, image: Image) {
                val fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                fragmentTransaction.replace((view.parent as ViewGroup).id, ImageDetailsFragment.newInstance(image), tag)
                fragmentTransaction.addToBackStack(tag)
                fragmentTransaction.addSharedElement(sharedView, getString(R.string.transition_image))
                fragmentTransaction.commit()
            }
        })

        rv.setItemViewCacheSize(20)
        rv.isNestedScrollingEnabled = false
        rv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        rv.adapter = imagesAdapter

        filterRv.setHasFixedSize(true)
        filterRv.isNestedScrollingEnabled = false
        filterRv.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        filterRv.adapter = filtersAdapter

        // Swipe Refresh layout
        swipe_layout.setOnRefreshListener {
            Timber.tag("TESTING").i("onRefresh called from SwipeRefreshLayout")
            viewModel.refresh()
        }

        observeData()
    }

    private fun observeData() {
        viewModel.imageList.observe(this, Observer {
            imagesAdapter.submitList(it)
        })

        viewModel.getStatus().observe(this, Observer {
            if (it == Status.LOADING) {
                swipe_layout.isRefreshing = true
            } else if (it == Status.SUCCESS) {
                if (swipe_layout.isRefreshing) swipe_layout.isRefreshing = false
            } else {
                if (swipe_layout.isRefreshing) swipe_layout.isRefreshing = false
                Toast.makeText(requireContext(), "Unknown error occurred", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun filterImages(filter: Filter) {
        viewModel.onFilterChange(filter)
    }

    inner class FiltersAdapter(val list: List<Filter>?) : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

        private var requestOptionsSmall = RequestOptions()
                .transforms(CenterCrop(), RoundedCorners(12))
                .override(100, 100)

        init {
            requestOptionsSmall = requestOptionsSmall
                    .placeholder(ColorDrawable(ContextCompat.getColor(this@ImagesFragment.requireContext(), R.color.shimmer_color)))
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val filter = list!![position]

            holder.name.text = toProperCase(filter.name)
            Glide.with(this@ImagesFragment)
                    .load(filter.image)
                    .apply(requestOptionsSmall)
                    .into(holder.image)

            holder.itemView.setOnClickListener { filterImages(filter) }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var image: ImageView = view.findViewById(R.id.image)
            var name: TextView = view.findViewById(R.id.name)
        }
    }
}