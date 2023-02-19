package com.nikitinnikita.cfdemo.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Images(
    @Embedded
    @SerializedName("firstPhoto")
    val photoSize: PhotoSize

)

data class PhotoSize(
    @SerializedName("large")
    val large: String
)