package com.bunq.paymentapp.repository
/**
* Reference: Courtesy of Jose Alcérreca, Google dev, over this blogpost:
* https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
**/
class Event<out T>(private val content: T) {

    private var hasBeenHandled = false // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}