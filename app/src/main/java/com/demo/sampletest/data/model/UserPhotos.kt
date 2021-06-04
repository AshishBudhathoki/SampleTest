package com.demo.sampletest.data.model

import android.os.Parcelable


data class UserPhotos(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
