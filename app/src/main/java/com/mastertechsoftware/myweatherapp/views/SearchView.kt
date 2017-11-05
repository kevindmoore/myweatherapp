package com.mastertechsoftware.myweatherapp.views

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.bluelinelabs.conductor.Controller
import com.mastertechsoftware.myweatherapp.MainActivity
import com.mastertechsoftware.myweatherapp.MainActivity.Companion.RESULTS_VIEW
import com.mastertechsoftware.myweatherapp.R
import com.mastertechsoftware.myweatherapp.preferences.Prefs
import com.mastertechsoftware.myweatherapp.weatherService.SearchResults
import com.pawegio.kandroid.find

/**
 * Show the search fields.
 */
class SearchView : Controller() {
    lateinit var prefs : Prefs

    /**
     * Inflate the search view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.search_view, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        val radioGroup = view.find<RadioGroup>(R.id.radioGroup)
        val city = view.find<RadioButton>(R.id.usRadio)
        val zip = view.find<RadioButton>(R.id.zipRadio)
        val search = view.find<Button>(R.id.search)
        city.isChecked = true
        search.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.usRadio -> searchCity()
                R.id.zipRadio -> searchZip()
            }
        }
        // Use the radio group to control the focus
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.usRadio -> view.find<TextView>(R.id.usCity).requestFocus()
                R.id.zipRadio -> view.find<TextView>(R.id.zip).requestFocus()
            }
        }
        prefs = Prefs(view.context)
        if (prefs.containsKey(ZIP)) {
            view.find<TextView>(R.id.zip).text =  prefs.getString(ZIP)
            zip.isChecked = true
        }
        if (prefs.containsKey(CITY)) {
            view.find<TextView>(R.id.usCity).text =  prefs.getString(CITY)
            city.isChecked = true
        }
    }

    private fun searchZip() {
        hideKeyboard(activity!!)
        view?.apply {
            val zip = find<TextView>(R.id.zip).text.toString()
            prefs.removePref(CITY)
            prefs.putString(ZIP, zip)
            find<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            find<RadioGroup>(R.id.radioGroup).visibility = View.GONE
            find<TextView>(R.id.usCity).visibility = View.GONE
            find<TextView>(R.id.zip).visibility = View.GONE
            find<TextView>(R.id.search).visibility = View.GONE
            showZip(zip)
        }
    }

    fun showZip(zip: String) {
        SearchResults.getZipWeather(zip)
                .subscribe { model ->
                    val searchResultsView = SearchResultsView()
                    searchResultsView.initialize(model)
                    if (router.backstackSize > 1) {
                        router.replaceTopController((activity as MainActivity).getRouterTransaction(searchResultsView, RESULTS_VIEW))
                    } else {
                        router.pushController((activity as MainActivity).getRouterTransaction(searchResultsView, RESULTS_VIEW))
                    }
                }
    }

    private fun searchCity() {
        hideKeyboard(activity!!)
        view?.apply {
            val city = find<TextView>(R.id.usCity).text.toString()
            prefs.removePref(ZIP)
            prefs.putString(CITY, city)
            find<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            find<RadioGroup>(R.id.radioGroup).visibility = View.GONE
            find<TextView>(R.id.usCity).visibility = View.GONE
            find<TextView>(R.id.zip).visibility = View.GONE
            find<TextView>(R.id.search).visibility = View.GONE
            showCity(city)
        }

    }

    fun showCity(city: String) {
        SearchResults.getCityWeather(city)
                .subscribe { model ->
                    val searchResultsView = SearchResultsView()
                    searchResultsView.initialize(model)
                    if (router.backstackSize > 1) {
                        router.replaceTopController((activity as MainActivity).getRouterTransaction(searchResultsView, RESULTS_VIEW))
                    } else {
                        router.pushController((activity as MainActivity).getRouterTransaction(searchResultsView, RESULTS_VIEW))
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