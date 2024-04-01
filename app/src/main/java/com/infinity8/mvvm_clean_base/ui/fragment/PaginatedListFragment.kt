package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.infinity8.mvvm_clean_base.adapter.CuratedPagedAdapter
import com.infinity8.mvvm_clean_base.adapter.MainLoadStateAdapter
import com.infinity8.mvvm_clean_base.controller.ApiPaginatedListCallback
import com.infinity8.mvvm_clean_base.databinding.FragmentPaginatedListBinding
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.utils.flowWithLifecycleUI
import com.infinity8.mvvm_clean_base.utils.handlePaginatedCallback
import com.infinity8.mvvm_clean_base.utils.isNetworkAvailable
import com.infinity8.mvvm_clean_base.utils.showErrorSnackBar
import com.infinity8.mvvm_clean_base.viewmodel.CuratedImageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaginatedListFragment :
    BaseFragment<FragmentPaginatedListBinding>(FragmentPaginatedListBinding::inflate),
    ApiPaginatedListCallback<Photo> {

    private val curatedPageAdapter: CuratedPagedAdapter by lazy {
        CuratedPagedAdapter()
    }
    private val curatedImageViewModel: CuratedImageViewModel by viewModels()

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

    private fun loadProductIntoList() =
        curatedPageAdapter.addLoadStateListener { loadState ->
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

}