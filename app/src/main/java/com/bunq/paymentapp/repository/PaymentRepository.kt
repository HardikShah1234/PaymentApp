package com.bunq.paymentapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunq.sdk.model.generated.endpoint.Payment

/** This Repository allow the new payments to be done and set the payments which has
 * mutable live data of payments **/
class PaymentRepository {

    private val _setPayments = MutableLiveData<Set<Payment>>()
    val setPayments : LiveData<Set<Payment>> = _setPayments

    fun newPayments(vararg newPayments:Payment){
        this._setPayments.postValue(newPayments.toSet())
    }
}