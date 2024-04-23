package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.infinity8.mvvm_clean_base.R
import com.infinity8.mvvm_clean_base.controller.Callbacks
import com.infinity8.mvvm_clean_base.controller.UICallback
import com.infinity8.mvvm_clean_base.databinding.FragmentSimpleListFragementBinding
import com.infinity8.mvvm_clean_base.model.CuratedImageModel
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.ui.adapter.PopularImgAdapter
import com.infinity8.mvvm_clean_base.utils.checkNetwork
import com.infinity8.mvvm_clean_base.utils.handleStateData
import com.infinity8.mvvm_clean_base.utils.launchWithLifecycle
import com.infinity8.mvvm_clean_base.viewmodel.PopularImgViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleListFragment :
    BaseFragment<FragmentSimpleListFragementBinding>(FragmentSimpleListFragementBinding::inflate),
    Callbacks, UICallback {
    private val popularImgViewModel: PopularImgViewModel by activityViewModels()

    private val popularImgAdapter: PopularImgAdapter by lazy {
        PopularImgAdapter(this@SimpleListFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPopular.apply {
            setHasFixedSize(true)
            adapter = popularImgAdapter
        }
        requireContext().checkNetwork(binding.rvPopular, binding.noInternetLbl) { getPhotoList() }
    }

    private fun getPhotoList() {
        popularImgViewModel.getPopularImg()
        viewLifecycleOwner.launchWithLifecycle(
            popularImgViewModel.postFlowSearchPaging,
            Lifecycle.State.CREATED
        ) { paginatedResponse ->
            paginatedResponse.handleStateData(view, this@SimpleListFragment)
        }
    }

    override fun <T> successResponse(result: T) {
        val resList = result as CuratedImageModel
        popularImgAdapter.diffCall.submitList(resList.photos)
    }

    override fun recyclerviewItemClick(photo: Photo?) = findNavController().navigate(
        R.id.action_simpleListFragment_to_detailsFragment,
        bundleOf("photo" to photo)
    )

    override fun <T> loadingNetwork(result: T) {
        val res = result as Boolean
        if (res) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.GONE
        }
    }

}