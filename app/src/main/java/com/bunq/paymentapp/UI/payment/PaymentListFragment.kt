package com.bunq.paymentapp.UI.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bunq.paymentapp.R
import com.bunq.paymentapp.repository.CustomAdapter
import com.bunq.paymentapp.repository.NetworkState
import com.bunq.paymentapp.toast
import kotlinx.android.synthetic.main.fragment_payment_list.*

/* To use fragments is inspired by the blog post- https://guides.codepath.com/android/creating-and-using-fragments.
* This fragment class attaches the adapter to recycler view and handles the search view options as well.*/

class PaymentListFragment : Fragment() {

    private val viewModel: PaymentFragmentViewModel by viewModels()

    private val adapter by lazy {
        CustomAdapter({
            requireContext().toast("Clicked and Payment ID is: $it")
        }, onAdapterIsEmptyListener = { isEmpty ->
            container_empty_list.isVisible =
                isEmpty && search_edit_text.text.isNotBlank() && viewModel.listPayments.value is NetworkState.Success

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_view.adapter = adapter
        search_edit_text.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private fun handleQuery(query: String) {
                viewModel.applyFilter(query)
                recycler_view.scrollToPosition(0)
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                handleQuery(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleQuery(newText!!)
                return true
            }
        })

        bindUI()
    }

    /*Shimmer is used to give the effects of loading data*/

    private fun bindUI() {
        fun setPaymentLoading(Shimmer: Boolean) {
            shimmer_view_container.isVisible = Shimmer
            if (Shimmer) shimmer_view_container.startShimmer() else shimmer_view_container.stopShimmer()
            recycler_view.isVisible = Shimmer
        }

        viewModel.listPayments.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkState.Loading -> setPaymentLoading(true)
                is NetworkState.Failure -> setPaymentLoading(false)
                is NetworkState.Success -> {
                    setPaymentLoading(false)
                    it.data!!.let {
                        adapter.clearAndReplace(it.map {
                            PaymentSearchList(it)
                        })
                    }
                }
            }
        })
    }

}