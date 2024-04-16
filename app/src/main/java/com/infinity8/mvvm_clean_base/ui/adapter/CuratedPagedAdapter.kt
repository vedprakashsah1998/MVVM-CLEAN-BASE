/*
 *
 *   Created by Ved Prakash on 4/1/24, 11:45 AM
 *   Copyright (c) 2024 . All rights reserved.
 *   Last modified 4/1/24, 11:32 AM
 *   Organization: NeoSoft
 *
 */

package com.infinity8.mvvm_clean_base.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.infinity8.mvvm_clean_base.controller.UICallback
import com.infinity8.mvvm_clean_base.databinding.CuratedItemBinding
import com.infinity8.mvvm_clean_base.model.Photo
import com.infinity8.mvvm_clean_base.utils.loadImageNormal

internal class CuratedPagedAdapter(val uiCallback: UICallback) :
    PagingDataAdapter<Photo, CuratedPagedAdapter.CuratedPageViewHolder>(
        PhotoComparator
    ) {
    object PhotoComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    inner class CuratedPageViewHolder(private val binding: CuratedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo?) {
            binding.artistName.text = photo?.photographer
            binding.artistBody.text = photo?.alt
            binding.imgSrc.loadImageNormal(photo?.src?.large2x.toString())

            binding.root.setOnClickListener {
                val adapterPosition = bindingAdapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    uiCallback.recyclerviewItemClick(photo)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CuratedPageViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = CuratedPageViewHolder(
        CuratedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}