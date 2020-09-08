package com.bunq.paymentapp

/** Just for the clear constant data all the constants are defined here and used whereever neccessary in the application**/
class Constants {
    companion object {
        /*Sandbox API Key is valid for some time interval??
        *The tutorial of bunq helps a lot to generate the api key from curl - https://together.bunq.com/d/24919-getting-started-with-bunq-api-through-curl-a-step-by-step-tutorial*/

        const val API_KEY = "sandbox_133c7be02732d40359d5226b8e467492152226f049fd5638c4dd63da"
        const val BUNQ_PRIVATE_SHARED_PREFERENCE_KEY = BuildConfig.APPLICATION_ID + "_bunq_config"
        const val API_CONTEXT_KEY = "bunq_api_context"
        val TAG: String
            get() {
                return javaClass.simpleName
            }
    }

}