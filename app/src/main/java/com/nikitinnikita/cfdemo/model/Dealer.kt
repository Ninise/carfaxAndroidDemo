package com.nikitinnikita.cfdemo.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Dealer(

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    val phone: String,

    @ColumnInfo(name = "city")
    @SerializedName("city")
    val city: String,

    @ColumnInfo(name = "state")
    @SerializedName("state")
    val state: String

)
