package com.adiliqbl.wallpapers.ui.images

import android.arch.paging.PagedListAdapter
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.adiliqbl.wallpapers.R
import com.adiliqbl.wallpapers.data.Image
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ImagesPagedAdapter(private val glide: RequestManager, private val listener: ImagesListener) : PagedListAdapter<Image, ImagesPagedAdapter.ViewHolder>(Image.DIFF_CALLBACK) {

    interface ImagesListener {
        fun onClick(view: ImageView, image: Image, transitionName: String)
    }

    private var requestOptions = RequestOptions()
            .override(500, 500)
            .centerCrop().fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    private var thumbnailOptions = RequestOptions()
            .override(100, 100)
            .centerCrop().fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesPagedAdapter.ViewHolder {
        requestOptions = requestOptions
                .placeholder(ColorDrawable(ContextCompat.getColor(parent.context, R.color.shimmer_color)))
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ImagesPagedAdapter.ViewHolder, position: Int) {
        ViewCompat.setTransitionName(holder.image, "picture_$position")

        val image = getItem(position) as Image
        if (image.largeImageURL.isNullOrEmpty()) return

        val m = glide
                .load(image.largeImageURL)
                .apply(requestOptions)

        if (!image.previewURL.isNullOrEmpty()) m.thumbnail(Glide
                .with(holder.itemView.context)
                .load(image.previewURL)
                .apply(thumbnailOptions))

        m.into(holder.image)

        holder.itemView.setOnClickListener { listener.onClick(holder.image, getItem(position)!!, "picture_$position") }

        holder.image.transitionName = holder.itemView.context.getString(R.string.transition_image)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.image)
    }
}