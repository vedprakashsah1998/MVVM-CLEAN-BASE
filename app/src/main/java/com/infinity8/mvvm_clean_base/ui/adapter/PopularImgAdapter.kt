/*
 *
 *   Created by Ved Prakash on 4/1/24, 5:09 PM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 5:09 PM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.databinding.CuratedItemBinding
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.utils.diff.createAsyncListDifferWithDiffCallback
import com.infinity8.mvvm_clean_base.utils.loadImageNormal

class PopularImgAdapter : RecyclerView.Adapter<PopularImgAdapter.PopularImgHolder>() {
    inner class PopularImgHolder(private val binding: CuratedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo?) {
            binding.artistName.text = photo?.photographer
            binding.artistBody.text = photo?.alt
            binding.imgSrc.loadImageNormal(photo?.src?.large2x.toString())
        }
    }

    val diffCall = createAsyncListDifferWithDiffCallback<Photo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PopularImgHolder(
            CuratedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = diffCall.currentList.size

    override fun onBindViewHolder(holder: PopularImgHolder, position: Int) {
        holder.bind(diffCall.currentList[position])
    }
}