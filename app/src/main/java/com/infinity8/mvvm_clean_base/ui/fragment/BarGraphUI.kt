package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinity8.mvvm_clean_base.databinding.FragmentBarGraphUIBinding
import com.infinity8.mvvm_clean_base.model.BarModel
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.ui.adapter.RvBarGraphAdapter


class BarGraphUI : BaseFragment<FragmentBarGraphUIBinding>(FragmentBarGraphUIBinding::inflate) {

    private val rvBarGraphAdapter: RvBarGraphAdapter by lazy {
        RvBarGraphAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvBar.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = rvBarGraphAdapter
        }
        rvBarGraphAdapter.diffCall.submitList(
            listOf(
                BarModel("Jan", 10),
                BarModel("Feb", 7),
                BarModel("Mar", 8),
                BarModel("Apr", 6),
                BarModel("May", 23),
                BarModel("Jun", 7),
                BarModel("Jul", 33)
            )
        )
    }

}