/*
 *
 *   Created by Ved Prakash on 4/1/24, 9:30 AM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 9:30 AM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.databinding.CuratedItemBinding
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.utils.diff.DiffCallback
import com.infinity8.mvvm_clean_base.utils.loadImage

internal class CuratedPagedAdapter :
    PagingDataAdapter<Photo, CuratedPagedAdapter.CuratedPageViewHolder>(
        DiffCallback()
    ) {
    inner class CuratedPageViewHolder(private val binding: CuratedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo?) {
            binding.artistName.text = photo?.photographer
            binding.artistBody.text = photo?.alt
            binding.imgSrc.loadImage(photo?.src?.large2x.toString())
        }
    }

    override fun onBindViewHolder(holder: CuratedPageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = CuratedPageViewHolder(
        CuratedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}