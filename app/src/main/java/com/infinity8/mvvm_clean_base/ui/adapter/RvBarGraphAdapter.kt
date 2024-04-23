/*
 *
 *   Created by Ved Prakash on 4/16/24, 5:39 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/16/24, 5:39 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.R
import com.infinity8.mvvm_clean_base.databinding.RvBarGraphItemBinding
import com.infinity8.mvvm_clean_base.model.BarModel
import com.infinity8.mvvm_clean_base.utils.diff.createAsyncListDifferWithDiffCallback
import kotlin.math.roundToInt

class RvBarGraphAdapter(val context: Context) :
    RecyclerView.Adapter<RvBarGraphAdapter.RvBarHolder>() {
    inner class RvBarHolder(val binding: RvBarGraphItemBinding) :
        RecyclerView.ViewHolder(binding.root)

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
    private var selectedItemPosition = RecyclerView.NO_POSITION
    override fun onBindViewHolder(holder: RvBarHolder, position: Int) {
        val data = diffCall.currentList[position]
        holder.binding.monthName.text = data.monthName
        val isItemSelected = position == selectedItemPosition || data.value
        val customHeight = calculateCustomHeight(data.barValue)
        val layoutParams = holder.binding.barItem.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.height = customHeight
        layoutParams.topMargin = dpToPx(200) - customHeight
        holder.binding.barItem.layoutParams = layoutParams

      /*  holder.binding.barItem.backgroundTintList =
            if (position == selectedItemPosition || customHeight == 60) {
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white_fade))
            } else {
                ColorStateList.valueOf(Color.TRANSPARENT)
            }*/
        holder.binding.barItem.backgroundTintList =
            if (isItemSelected) {
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white_fade))
            } else {
                ColorStateList.valueOf(Color.TRANSPARENT)
            }

        holder.binding.monthName.setTextColor(
            if (isItemSelected) {
                ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.darker_gray))
            } else {
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
            }
        )

        holder.binding.selectedColor.backgroundTintList =
            if (isItemSelected) {
                ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_purple))

            } else {
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white_fade))
            }
        holder.binding.barItem.setOnClickListener {
            data.value = !data.value // Toggle the value state for this item
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = if (data.value) position else RecyclerView.NO_POSITION
            notifyItemChanged(previousSelectedItemPosition)
            notifyItemChanged(selectedItemPosition)
        }
    }

    private fun calculateCustomHeight(value: Int) = dpToPx(value * 5)
    private fun dpToPx(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).roundToInt()
    }

}