package com.bunq.paymentapp

/** Just for the clear constant data all the constants are defined here and used whereever neccessary in the application**/
class Constants {
    companion object {
        const val API_KEY = "sandbox_09c251f0aec63fd96c06bf50262aab8fdf64a8a00ee81f1342cd9f48"
        const val BUNQ_PRIVATE_SHARED_PREFERENCE_KEY = BuildConfig.APPLICATION_ID + "_bunq_config"
        const val API_CONTEXT_KEY = "bunq_api_context"
        val TAG : String
            get() {
                return javaClass.simpleName
            }
    }

}