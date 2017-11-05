package com.mastertechsoftware.myweatherapp.views

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
    lateinit var weatherModel: WeatherModel

    // Set the model
    fun initialize(weatherModel: WeatherModel) {
        this.weatherModel = weatherModel
    }

    // Create the view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.search_results, container, false)
    }


    override fun onAttach(view: View) {
        super.onAttach(view)
        view.find<TextView>(R.id.weather_for).text = activity?.getString(R.string.weather_for, weatherModel.name)
        if (weatherModel.weather.size > 0) {
            val weather = weatherModel.weather[0]
            Glide.with(activity).load("http://openweathermap.org/img/w/${weather.icon}.png").into(view.find(R.id.weather_icon))
            view.find<TextView>(R.id.description).text = activity?.getString(R.string.description, weather.description)
        } else {
            view.find<TextView>(R.id.description).text = activity?.getString(R.string.search_failed)
        }
        view.find<TextView>(R.id.temp).text = activity?.getString(R.string.temp, weatherModel.main.temp)
        view.find<TextView>(R.id.wind).text = activity?.getString(R.string.wind, weatherModel.wind.speed)
        view.find<TextView>(R.id.humidity).text = activity?.getString(R.string.humidity, weatherModel.main.humidity)
    }
}