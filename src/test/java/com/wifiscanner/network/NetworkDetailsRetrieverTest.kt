package com.wifiscanner.network

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.ScanResult
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.test.assertTrue
import kotlin.test.assertIs

class NetworkDetailsRetrieverTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockWifiManager: WifiManager

    private lateinit var networkDetailsRetriever: DefaultNetworkDetailsRetriever

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        networkDetailsRetriever = DefaultNetworkDetailsRetriever(mockContext, mockWifiManager)
    }

    @Test
    fun `when wifi is disabled, return WiFiDisabled error`() {
        `when`(mockWifiManager.isWifiEnabled).thenReturn(false)

        val result = networkDetailsRetriever.retrieveNetworkDetails()

        assertTrue(result is NetworkDetailResult.Error.WiFiDisabled)
    }

    @Test
    fun `when no networks found, return NoNetworksFound error`() {
        `when`(mockWifiManager.isWifiEnabled).thenReturn(true)
        `when`(mockWifiManager.scanResults).thenReturn(emptyList())

        val result = networkDetailsRetriever.retrieveNetworkDetails()

        assertTrue(result is NetworkDetailResult.Error.NoNetworksFound)
    }

    @Test
    fun `when network details retrieved successfully, return Success result`() {
        // Mock a scan result
        val mockScanResult = mock(ScanResult::class.java).apply {
            SSID = "TestNetwork"
            BSSID = "00:11:22:33:44:55"
            level = -55
            capabilities = "[WPA2-PSK-CCMP]"
        }

        `when`(mockWifiManager.isWifiEnabled).thenReturn(true)
        `when`(mockWifiManager.scanResults).thenReturn(listOf(mockScanResult))

        val result = networkDetailsRetriever.retrieveNetworkDetails()

        assertIs<NetworkDetailResult.Success>(result)
    }
}