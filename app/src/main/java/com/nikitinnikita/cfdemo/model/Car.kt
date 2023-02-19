package com.nikitinnikita.cfdemo.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity
data class Car(

    @PrimaryKey()
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "vin")
    @SerializedName("vin")
    val vin: String,

    @ColumnInfo(name = "year")
    @SerializedName("year")
    val year: String,

    @ColumnInfo(name = "make")
    @SerializedName("make")
    val make: String,

    @ColumnInfo(name = "model")
    @SerializedName("model")
    val model: String,

    @ColumnInfo(name = "trim")
    @SerializedName("trim")
    val trim: String,

    @Embedded
    @SerializedName("dealer")
    val dealer: Dealer,

    @ColumnInfo(name = "mileage")
    @SerializedName("mileage")
    val mileage: Int,

    @ColumnInfo(name = "currentPrice")
    @SerializedName("currentPrice")
    val currentPrice: Int,

    @ColumnInfo(name = "exteriorColor")
    @SerializedName("exteriorColor")
    val exteriorColor: String,

    @ColumnInfo(name = "interiorColor")
    @SerializedName("interiorColor")
    val interiorColor: String,

    @ColumnInfo(name = "engine")
    @SerializedName("engine")
    val engine: String,

    @ColumnInfo(name = "drivetype")
    @SerializedName("drivetype")
    val drivetype: String,

    @ColumnInfo(name = "transmission")
    @SerializedName("transmission")
    val transmission: String,

    @ColumnInfo(name = "bodytype")
    @SerializedName("bodytype")
    val bodytype: String,

    @ColumnInfo(name = "fuel")
    @SerializedName("fuel")
    val fuel: String,

    @Embedded
    @SerializedName("images")
    val images: Images
) {
    constructor() : this(vin = "3321321", dealer = Dealer(phone = "1321321", city = "Toronto", state = "ON"),
        mileage = 112321, currentPrice = 12312, exteriorColor = "black", interiorColor = "white",
        engine = "4 Cyl", drivetype = "FWD", transmission = "manual", bodytype =  "SUV",
        images = Images(photoSize = PhotoSize(large = "https://i.gaw.to/vehicles/photos/40/26/402628-2022-ford-explorer.jpg")), id = "-1",
        year = "2013", make = "Ford", model = "x4", trim = "good", fuel = "Gasoline") {

    }
}
