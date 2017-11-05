package com.mastertechsoftware.myweatherapp.weatherService

import com.mastertechsoftware.myweatherapp.models.WeatherModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Query

/**
 *
 */
object WeatherService {
    private val service : WeatherAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        service = retrofit.create(WeatherAPI::class.java)
    }

    fun getZipWeather(zip : Int) : Single<WeatherModel> {
        return service.getZipWeather(zip)
    }

    fun getCityWeather(@Query("q") city: String) : Single<WeatherModel> {
        return service.getCityWeather(city)
    }
    fun getLocationWeather(@Query("lat") lat: Int, @Query("long") long: Int) : Single<WeatherModel> {
        return service.getLocationWeather(lat, long)
    }
}