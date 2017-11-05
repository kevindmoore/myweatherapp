package com.mastertechsoftware.myweatherapp.models

/**
 * Starting point for the models from OpenWeather
 */
data class WeatherModel(var weather: List<Weather> = ArrayList<Weather>(), var main : Main = Main(), var wind: Wind = Wind(), var name : String = "" )