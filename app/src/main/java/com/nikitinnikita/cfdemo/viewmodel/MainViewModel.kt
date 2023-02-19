package com.nikitinnikita.cfdemo.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikitinnikita.cfdemo.model.Car
import com.nikitinnikita.cfdemo.model.repository.CarRepository
import com.nikitinnikita.cfdemo.utils.Utils
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _cars = mutableStateListOf<Car>()

    val cars: List<Car>
        get() = _cars

    fun getAllCars(context: Context) {
        viewModelScope.launch {
            _cars.addAll(CarRepository.getAllCars(context))
        }
    }

    fun callDealer(context: Context, car: Car) {
        Utils.makeCall(context, car.dealer.phone)
    }

}