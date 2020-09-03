package com.bunq.paymentapp.repository

/**Seales class Network state is implemented to have all the data of Errors and Success**/

sealed class NetworkState<T>(val data: T? = null, val msg: String? = null) {
    class Success<T>(data: T? = null) : NetworkState<T>(data)
    class Failure<T>(data: T? = null, msg: String? = null) : NetworkState<T>(data, msg)
    class Loading<T> : NetworkState<T>()

}