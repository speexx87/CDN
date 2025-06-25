package com.wifiscanner.network

/**
 * Data class representing details of a WiFi network
 * @param ssid Service Set Identifier (network name)
 * @param bssid Basic Service Set Identifier (MAC address)
 * @param signalStrength Strength of the WiFi signal
 * @param securityType Security protocol of the network
 */
data class NetworkDetails(
    val ssid: String,
    val bssid: String,
    val signalStrength: Int,
    val securityType: String
) {
    /**
     * Provides a human-readable signal strength description
     */
    val signalStrengthDescription: String
        get() = when {
            signalStrength >= -50 -> "Excellent"
            signalStrength >= -60 -> "Good"
            signalStrength >= -70 -> "Fair"
            else -> "Weak"
        }
}