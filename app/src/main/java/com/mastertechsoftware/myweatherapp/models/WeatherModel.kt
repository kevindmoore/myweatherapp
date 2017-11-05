package com.mastertechsoftware.myweatherapp.models

/**
 *
 */
data class WeatherModel(var weather: List<Weather> = ArrayList<Weather>(), var main : Main, var wind: Wind, var name : String )