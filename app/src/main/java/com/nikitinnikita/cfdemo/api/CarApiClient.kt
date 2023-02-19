package com.nikitinnikita.cfdemo.api

import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.model.CarListingsResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object CarApiClient: CarApiService {

    private val service: CarApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://carfax-for-consumers.firebaseio.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(CarApiService::class.java)
    }

    override suspend fun getAllCars(): Response<CarListingsResponse> {
        return service.getAllCars()
    }


}