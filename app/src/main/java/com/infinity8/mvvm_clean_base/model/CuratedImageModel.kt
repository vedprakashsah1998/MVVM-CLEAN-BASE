package com.infinity8.mvvm_clean_base.model

data class CuratedImageModel(
    val next_page: String?,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val prev_page: String?,
    val total_results: Int
)

data class Photo(
    val alt: String,
    val avg_color: String?,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: Src,
    val url: String,
    val width: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Photo
        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}

data class Src(
    val landscape: String?,
    val large: String?,
    val large2x: String?,
    val medium: String?,
    val original: String?,
    val portrait: String?,
    val small: String?,
    val tiny: String?
)
