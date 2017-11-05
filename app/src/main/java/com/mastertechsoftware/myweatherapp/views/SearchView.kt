package com.mastertechsoftware.myweatherapp.views

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.mastertechsoftware.myweatherapp.R
import com.mastertechsoftware.myweatherapp.models.WeatherModel
import com.mastertechsoftware.myweatherapp.preferences.Prefs
import com.mastertechsoftware.myweatherapp.weatherService.WeatherService
import com.pawegio.kandroid.find
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Show the search fields.
 */
class SearchView : Controller() {
    lateinit var prefs : Prefs

    /**
     * Inflate teh search view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.search_view, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        val radioGroup = view.find<RadioGroup>(R.id.radioGroup)
        val city = view.find<RadioButton>(R.id.usRadio)
        val search = view.find<Button>(R.id.search)
        city.isChecked = true
        search.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.usRadio -> searchCity()
                R.id.zipRadio -> searchZip()
            }
        }
        prefs = Prefs(view.context)
        if (prefs.containsKey(ZIP)) {
            view.find<TextView>(R.id.zip).text =  prefs.getString(ZIP)
        }
        if (prefs.containsKey(CITY)) {
            view.find<TextView>(R.id.usCity).text =  prefs.getString(CITY)
        }
    }

    private fun searchZip() {
        hideKeyboard(activity!!)
        view?.apply {
            val zip = find<TextView>(R.id.zip).text.toString()
            prefs.putString(ZIP, zip)
            find<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            find<RadioGroup>(R.id.radioGroup).visibility = View.GONE
            find<TextView>(R.id.usCity).visibility = View.GONE
            find<TextView>(R.id.zip).visibility = View.GONE
            find<TextView>(R.id.search).visibility = View.GONE
            WeatherService.getZipWeather(zip.toInt())
                    .subscribeOn(Schedulers.io())
                    .map { weather: WeatherModel -> weather }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { model ->
                        val searchResultsView = SearchResultsView()
                        searchResultsView.initialize(model)
                        router.pushController(RouterTransaction.with(searchResultsView))
                    }
        }
    }

    private fun searchCity() {
        hideKeyboard(activity!!)
        view?.apply {
            val city = find<TextView>(R.id.usCity).text.toString()
            prefs.putString(CITY, city)
            find<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            find<RadioGroup>(R.id.radioGroup).visibility = View.GONE
            find<TextView>(R.id.usCity).visibility = View.GONE
            find<TextView>(R.id.zip).visibility = View.GONE
            find<TextView>(R.id.search).visibility = View.GONE
            WeatherService.getCityWeather(city)
                .subscribeOn(Schedulers.io())
                .map { weather: WeatherModel -> weather }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { model ->
                    val searchResultsView = SearchResultsView()
                    searchResultsView.initialize(model)
                    router.pushController(RouterTransaction.with(searchResultsView))
                }
        }

    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodService = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }

    companion object {
        const val ZIP = "zip"
        const val CITY = "city"
    }
}