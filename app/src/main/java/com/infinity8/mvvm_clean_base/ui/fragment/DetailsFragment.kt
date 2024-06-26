package com.infinity8.mvvm_clean_base.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinity8.mvvm_clean_base.databinding.FragmentDetailsBinding
import com.infinity8.mvvm_clean_base.ui.BaseFragment
import com.infinity8.mvvm_clean_base.utils.loadImageNormal
import com.infinity8.mvvm_clean_base.workmanager.ImageDownloadWorker3
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {
    private val arguments by navArgs<DetailsFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val workManager = WorkManager.getInstance(requireContext())
        binding.downloadBtn.setOnClickListener {
            val inputData = workDataOf("IMAGE_URL" to arguments.photo.src.original.toString())

            val downloadRequest = OneTimeWorkRequestBuilder<ImageDownloadWorker3>()
                .setInputData(inputData)
                .build()

            workManager.enqueue(downloadRequest)
        }
        binding.loadImg.loadImageNormal(arguments.photo.src.large2x.toString())
    }

}