package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import com.infinity8.mvvm_clean_base.databinding.FragmentDetailsBinding
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.utils.loadImageNormal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = PaginatedListFragmentArgs.fromBundle(requireArguments()).photo
        binding.loadImg.loadImageNormal(photo.src.large2x.toString())

    }

}