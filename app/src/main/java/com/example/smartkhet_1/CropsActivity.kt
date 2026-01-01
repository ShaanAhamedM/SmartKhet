package com.example.smartkhet_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CropsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crops)

        // Back Button
        findViewById<View>(R.id.backBtn).setOnClickListener { finish() }

        // Setup the Dummy Data
        setupCrop(R.id.crop1, "Wheat (Sona)", "North Field • Plot A", "Healthy", 45, 120)
        setupCrop(R.id.crop2, "Tomato (Hybrid)", "Greenhouse B", "Needs Water", 70, 90)
        setupCrop(R.id.crop3, "Corn (Maize)", "East Field • Sector 2", "Healthy", 20, 100)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Force "Crops" to be the selected (Green) item
        bottomNav.selectedItemId = R.id.nav_crops

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Go to Home
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_crops -> true // Already here, do nothing
                R.id.nav_sensors -> {
                    // Go to Sensors
                    val intent = Intent(this, SensorsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCrop(cardId: Int, name: String, loc: String, status: String, day: Int, totalDays: Int) {
        val card = findViewById<View>(cardId)

        card.findViewById<TextView>(R.id.crop_name).text = name
        card.findViewById<TextView>(R.id.crop_location).text = loc
        card.findViewById<TextView>(R.id.crop_status).text = status

        // Progress Logic
        val progress = (day.toFloat() / totalDays.toFloat() * 100).toInt()
        card.findViewById<ProgressBar>(R.id.crop_progress).progress = progress
        card.findViewById<TextView>(R.id.crop_stage_text).text = "Day $day of $totalDays"

        // Change color if "Needs Water"
        if (status == "Needs Water") {
            card.findViewById<TextView>(R.id.crop_status).setTextColor(resources.getColor(android.R.color.holo_orange_dark, theme))
        }
    }
}