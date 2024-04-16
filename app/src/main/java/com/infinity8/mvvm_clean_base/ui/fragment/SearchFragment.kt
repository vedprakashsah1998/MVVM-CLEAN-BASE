package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import com.infinity8.mvvm_clean_base.databinding.FragmentSearchBinding
import com.infinity8.mvvm_clean_base.ui.BaseFragment


class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressCircular.setProgress(40F)
        binding.progressCircular.setMaxProgress(100F)
//        binding.progressCircular.setDrawableResource(R.drawable.baseline_currency_bitcoin_24)
    }

}