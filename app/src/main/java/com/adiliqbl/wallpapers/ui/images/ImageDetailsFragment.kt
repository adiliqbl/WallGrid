package com.adiliqbl.wallpapers.ui.images

import android.os.Bundle
import android.view.View
import com.adiliqbl.wallpapers.R
import com.adiliqbl.wallpapers.data.Image
import com.adiliqbl.wallpapers.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_image_details.*
import kotlin.reflect.KClass

class ImageDetailsFragment : BaseFragment<ImageDetailsViewModel>() {

    companion object {
        private const val KEY_IMAGE = "image"

        @JvmStatic
        fun newInstance(image: Image): ImageDetailsFragment {
            val fragment = ImageDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_IMAGE, image)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_image_details
    }

    override fun getViewModelClass(): KClass<ImageDetailsViewModel> {
        return ImageDetailsViewModel::class
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null && arguments != null) {
            viewModel.setData(arguments!!.getSerializable(KEY_IMAGE) as Image)
        }

        toolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        Glide.with(this)
                .load(viewModel.image!!.largeImageURL)
                .apply(RequestOptions().centerCrop().fitCenter())
                .into(image)
    }
}