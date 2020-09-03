package com.bunq.paymentapp

import androidx.lifecycle.ViewModel
import com.bunq.paymentapp.ApiData.BunqApiContextCreation

//Load Bunq Context class which will called in main activity
abstract class BunqApiContextCreationViewModel : ViewModel() {

    fun LoadBunqContext() {
        if (BunqApiContextCreation().hasUserContext) {
            BunqApiContextCreation().BunqContextLoading()
        }
    }
}