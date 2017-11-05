package com.mastertechsoftware.myweatherapp.weatherService

import com.mastertechsoftware.myweatherapp.models.WeatherModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Helper methods for searches
 */
object SearchResults {
    fun getZipWeather(zip: String): Single<WeatherModel> {
        return WeatherService.getZipWeather(zip.toInt())
                .subscribeOn(Schedulers.io())
                .map { weather: WeatherModel -> weather }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { WeatherModel(name = "Not Found") }
     }

    fun getCityWeather(city: String): Single<WeatherModel> {
        return WeatherService.getCityWeather(city)
                .subscribeOn(Schedulers.io())
                .map { weather: WeatherModel -> weather }
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { WeatherModel(name = "Not Found") }
    }

}