package com.bunq.paymentapp.ApiData

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunq.paymentapp.Constants
import com.bunq.paymentapp.repository.NetworkState
import com.bunq.sdk.context.ApiContext
import com.bunq.sdk.context.ApiEnvironmentType
import com.bunq.sdk.context.BunqContext
import com.bunq.sdk.exception.BunqException
import com.bunq.sdk.exception.UncaughtExceptionError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/** Class creates the API Context here and also checks the user context
 * The creation is as per defined on the github of bunq sdk - https://github.com/bunq/sdk_java
 * Also has sharedPreferences instance for the Api Context Json Data**/

class BunqApiContextCreation {

    private lateinit var sharedPreferences: SharedPreferences

    fun setSharedPreferenceApiConfigure(application: Application) {
        sharedPreferences = application.getSharedPreferences(
            Constants.BUNQ_PRIVATE_SHARED_PREFERENCE_KEY,
            Context.MODE_PRIVATE
        )
    }
    //check if the User Context is available
    //If it is null throws the exception

    val hasUserContext: Boolean
        get() {
            try {
                BunqContext.getUserContext()
            } catch (e: BunqException) {
                return false
            }
            return true
        }
    /*now let set the ApiContext here.
    From the documentation of Bunq sdk it is clear that after creating the Api Context it will create the session for the device.
    If seesion is registered for particular IP then it works in every device?? IP will change with internet and Wifi.*/

    private val apiContext by lazy {
        fun CreateNewContext(): ApiContext {
            return ApiContext.create(
                ApiEnvironmentType.SANDBOX,
                Constants.API_KEY, "Android-device", "*"
            )
        }
        if (::sharedPreferences.isInitialized) {
            sharedPreferences.Jsondata?.let { it ->
                ApiContext.fromJson(it)
            } ?: CreateNewContext().also {
                sharedPreferences.Jsondata = it.toJson()
            }
        } else CreateNewContext()
    }

    private val _networkState = MutableLiveData<NetworkState<Unit>>()
    val networkState: LiveData<NetworkState<Unit>> = _networkState

    fun BunqContextLoading() {
        if (networkState.value is NetworkState.Loading) return
        if (hasUserContext) throw IllegalStateException("Method not called")

        GlobalScope.launch {
            try {
                BunqContext.loadApiContext(apiContext)
            } catch (e: UncaughtExceptionError) {
                Log.e(Constants.TAG, "Error Occured")
                _networkState.postValue(NetworkState.Failure())
            }
            _networkState.postValue(NetworkState.Success())
        }

    }

    private var SharedPreferences.Jsondata: String?
        get() = getString(Constants.API_CONTEXT_KEY, null)
        set(value) = edit().putString(Constants.API_CONTEXT_KEY, value).apply()
}