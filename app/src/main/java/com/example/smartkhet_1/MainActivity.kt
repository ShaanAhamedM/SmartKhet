package com.example.smartkhet_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Link "View All" button to Actions Page
        val viewAllBtn = findViewById<android.widget.TextView>(R.id.viewAllBtn)
        viewAllBtn.setOnClickListener {
            val intent = android.content.Intent(this, AllActionsActivity::class.java)
            startActivity(intent)
        }

        // 2. Link "Details" button to Sustainability Page
        val sustainDetailsBtn = findViewById<android.widget.TextView>(R.id.sustainabilityDetailsBtn)
        sustainDetailsBtn.setOnClickListener {
            val intent = android.content.Intent(this, SustainabilityDetailsActivity::class.java)
            startActivity(intent)
        }

        // 3. Setup Bottom Navigation logic
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Ensure "Home" is selected
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_crops -> {
                    val intent = android.content.Intent(this, CropsActivity::class.java)
                    startActivity(intent)
                    // Remove the transition animation for a smoother feel
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_home -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset the navigation bar to "Home" whenever we return
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.nav_home
    }
}