package com.adiliqbl.wallpapers.ui.images

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        private const val KEY_TRANSITION_NAME = "transitionName"

        @JvmStatic
        fun newInstance(image: Image, transitionName: String): ImageDetailsFragment {
            val fragment = ImageDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_IMAGE, image)
            bundle.putString(KEY_TRANSITION_NAME, transitionName)
            fragment.arguments = bundle

            val transitionSet = TransitionSet()
            transitionSet.ordering = android.support.transition.TransitionSet.ORDERING_TOGETHER
            transitionSet.addTransition(ChangeBounds())
                    .addTransition(ChangeTransform())
                    .addTransition(ChangeImageTransform())

            fragment.sharedElementEnterTransition = transitionSet
            // fragment.enterTransition = Fade()
            // fragment.exitTransition = Fade()
            fragment.sharedElementReturnTransition = transitionSet

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState).apply {
            val transitionName = arguments?.getString(KEY_TRANSITION_NAME)
            ViewCompat.setTransitionName(this.findViewById(R.id.image), transitionName)
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