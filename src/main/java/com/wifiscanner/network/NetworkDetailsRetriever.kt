package com.wifiscanner.network

import android.content.Context
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

/**
 * Interface for retrieving network details with error handling
 */
interface NetworkDetailsRetriever {
    /**
     * Retrieve network details with comprehensive error handling
     * @return NetworkDetailResult representing the retrieval outcome
     */
    fun retrieveNetworkDetails(): NetworkDetailResult
}

/**
 * Default implementation of NetworkDetailsRetriever
 * @param context Android application context
 */
class DefaultNetworkDetailsRetriever(
    private val context: Context,
    private val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
) : NetworkDetailsRetriever {

    override fun retrieveNetworkDetails(): NetworkDetailResult {
        // Check WiFi state
        return when {
            !wifiManager.isWifiEnabled -> NetworkDetailResult.Error.WiFiDisabled
            !hasLocationPermission() -> NetworkDetailResult.Error.PermissionDenied
            else -> tryRetrieveNetworkDetails()
        }
    }

    /**
     * Attempt to retrieve network details with error handling
     */
    private fun tryRetrieveNetworkDetails(): NetworkDetailResult {
        return try {
            val scanResults = wifiManager.scanResults
            
            // Check if no networks are found
            if (scanResults.isEmpty()) {
                return NetworkDetailResult.Error.NoNetworksFound
            }

            // Convert scan results to network details
            val networkDetails = scanResults.map { result ->
                NetworkDetails(
                    ssid = result.SSID ?: "Unknown",
                    bssid = result.BSSID ?: "Unknown",
                    signalStrength = result.level,
                    securityType = determineSecurityType(result)
                )
            }

            // Return successful result
            NetworkDetailResult.Success(networkDetails.first())
        } catch (e: Exception) {
            // Catch any unexpected errors
            NetworkDetailResult.Error.Generic(e.localizedMessage)
        }
    }

    /**
     * Check if location permission is granted
     */
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Determine the security type of the WiFi network
     */
    private fun determineSecurityType(scanResult: android.net.wifi.ScanResult): String {
        return when {
            scanResult.capabilities.contains("WEP") -> "WEP"
            scanResult.capabilities.contains("WPA") -> "WPA"
            scanResult.capabilities.contains("WPA2") -> "WPA2"
            scanResult.capabilities.contains("WPA3") -> "WPA3"
            else -> "Open"
        }
    }
}