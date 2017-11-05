package com.mastertechsoftware.myweatherapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.mastertechsoftware.myweatherapp.preferences.Prefs
import com.mastertechsoftware.myweatherapp.views.SearchView
import com.pawegio.kandroid.find







class MainActivity : AppCompatActivity() {
    lateinit var conductorRouter: Router
    lateinit var prefs : Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = find<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.search_menu)
        val searchView = SearchView()
        toolbar.setOnMenuItemClickListener { menuItem ->
            conductorRouter.pushController(RouterTransaction.with(searchView))
            true
        }
        prefs = Prefs(this)
        if (prefs.containsKey(CURRENT_SEARCH)) {
        }
        conductorRouter = Conductor.attachRouter(this, find(R.id.controller_container), null)
        conductorRouter.setRoot(RouterTransaction.with(searchView))
        if (!PermissionsManager.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(title).setPositiveButton(getString(R.string.ok),  {dialog , which ->
                PermissionsManager.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST)
            }).setMessage(getString(R.string.locationPermissionMsg)).show()
        }
    }


    override fun onBackPressed() {
        if (conductorRouter.handleBack()) {
            return
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prefs.putBoolean(LOCATION_PERMISSION, true)
                conductorRouter.replaceTopController(RouterTransaction.with(SearchView()))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_REQUEST && resultCode == Activity.RESULT_OK) {
            prefs.putBoolean(LOCATION_PERMISSION, true)
        }
    }
    companion object {
       const val LOCATION_REQUEST = 100
       const val CURRENT_SEARCH = "currentSearch"
       const val LOCATION_PERMISSION = "location_permission"
       const val LOCATION_INTERVAL = 1000 * 60 * 60L // 1 HOUR
       const val FASTEST_LOCATION_INTERVAL = 1000 * 60L // 1 MINUTE
    }

}
