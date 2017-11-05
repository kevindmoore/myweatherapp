package com.mastertechsoftware.myweatherapp.weatherService

import com.mastertechsoftware.myweatherapp.models.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 */
interface WeatherAPI {
    @GET("weather?APPID=ddbab4cf5fd5fd708934c414829bbc79&units=imperial")
    fun getZipWeather(@Query("zip") zip: Int): Single<WeatherModel>
    @GET("weather?APPID=ddbab4cf5fd5fd708934c414829bbc79&units=imperial")
    fun getCityWeather(@Query("q") city: String): Single<WeatherModel>
    @GET("weather?APPID=ddbab4cf5fd5fd708934c414829bbc79&units=imperial")
    fun getLocationWeather(@Query("lat") lat: Int, @Query("long") long: Int): Single<WeatherModel>

}