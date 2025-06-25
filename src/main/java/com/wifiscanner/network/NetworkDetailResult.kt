package com.wifiscanner.network

/**
 * Sealed class representing the result of network details retrieval
 * Provides a type-safe way to handle success and error scenarios
 */
sealed class NetworkDetailResult {
    /**
     * Successful network details retrieval
     * @param networkDetails The details of the retrieved network
     */
    data class Success(val networkDetails: NetworkDetails) : NetworkDetailResult()

    /**
     * Represents different types of errors that can occur during network details retrieval
     */
    sealed class Error : NetworkDetailResult() {
        /**
         * No networks found error
         */
        object NoNetworksFound : Error()

        /**
         * Permission denied error
         */
        object PermissionDenied : Error()

        /**
         * WiFi disabled error
         */
        object WiFiDisabled : Error()

        /**
         * Generic error with optional message
         * @param message Optional error description
         */
        data class Generic(val message: String? = null) : Error()
    }

    /**
     * Checks if the result is a success
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Checks if the result is an error
     */
    val isError: Boolean
        get() = this is Error
}