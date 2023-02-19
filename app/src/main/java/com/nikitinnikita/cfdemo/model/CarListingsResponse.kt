package com.nikitinnikita.cfdemo.model

import com.google.gson.annotations.SerializedName
import com.nikitinnikita.cfdemo.model.Car

data class CarListingsResponse(
    @SerializedName("listings")
    val car: List<Car>
)
