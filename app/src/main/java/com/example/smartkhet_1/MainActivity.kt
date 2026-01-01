package com.example.smartkhet_1

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. FETCH DATA FROM YOUR "BACKEND"
        val healthData = MockBackend.getFarmHealth()
        val actions = MockBackend.getTodayActions()

        // 2. UPDATE THE UI
        setupWeatherCard(healthData)
        setupFarmHealthCard(healthData)
        setupSustainabilityCard(healthData)
        setupActionsList(actions)

        // 3. SETUP BUTTONS & NAVIGATION
        setupNavigation()
    }

    // --- HELPER FUNCTIONS TO KEEP CODE CLEAN ---

    private fun setupWeatherCard(data: FarmHealth) {
        // In a real app, you'd fetch real weather. For now, we use static/backend data.
        findViewById<TextView>(R.id.weather_temp_text).text = "31°C" // Example static
        findViewById<TextView>(R.id.weather_desc_text).text = "Partly cloudy • 40% rain"
    }

    private fun setupFarmHealthCard(data: FarmHealth) {
        // 1. The Main Badge (e.g., "Good")
        val badge = findViewById<TextView>(R.id.farm_health_badge)
        badge.text = "● Good"
        // You can change color based on status if you want:
        // badge.setTextColor(getColor(R.color.primary_green))

        // 2. The 3 Columns
        findViewById<TextView>(R.id.soil_moisture_text).text = data.soilMoisture
        findViewById<TextView>(R.id.pest_risk_text).text = data.pestRisk
        findViewById<TextView>(R.id.weather_alert_text).text = data.weatherAlert
    }

    private fun setupSustainabilityCard(data: FarmHealth) {
        // 1. Score Text & Progress
        findViewById<TextView>(R.id.score_text).text = data.ecoScore.toString()
        findViewById<ProgressBar>(R.id.score_progress).progress = data.ecoScore

        // 2. Efficiency Texts
        findViewById<TextView>(R.id.water_efficiency_text).text = data.waterEfficiency
        findViewById<TextView>(R.id.carbon_footprint_text).text = data.carbonFootprint

        // 3. "Details" Button Click
        findViewById<TextView>(R.id.sustainabilityDetailsBtn).setOnClickListener {
            val intent = android.content.Intent(this, SustainabilityDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionsList(actions: List<FarmAction>) {
        // Link "View All" Button
        findViewById<TextView>(R.id.viewAllBtn).setOnClickListener {
            val intent = android.content.Intent(this, AllActionsActivity::class.java)
            startActivity(intent)
        }

        // Fill Action 1 (First item in list)
        if (actions.isNotEmpty()) {
            val a1 = actions[0]
            findViewById<TextView>(R.id.act1_title).text = a1.title
            findViewById<TextView>(R.id.act1_desc).text = a1.desc
            findViewById<TextView>(R.id.act1_time).text = a1.time
        }

        // Fill Action 2 (Second item in list)
        if (actions.size > 1) {
            val a2 = actions[1]
            findViewById<TextView>(R.id.act2_title).text = a2.title
            findViewById<TextView>(R.id.act2_desc).text = a2.desc
            findViewById<TextView>(R.id.act2_time).text = a2.time
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Ensure "Home" is selected when page loads
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_crops -> {
                    val intent = android.content.Intent(this, CropsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0) // Smooth transition
                    true
                }
                R.id.nav_home -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset Highlight to Home when returning
        findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_home
    }
}