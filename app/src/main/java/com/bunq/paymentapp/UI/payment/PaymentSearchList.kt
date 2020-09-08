package com.bunq.paymentapp.UI.payment

import com.bunq.sdk.model.generated.endpoint.Payment
import java.text.SimpleDateFormat
import java.util.*

/** Payment Search List class contains the searchable list defined below**/

data class PaymentSearchList(val payment: Payment) {

    object Date {
        val formatter = SimpleDateFormat("YYYY-MM-DD hh:mm:ss.ssssss", Locale.GERMAN)
    }

    val createdDate = Date.formatter.parse(payment.created)!!
}

fun Payment.contains(query: String, disregard: Boolean = false): Boolean {
    val words = query.split("\\s+".toRegex())
    for (word in words) {
        val SearchList = "${alias.displayName} ${alias.labelUser} ${alias.iban} ${alias.country}" +
                "${counterpartyAlias.displayName} ${counterpartyAlias.labelUser} ${counterpartyAlias.iban} ${counterpartyAlias.country}" +
                "${amount} ${created} ${updated}"
        if (!SearchList.contains(word, disregard)) {
            return false
        }
    }
    return true
}