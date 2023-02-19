package com.nikitinnikita.cfdemo.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.model.repository.CarRepository
import com.nikitinnikita.cfdemo.utils.Utils
import io.reactivex.rxjava3.kotlin.toObservable
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    private var _car = mutableStateOf(Car())

    val car: Car
        get() = _car.component1()

    fun getCar(context: Context, vin: String) {
        viewModelScope.launch {
           _car.value = CarRepository.getCar(context, vin)
        }
    }

    fun callDealer(context: Context, car: Car) {
        Utils.makeCall(context, car.dealer.phone)
    }
}