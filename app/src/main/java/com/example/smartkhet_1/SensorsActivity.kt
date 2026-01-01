package com.example.smartkhet_1

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SensorsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensors)

        // 1. Get Data
        val data = MockBackend.getSensorReadings()

        // 2. Setup Moisture Gauge
        findViewById<ProgressBar>(R.id.moisture_progress).progress = data.moisture
        findViewById<TextView>(R.id.moisture_text).text = "${data.moisture}%"

        // 3. Setup Temp & pH
        findViewById<TextView>(R.id.temp_text).text = "${data.temp}°C"
        findViewById<TextView>(R.id.ph_text).text = "${data.ph}"

        // 4. Setup NPK Bars
        findViewById<ProgressBar>(R.id.progress_n).progress = data.nitrogen
        findViewById<ProgressBar>(R.id.progress_p).progress = data.phosphorus
        findViewById<ProgressBar>(R.id.progress_k).progress = data.potassium

        // 5. Setup Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // This makes the Sensors icon green because we are here
        bottomNav.selectedItemId = R.id.nav_sensors

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Go to Home
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_crops -> {
                    // Go to Crops
                    startActivity(Intent(this, CropsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_sensors -> true // Already here, do nothing
                else -> false
            }
        }
    }
}