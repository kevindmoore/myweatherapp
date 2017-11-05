package com.mastertechsoftware.myweatherapp.views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.bumptech.glide.Glide
import com.mastertechsoftware.myweatherapp.R
import com.mastertechsoftware.myweatherapp.models.WeatherModel
import com.pawegio.kandroid.find

/**
 * Show the results of the search
 */
class SearchResultsView : Controller() {
    lateinit var weather: WeatherModel

    fun initialize(weather: WeatherModel) {
        this.weather = weather
        Log.d("Kevin", "Weather $weather")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.search_results, container, false)
    }
    override fun onAttach(view: View) {
        super.onAttach(view)
        view.find<TextView>(R.id.weather_for).text = activity?.getString(R.string.weather_for, weather.name)
        view.find<TextView>(R.id.description).text = activity?.getString(R.string.description, weather.weather[0].description)
        view.find<TextView>(R.id.temp).text = activity?.getString(R.string.temp, weather.main.temp)
        view.find<TextView>(R.id.wind).text = activity?.getString(R.string.wind, weather.wind.speed)
        view.find<TextView>(R.id.humidity).text = activity?.getString(R.string.humidity, weather.main.humidity)
        Glide.with(activity)
            .load("http://openweathermap.org/img/w/${weather.weather[0].icon}.png")
            .into(view.find(R.id.weather_icon))
    }
}