package com.bunq.paymentapp

import android.app.Application
import com.bunq.paymentapp.ApiData.BunqApiContextCreation

/** instance of the application**/

class NewPaymentApp :Application() {
    override fun onCreate() {
        super.onCreate()
        BunqApiContextCreation().setSharedPreferenceApiConfigure(this)
    }
}