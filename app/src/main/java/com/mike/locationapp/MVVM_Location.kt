package com.mike.locationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MVVM_Location: ViewModel() {

    // ViewModel logic for location updates
    // This is where you would handle the location updates and permissions
    // For example, you could use LiveData to observe location changes
    // and update the UI accordingly

    private val _locationUpdates = mutableStateOf<Location?>(null)// MutableLiveData<Location?> by lazy {
       // MutableLiveData<Location?>(null)
    //}
    val locationUpdates: State<Location?> = _locationUpdates
        //< LiveData<Location?>
       // get() = _locationUpdates


    fun updateLocation(location: Location) {
        // Update the location in the ViewModel
        // This will notify observers of the change
        // and update the UI accordingly
        _locationUpdates.value = location
    }



}

data class Location
    (
    val latitude: Double,
    val longitude: Double,
    val address: String? = null
)