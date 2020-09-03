package com.bunq.paymentapp

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("WrongConstant")
fun View.snackbar(
    @StringRes stringId: Int,
    @BaseTransientBottomBar.Duration durationId: Int = BaseTransientBottomBar.LENGTH_INDEFINITE
) =
    Snackbar.make(this, this.resources.getString(stringId), durationId)

inline fun LifecycleOwner.doActionAfterDelay(delayDuration: Long, crossinline action: () -> Unit) {
    lifecycleScope.launch { //bind job to lifecycle
        delay(delayDuration)
        action.invoke()
    }
}