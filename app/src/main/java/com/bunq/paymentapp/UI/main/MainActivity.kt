package com.bunq.paymentapp.UI.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bunq.paymentapp.R
import com.bunq.paymentapp.doActionAfterDelay
import com.bunq.paymentapp.repository.NetworkState
import com.bunq.paymentapp.snackbar
import com.bunq.paymentapp.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val rootView by lazy { activity_main_root_view }

    //    private lateinit var customSearchView: CustomSearchView
    //private lateinit var viewModel: MainActivityViewModel
    private val viewModel: MainActivityViewModel by viewModels()
//    private val adapter by lazy {
//        CustomAdapter({
//            Toast.makeText(this, "Clicked with Id $it", Toast.LENGTH_SHORT).show()
//        }, onAdapterIsEmptyListener = {
//            customSearchView.text.isNotBlank() && viewModel.listPayments.value!! is NetworkState.Success
//        })
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // viewModel = getViewModel()
//        viewModel.listPayments.observe(this, Observer {
//            when (it) {
//                is NetworkState.Loading -> loader.visibility = View.VISIBLE
//                is NetworkState.Failure -> loader.visibility = View.GONE
//                is NetworkState.Success -> {
//                    loader.visibility = View.GONE
//                    it.data!!.let {
//                        adapter.clearAndReplace(it.map {
//                            PaymentSearchList(it)
//                        })
//                    }
//                }
//            }
//        })

        viewModel.let {
            val snackbarBunqRetry by lazy {
                rootView.snackbar(R.string.Not_Connect)
                    .setAction(getString(R.string.Try_Again)) {
                        viewModel.LoadBunqContext()
                    }
            }
            viewModel.bunqNetworkStatIntialize.observe(this, Observer {
                when (it) {
                    is NetworkState.Success -> snackbarBunqRetry.dismiss()
                    is NetworkState.Failure -> it.data!!.let {
                        it.getContentIfNotHandled()?.let { exception ->
                            when (exception) {
                                is MainActivityViewModel.BunqInitializeException -> {
                                    doActionAfterDelay(500) {
                                        snackbarBunqRetry.show()
                                    }
                                }
                                is MainActivityViewModel.BunqErrorsException -> toast("Bunq failed to connect")
                            }
                        }
                    }
                }
            })
            viewModel.LoadBunqContext()
        }
        // TODO: have to implement search view functionality here properly with setOnQueryTextListener
        //TODO: have to bind recycler View Here in search function so the list should be able to search properly
//        adapter.notifyDataSetChanged()
//        recycler_view.adapter = adapter

    }

    /** fun to bind the viewModel with the Activity **/

//    private fun getViewModel(): MainActivityViewModel {
//        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                return MainActivityViewModel() as T
//            }
//
//        })[MainActivityViewModel::class.java]
//    }
}