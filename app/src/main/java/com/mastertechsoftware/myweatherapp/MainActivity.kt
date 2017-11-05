package com.mastertechsoftware.myweatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.mastertechsoftware.myweatherapp.preferences.Prefs
import com.mastertechsoftware.myweatherapp.views.SearchResultsView
import com.mastertechsoftware.myweatherapp.views.SearchView
import com.mastertechsoftware.myweatherapp.views.SearchView.Companion.CITY
import com.mastertechsoftware.myweatherapp.views.SearchView.Companion.ZIP
import com.mastertechsoftware.myweatherapp.weatherService.SearchResults
import com.pawegio.kandroid.find


/**
 * Main starting point.
 * Note that this is a an example of a Single Activity app using the Conductor library
 */
class MainActivity : AppCompatActivity() {
    lateinit var conductorRouter: Router // Conductor router
    lateinit var prefs : Prefs // For storing preferences
    val changeHandler = HorizontalChangeHandler() // For animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup Layouts
        setContentView(R.layout.activity_main)
        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.search_menu)

        toolbar.setOnMenuItemClickListener { menuItem ->
            // Check for the case where we already have the search view on top
            val transactions = conductorRouter.getBackstack()
            if (transactions[transactions.size-1].tag() == SEARCH_VIEW) {
                return@setOnMenuItemClickListener true
            }
            if (conductorRouter.backstackSize > 1) {
                conductorRouter.replaceTopController(getRouterTransaction(SearchView(), SEARCH_VIEW))
            } else {
                conductorRouter.pushController(getRouterTransaction(SearchView(), SEARCH_VIEW))
            }
            true
        }
        conductorRouter = Conductor.attachRouter(this, find(R.id.controller_container), null)

        // On startup check if we have a previous saved value.
        // Go to results screen if so
        prefs = Prefs(this)
        if (prefs.containsKey(SearchView.ZIP) || prefs.containsKey(SearchView.CITY)) {
            if (prefs.containsKey(SearchView.ZIP)) {
                SearchResults.getZipWeather(prefs.getString(ZIP)!!)
                        .subscribe { model ->
                            val searchResultsView = SearchResultsView()
                            searchResultsView.initialize(model)
                            if (conductorRouter.backstackSize > 1) {
                                conductorRouter.replaceTopController(getRouterTransaction(SearchView(), RESULTS_VIEW))
                            } else {
                                conductorRouter.pushController(getRouterTransaction(SearchView(), RESULTS_VIEW))
                            }
                        }
            } else {
                SearchResults.getCityWeather(prefs.getString(CITY)!!).subscribe { model ->
                    val searchResultsView = SearchResultsView()
                    searchResultsView.initialize(model)
                    if (conductorRouter.backstackSize > 1) {
                        conductorRouter.replaceTopController(getRouterTransaction(searchResultsView, RESULTS_VIEW))
                    } else {
                        conductorRouter.pushController(getRouterTransaction(searchResultsView,RESULTS_VIEW ))
                    }
                }
            }
        } else {
            conductorRouter.setRoot(getRouterTransaction(SearchView(), SEARCH_VIEW))
        }
    }

    /**
     * Create a RouterTransaction with a handler that can do animations
     */
    fun getRouterTransaction(view: Controller, tag: String) : RouterTransaction {
        return RouterTransaction.with(view).pushChangeHandler(changeHandler).tag(tag)
    }

    override fun onBackPressed() {
        if (conductorRouter.handleBack()) {
            return
        }
        super.onBackPressed()
    }

    companion object {
        const val SEARCH_VIEW = "search_view"
        const val RESULTS_VIEW = "results_view"
    }

}
