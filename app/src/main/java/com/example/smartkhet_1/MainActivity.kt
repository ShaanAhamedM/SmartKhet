package com.example.smartkhet_1

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. FETCH DATA (Home Page Only)
        val healthData = MockBackend.getFarmHealth()
        val actions = MockBackend.getTodayActions()

        // 2. UPDATE UI
        setupWeatherCard(healthData)
        setupFarmHealthCard(healthData)
        setupSustainabilityCard(healthData)
        setupActionsList(actions)

        // 3. SETUP NAVIGATION
        setupNavigation()
    }

    // --- HELPER FUNCTIONS ---

    private fun setupWeatherCard(data: FarmHealth) {
        findViewById<TextView>(R.id.weather_temp_text).text = "31°C"
        findViewById<TextView>(R.id.weather_desc_text).text = "Partly cloudy • 40% rain"
    }

    private fun setupFarmHealthCard(data: FarmHealth) {
        val badge = findViewById<TextView>(R.id.farm_health_badge)
        badge.text = "● Good"

        findViewById<TextView>(R.id.soil_moisture_text).text = data.soilMoisture
        findViewById<TextView>(R.id.pest_risk_text).text = data.pestRisk
        findViewById<TextView>(R.id.weather_alert_text).text = data.weatherAlert
    }

    private fun setupSustainabilityCard(data: FarmHealth) {
        findViewById<TextView>(R.id.score_text).text = data.ecoScore.toString()
        findViewById<ProgressBar>(R.id.score_progress).progress = data.ecoScore
        findViewById<TextView>(R.id.water_efficiency_text).text = data.waterEfficiency
        findViewById<TextView>(R.id.carbon_footprint_text).text = data.carbonFootprint

        findViewById<TextView>(R.id.sustainabilityDetailsBtn).setOnClickListener {
            val intent = android.content.Intent(this, SustainabilityDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupActionsList(actions: List<FarmAction>) {
        findViewById<TextView>(R.id.viewAllBtn).setOnClickListener {
            val intent = android.content.Intent(this, AllActionsActivity::class.java)
            startActivity(intent)
        }

        if (actions.isNotEmpty()) {
            val a1 = actions[0]
            findViewById<TextView>(R.id.act1_title).text = a1.title
            findViewById<TextView>(R.id.act1_desc).text = a1.desc
            findViewById<TextView>(R.id.act1_time).text = a1.time
        }

        if (actions.size > 1) {
            val a2 = actions[1]
            findViewById<TextView>(R.id.act2_title).text = a2.title
            findViewById<TextView>(R.id.act2_desc).text = a2.desc
            findViewById<TextView>(R.id.act2_time).text = a2.time
        }
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Reset to Home
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_crops -> {
                    val intent = android.content.Intent(this, CropsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_sensors -> {
                    // 1. Add this popup to prove the click works
                    //android.widget.Toast.makeText(this, "I AM CLICKED!", android.widget.Toast.LENGTH_LONG).show()

                    val intent = android.content.Intent(this, SensorsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_community -> {
                    val intent = android.content.Intent(this, CommunityActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Keep Home highlighted when you return
        findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId = R.id.nav_home
    }
}