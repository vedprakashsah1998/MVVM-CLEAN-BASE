package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.infinity8.mvvm_clean_base.controller.Callbacks
import com.infinity8.mvvm_clean_base.databinding.FragmentSimpleListFragementBinding
import com.infinity8.mvvm_clean_base.model.CuratedImageModel
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.ui.adapter.PopularImgAdapter
import com.infinity8.mvvm_clean_base.utils.handleStateData
import com.infinity8.mvvm_clean_base.utils.isNetworkAvailable
import com.infinity8.mvvm_clean_base.utils.launchWithLifecycle
import com.infinity8.mvvm_clean_base.viewmodel.PopularImgViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimpleListFragment :
    BaseFragment<FragmentSimpleListFragementBinding>(FragmentSimpleListFragementBinding::inflate),
    Callbacks {
    private val popularImgViewModel: PopularImgViewModel by activityViewModels()

    private val popularImgAdapter: PopularImgAdapter by lazy {
        PopularImgAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvPopular.apply {
            setHasFixedSize(true)
            adapter = popularImgAdapter
        }
        if (requireContext().isNetworkAvailable()) {
            binding.rvPopular.visibility = View.VISIBLE
            binding.noInternetLbl.visibility = View.GONE
            getPhotoList()
        } else {
            binding.rvPopular.visibility = View.GONE
            binding.noInternetLbl.visibility = View.VISIBLE
        }
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

    override fun <T> loadingNetwork(result: T) {
        val res = result as Boolean
        if (res) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.GONE
        }
    }

}