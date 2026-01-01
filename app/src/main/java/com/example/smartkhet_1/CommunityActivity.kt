package com.example.smartkhet_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        // 1. Setup Expert Button
        findViewById<Button>(R.id.btn_consult).setOnClickListener {
            Toast.makeText(this, "Connecting you to an Agricultural Expert...", Toast.LENGTH_LONG).show()
        }

        // 2. Setup AI Floating Button
        findViewById<FloatingActionButton>(R.id.fab_ask_ai).setOnClickListener {
            Toast.makeText(this, "AI Assistant: How can I help your farm today?", Toast.LENGTH_LONG).show()
        }

        // 3. Setup Navigation
        setupNavigation()
    }

    private fun setupNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Highlight "Community"
        bottomNav.selectedItemId = R.id.nav_community

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
                R.id.nav_sensors -> {
                    startActivity(Intent(this, SensorsActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_community -> true // Already here
                else -> false
            }
        }
    }
}