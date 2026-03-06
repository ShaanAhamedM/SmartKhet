// Sensors Code

package com.example.smartkhet_1

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class SensorsActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var moistureProgress: ProgressBar
    private lateinit var moistureText: TextView
    private lateinit var tempText: TextView
    private lateinit var phText: TextView
    private lateinit var progressN: ProgressBar
    private lateinit var progressP: ProgressBar
    private lateinit var progressK: ProgressBar

    // Bluetooth Variables
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var isConnected = false
    private var listenThread: Thread? = null

    // Standard SPP UUID for ESP32 Bluetooth Classic
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val DEVICE_NAME = "SmartFarm_ESP32"
    private val uiHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors)

        // 1. Initialize UI Elements
        moistureProgress = findViewById(R.id.moisture_progress)
        moistureText = findViewById(R.id.moisture_text)
        tempText = findViewById(R.id.temp_text)
        phText = findViewById(R.id.ph_text)
        progressN = findViewById(R.id.progress_n)
        progressP = findViewById(R.id.progress_p)
        progressK = findViewById(R.id.progress_k)

        // 2. Setup Navigation (Your original logic)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_sensors

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_crops -> {
                    startActivity(Intent(this, CropsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_sensors -> true // Already here
                R.id.nav_community -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // 3. Initialize Bluetooth
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        } else {
            connectToESP32()
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToESP32() {
        // Check for runtime permissions on newer Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please allow 'Nearby Devices' permission in App Settings", Toast.LENGTH_LONG).show()
            return
        }

        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(this, "Please turn on Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        var esp32Device: BluetoothDevice? = null

        pairedDevices?.forEach { device ->
            if (device.name == DEVICE_NAME) {
                esp32Device = device
            }
        }

        if (esp32Device != null) {
            Toast.makeText(this, "Connecting to ESP32...", Toast.LENGTH_SHORT).show()
            Thread {
                try {
                    bluetoothSocket = esp32Device?.createRfcommSocketToServiceRecord(MY_UUID)
                    bluetoothSocket?.connect()
                    inputStream = bluetoothSocket?.inputStream
                    isConnected = true

                    uiHandler.post { Toast.makeText(this, "Connected! Waiting for data...", Toast.LENGTH_SHORT).show() }
                    beginListeningForData()

                } catch (e: IOException) {
                    Log.e("Bluetooth", "Connection Failed", e)
                    isConnected = false
                    try { bluetoothSocket?.close() } catch (ignored: IOException) { }
                    uiHandler.post { Toast.makeText(this, "Connection Failed. Is ESP32 on?", Toast.LENGTH_SHORT).show() }
                }
            }.start()
        } else {
            Toast.makeText(this, "Please pair your phone with 'SmartFarm_ESP32' in Bluetooth settings first", Toast.LENGTH_LONG).show()
        }
    }

    private fun beginListeningForData() {
        val buffer = ByteArray(1024)
        var bytes: Int
        var messageBuilder = StringBuilder()

        listenThread = Thread {
            while (isConnected && !Thread.currentThread().isInterrupted) {
                try {
                    bytes = inputStream?.read(buffer) ?: 0
                    if (bytes > 0) {
                        val incomingMessage = String(buffer, 0, bytes)
                        messageBuilder.append(incomingMessage)

                        // Wait until we receive a full line (ending with \n from ESP32 println)
                        val endOfLineIndex = messageBuilder.indexOf("\n")
                        if (endOfLineIndex > 0) {
                            val completeData = messageBuilder.substring(0, endOfLineIndex).trim()
                            messageBuilder.delete(0, endOfLineIndex + 1)

                            if (completeData.contains(",")) {
                                parseDataAndUpdateUI(completeData)
                            }
                        }
                    }
                } catch (e: IOException) {
                    Log.e("Bluetooth", "Disconnected", e)
                    isConnected = false
                    uiHandler.post { Toast.makeText(this@SensorsActivity, "ESP32 Disconnected", Toast.LENGTH_SHORT).show() }
                    break
                }
            }
        }
        listenThread?.start()
    }

    private fun parseDataAndUpdateUI(data: String) {
        // Format from ESP32: Moisture,Temp,N,P,K,pH
        try {
            val values = data.split(",")
            if (values.size >= 6) {
                val moisture = values[0].toIntOrNull() ?: 0
                val temp = values[1]
                val n = values[2].toIntOrNull() ?: 0
                val p = values[3].toIntOrNull() ?: 0
                val k = values[4].toIntOrNull() ?: 0
                val ph = values[5]

                // Update the screen on the Main UI Thread
                uiHandler.post {
                    moistureProgress.progress = moisture
                    moistureText.text = "$moisture%"

                    tempText.text = "${temp}°C"
                    phText.text = ph

                    progressN.progress = n
                    progressP.progress = p
                    progressK.progress = k
                }
            }
        } catch (e: Exception) {
            Log.e("ParseData", "Error parsing: $data", e)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        isConnected = false
        listenThread?.interrupt()
        try { bluetoothSocket?.close() } catch (ignored: IOException) { }
    }
}