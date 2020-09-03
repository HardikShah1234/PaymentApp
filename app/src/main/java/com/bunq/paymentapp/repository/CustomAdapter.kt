package com.bunq.paymentapp.repository

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.bunq.paymentapp.R
import com.bunq.paymentapp.UI.payment.PaymentSearchList
import kotlinx.android.synthetic.main.payment_list_layout.view.*

class CustomAdapter(
    private val onItemClickListener: (Int) -> Unit,
    private val onAdapterIsEmptyListener: (Boolean) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(), Filterable {

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_price: TextView
        var tv_alias: TextView

        init {
            tv_price = itemView.tv_price
            tv_alias = itemView.tv_alias
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.payment_list_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sortlist.size().also { size -> onAdapterIsEmptyListener.invoke(size == 0) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paymentSearchList = sortlist[position]
        val payment = paymentSearchList.payment
        holder.itemView.setOnClickListener { onItemClickListener.invoke(payment.id) }
        val text = holder.itemView.context.getString(R.string.euro_currency, payment.amount.value)
        holder.tv_price.text = text
        holder.tv_alias.text = payment.counterpartyAlias.displayName
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

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}