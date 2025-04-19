package com.mike.locationapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mike.locationapp.common.LocationUtils
import com.mike.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    private val locationViewModel by viewModels<MVVM_Location>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationUtils = LocationUtils(this)
        enableEdgeToEdge()
        setContent {
            LocationAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        locationViewModel = locationViewModel,
                        locationUtils = locationUtils,
                        context = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(locationViewModel: MVVM_Location, locationUtils: LocationUtils, context: Context, modifier: Modifier = Modifier) {

    val locationPermissionLauncher = rememberLauncherForActivityResult( // request to start an activity for a result!
        contract = ActivityResultContracts.RequestMultiplePermissions() // contract to request multiple permissions
    ) { permissionsMap -> // return a map of the location permissions requests
        // Check if all requested permissions are granted
        /*val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }*/
    //Initialize it where you need it

        if (permissionsMap[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
            && permissionsMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {

            // Permission granted, proceed with location updates
            locationUtils.getLocationUpdates(locationViewModel) // get the location updates


        } else { // ask for permission
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (rationaleRequired) { // user dont allow
                Toast.makeText(
                    context,
                    "Location Permission is required for this feature to work",
                    Toast.LENGTH_LONG
                ).show()
            } else { // app not allowed in settings
                Toast.makeText(
                    context,
                    "You need to go to the settings to enable location permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // 3. Invoke the appropriate callback based on the permission result
        /*if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            onPermissionDenied.invoke()
        }*/
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                // Handle button click
                if (locationUtils.getLocationPermission(context)) { // if permissions and location already granted
                    // Permission granted, proceed with location updates
                    // Use the location updates as needed
                    locationUtils.getLocationUpdates(locationViewModel) // get the location updates
                    Log.d("Location 2", "${locationViewModel.locationUpdates.value?.latitude}")

                } else {
                    // Permission not granted, request permission
                    // You can use ActivityCompat.requestPermissions() here
                    locationPermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Request Location")
        }

            Text(
                text = "Latitude: ${locationViewModel.locationUpdates.value?.latitude}",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Longitude: ${locationViewModel.locationUpdates.value?.longitude}",
                modifier = Modifier.padding(16.dp)
            )
            // Display the address if available
            locationViewModel.locationUpdates.value?.address?.let {
                Text(
                    text = "Address: $it",
                    modifier = Modifier.padding(16.dp)
                )
            }  // Display a message if the address is not available


    }

}

/*
@Composable
fun RequestLocationPermissionUsingRememberLauncherForActivityResult(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    // 1. Create a stateful launcher using rememberLauncherForActivityResult
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // 2. Check if all requested permissions are granted
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        // 3. Invoke the appropriate callback based on the permission result
        if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            onPermissionDenied.invoke()
        }
    }

    // 4. Launch the permission request on composition
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
*/