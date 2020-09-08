package com.bunq.paymentapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*This function is used for toast, snackbar to show in the app whereever is used for short duration.*/

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

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()