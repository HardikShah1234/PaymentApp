package com.bunq.paymentapp.UI.main

import android.util.Log
import androidx.lifecycle.*
import com.bunq.paymentapp.ApiData.BunqApiContextCreation
import com.bunq.paymentapp.BunqApiContextCreationViewModel
import com.bunq.paymentapp.Constants
import com.bunq.paymentapp.UI.payment.contains
import com.bunq.paymentapp.repository.Event
import com.bunq.paymentapp.repository.NetworkState
import com.bunq.paymentapp.repository.PaymentRepository
import com.bunq.sdk.context.BunqContext
import com.bunq.sdk.exception.BunqException
import com.bunq.sdk.exception.UncaughtExceptionError
import com.bunq.sdk.model.generated.endpoint.Payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : BunqApiContextCreationViewModel() {


    private companion object {
        const val BUNQ_ERRORS = 3
    }

    private var bunqErrors = 0
    private val paymentRepository by lazy { PaymentRepository() }
    private val paymentState = MutableLiveData<NetworkState<Unit>>()
    private val filteredText = MutableLiveData<String>()

    val bunqNetworkStatIntialize: LiveData<NetworkState<Event<BunqException>>> =
        MediatorLiveData<NetworkState<Event<BunqException>>>().apply {
            val eventBunqErrors by lazy {
                Event(BunqErrorsException())
            }
            addSource(BunqApiContextCreation().networkState) {
                when (it) {
                    is NetworkState.Success -> {
                        bunqErrors = 0
                        value = NetworkState.Success()
                    }
                    is NetworkState.Loading -> value = NetworkState.Loading()
                    is NetworkState.Failure -> value =
                        if (++bunqErrors >= BUNQ_ERRORS) NetworkState.Failure(eventBunqErrors) else NetworkState.Failure(
                            Event(BunqInitializeException())
                        )
                }
            }
        }

    private val paymentRepositorySource =
        MediatorLiveData<NetworkState<Iterable<Payment>>>().apply {
            val filtered by lazy {
                MediatorLiveData<Iterable<Payment>>().apply {
                    var setpayment: Set<Payment>? = null
                    var list = ""

                    fun onChange() {
                        if (setpayment == null) return

                        value = setpayment!!.run {
                            if (isEmpty()) this
                            else filter {
                                it.contains(list, true)
                            }
                        }.toSet()
                    }
                    addSource(paymentRepository.setPayments) {
                        setpayment = it
                        onChange()
                    }
                }
            }
            val observer by lazy {
                Observer<Iterable<Payment>> {
                    value = NetworkState.Success(it)
                }
            }
            addSource(paymentState) {
                when (it) {
                    is NetworkState.Success -> {
                        addSource(filtered, observer)
                    }
                    else -> value = it as NetworkState<Iterable<Payment>>
                }
            }

        }

    val listPayments: LiveData<NetworkState<Iterable<Payment>>> =
        MediatorLiveData<NetworkState<Iterable<Payment>>>().apply {
            addSource(BunqApiContextCreation().networkState) {
                when (it) {
                    is NetworkState.Success -> {
                        removeSource(BunqApiContextCreation().networkState)

                        addSource(paymentRepositorySource) {
                            value = it
                        }.also {
                            requestPaymentList()
                        }
                    }
                    else -> value = it as NetworkState<Iterable<Payment>>
                }
            }
        }

    private fun requestPaymentList() {
        paymentState.apply {
            value = NetworkState.Loading()

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        delay(1000)
                        Payment.list(BunqContext.getUserContext().mainMonetaryAccountId).let {
                            it.value.let {
                                PaymentRepository()
                                    .newPayments(*it.toTypedArray())
                                postValue(NetworkState.Success())
                            }
                        }

                    } catch (e: UncaughtExceptionError) {
                        Log.e(Constants.TAG, "Error in Payment request", e)
                        postValue(NetworkState.Failure())

                    }

                }
            }
        }
    }

    // TODO: Applyfilter fuction will be called in the main Activity when search view is implemented

    fun applyFilter(filterText: String) {
        this.filteredText.value = filterText
    }

    class BunqErrorsException(
        message: String = "Too many consecutive failures to initialize BunqContext.",
        cause: Throwable? = null
    ) : BunqException(message, cause)

    class BunqInitializeException(
        message: String = "Failed to initialize BunqContext.",
        cause: Throwable? = null
    ) : BunqException(message, cause)
}