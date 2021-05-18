package edu.uw.echee.requestpermissionandfetchlocationspr21

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import edu.uw.echee.MyApplication
import edu.uw.echee.requestpermissionandfetchlocationspr21.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    private val simpleLocationManager by lazy { (application as MyApplication).simpleLocationManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
        with(binding) {

            simpleLocationManager.onLocationUpdateListener = { location ->
                Log.i("echee", "Location: $location")
            }

            btnFetchLocation.setOnClickListener {
                if (hasLocationPermission()) {
                    // Location permission is enabled
                    Toast.makeText(this@MainActivity, "Hooray! we can start fetching location", Toast.LENGTH_SHORT).show()
                    simpleLocationManager.startRequestLocationUpdates(1000)

                } else {
                    // Request for Location permissions
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle("My App needs location permission")
                        .setMessage("In order to find near by restaurants near you this app need would like location to provide relevant results")
                        .setPositiveButton("Got it") { _, _ ->
                            // Handle when they click got it

                            // Tells os to ask user to grant permissions of location
                            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        .create()
                        .show()
                }
            }

            btnStop.setOnClickListener {
                simpleLocationManager.stopLocationUpdates()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleLocationManager.stopLocationUpdates()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->

        if (isGranted) {
            // User has given us permission

            simpleLocationManager.startRequestLocationUpdates(5000)
            
        } else {
            // User has not given us permission
            Toast.makeText(this@MainActivity, "Ohh... okay so... its gonnna be like that :'(", Toast.LENGTH_SHORT).show()
        }

    }
}
