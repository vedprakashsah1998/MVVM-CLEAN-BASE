package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.infinity8.mvvm_clean_base.R
import com.infinity8.mvvm_clean_base.controller.ApiPaginatedListCallback
import com.infinity8.mvvm_clean_base.controller.UICallback
import com.infinity8.mvvm_clean_base.databinding.FragmentPaginatedListBinding
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.ui.adapter.CuratedPagedAdapter
import com.infinity8.mvvm_clean_base.ui.adapter.MainLoadStateAdapter
import com.infinity8.mvvm_clean_base.utils.flowWithLifecycleUI
import com.infinity8.mvvm_clean_base.utils.handlePaginatedCallback
import com.infinity8.mvvm_clean_base.utils.isNetworkAvailable
import com.infinity8.mvvm_clean_base.utils.showErrorSnackBar
import com.infinity8.mvvm_clean_base.viewmodel.CuratedImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaginatedListFragment :
    BaseFragment<FragmentPaginatedListBinding>(FragmentPaginatedListBinding::inflate),
    ApiPaginatedListCallback<Photo>, UICallback {

    private val curatedPageAdapter: CuratedPagedAdapter by lazy {
        CuratedPagedAdapter(this@PaginatedListFragment)
    }
    private val curatedImageViewModel: CuratedImageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCurated.apply {
            setHasFixedSize(true)
            adapter = curatedPageAdapter.withLoadStateFooter(footer = MainLoadStateAdapter())
        }
        if (requireContext().isNetworkAvailable()) {
            binding.rvCurated.visibility = View.VISIBLE
            binding.noInternetLbl.visibility = View.GONE
            getPhotoList()
        } else {
            binding.rvCurated.visibility = View.GONE
            binding.noInternetLbl.visibility = View.VISIBLE
        }

    }

    override fun recyclerviewItemClick(photo: Photo?) = findNavController().navigate(
        R.id.action_paginatedListFragment_to_detailsFragment,
        bundleOf("photo" to photo)
    )

    private fun getPhotoList() {
        curatedImageViewModel.getCuratedImage()
        loadProductIntoList()
        viewLifecycleOwner.flowWithLifecycleUI(
            curatedImageViewModel.postFlowSearchPaging,
            Lifecycle.State.CREATED
        ) { paginatedResponse ->
            paginatedResponse.handlePaginatedCallback(this, this)
        }
    }

    private fun loadProductIntoList() = curatedPageAdapter.addLoadStateListener { loadState ->
        when (val currentState = loadState.refresh) {
            is LoadState.Loading -> {
                binding.progress.visibility = View.VISIBLE
            }

            is LoadState.Error -> {
                val extractedException = currentState.error
                showErrorSnackBar(extractedException.message.toString())
                binding.progress.visibility = View.GONE
            }

            is LoadState.NotLoading -> {
                binding.progress.visibility = View.GONE
            }
        }

    }

    override fun successPaging(list: PagingData<Photo>) = curatedPageAdapter.submitData(
        viewLifecycleOwner.lifecycle,
        list
    )

    override fun <T> loading(result: T) {
        val loadRes = result as Boolean
        if (loadRes) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.GONE
        }
    }

}