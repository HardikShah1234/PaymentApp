package com.bunq.paymentapp.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bunq.paymentapp.R
import com.bunq.paymentapp.UI.payment.PaymentSearchList
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.payment_list_layout.view.*


/*Adapter class for the recycler view
* Also set the position of sortList of the payment
* This custom adapter is filterable used for search view*/
class CustomAdapter(
    private val onItemClickListener: (Int) -> Unit,
    private val onAdapterIsEmptyListener: (Boolean) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private val adapterCallback = object : SortedListAdapterCallback<PaymentSearchList>(this) {
        override fun areItemsTheSame(
            item1: PaymentSearchList,
            item2: PaymentSearchList
        ): Boolean {
            return item1.payment.id == item2.payment.id
        }

        override fun compare(o1: PaymentSearchList, o2: PaymentSearchList): Int {
            return o1.createdDate.compareTo(o2.createdDate)
        }

        override fun areContentsTheSame(
            oldItem: PaymentSearchList?,
            newItem: PaymentSearchList?
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val sortlist: SortedList<PaymentSearchList> =
        SortedList(PaymentSearchList::class.java, adapterCallback)

    fun clearAndReplace(newData: List<PaymentSearchList>) {
        sortlist.beginBatchedUpdates()

        for (i in sortlist.size() - 1 downTo 0) {
            val item: PaymentSearchList = sortlist[i]

            val isContained = newData.any { new ->
                adapterCallback.areItemsTheSame(new, item)
            }
            if (!isContained) {
                sortlist.remove(item)
            }
        }
        for (new in newData)
            sortlist.add(new)
        sortlist.endBatchedUpdates()
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.payment_list_layout, parent, false)
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            itemView.run {
                val paymentSearchList = sortlist[position]
                val payment = paymentSearchList.payment

                setOnClickListener {
                    onItemClickListener.invoke(payment.id)
                }

                context.apply {
                    payment.let {
                        tv_price.text = getString(R.string.euro_currency, payment.amount.value)
                        payment_alias.text = it.counterpartyAlias.displayName
                        payment_status.text = it.subType
                    }
                }
            }
        }
    }

    override fun getItemCount() =
        sortlist.size().also { size -> onAdapterIsEmptyListener.invoke(size == 0) }

}