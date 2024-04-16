/*
 *
 *   Created by Ved Prakash on 4/16/24, 5:39 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/16/24, 5:39 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.databinding.RvBarGraphItemBinding
import com.infinity8.mvvm_clean_base.model.BarModel
import com.infinity8.mvvm_clean_base.utils.diff.createAsyncListDifferWithDiffCallback
import kotlin.math.roundToInt

class RvBarGraphAdapter : RecyclerView.Adapter<RvBarGraphAdapter.RvBarHolder>() {
    inner class RvBarHolder( val binding: RvBarGraphItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RvBarHolder(
            RvBarGraphItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    val diffCall = createAsyncListDifferWithDiffCallback<BarModel>()

    override fun getItemCount() = diffCall.currentList.size

    override fun onBindViewHolder(holder: RvBarHolder, position: Int) {
        val data = diffCall.currentList[position]
        holder.binding.monthName.text = data.monthName

        // Calculate height from bottom to top
        val customHeight = calculateCustomHeight(data.barValue)
        val layoutParams = holder.binding.barItem.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = customHeight
        // Adjust top margin to keep text view fixed
        layoutParams.topMargin = dpToPx(200) - customHeight // Adjust as needed
        holder.binding.barItem.layoutParams = layoutParams
    }
    private fun calculateCustomHeight(value: Int): Int {
        // Your logic to calculate height based on the value
        // Adjust this logic based on your requirements
        // Here, height increases from bottom to top
        return dpToPx(value * 5) // Calculate height from bottom to top based on value
    }
    private fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

}