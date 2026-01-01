package com.example.smartkhet_1

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AllActionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_actions)

        // 1. Back Button Logic
        findViewById<View>(R.id.backBtn).setOnClickListener {
            finish() // Closes this page and goes back
        }

        // 2. FAB (Plus Button) Logic
        findViewById<View>(R.id.fab_add_action).setOnClickListener {
            Toast.makeText(this, "Create New Action clicked!", Toast.LENGTH_SHORT).show()
        }

        // 3. Fill the Dummy Data manually for the "Full Page" feel
        setupRow(R.id.act1, "Irrigate Plot 1", "20mm water recommended", "Today, 5:30 PM")
        setupRow(R.id.act2, "Apply Organic Spray", "Tomato field", "Today, 6:00 PM")
        setupRow(R.id.act3, "Record Fertilizer", "Urea, 2 bags", "Today, 8:00 PM")
        setupRow(R.id.act4, "Check Soil pH", "North Field Sector 4", "Tomorrow, 7:00 AM")
        setupRow(R.id.act5, "Tool Maintenance", "Clean sprayer nozzles", "Tomorrow, 9:00 AM")
    }

    // Helper function to set text for our included rows
    private fun setupRow(rowId: Int, title: String, desc: String, time: String) {
        val row = findViewById<View>(rowId)
        row.findViewById<TextView>(R.id.action_title).text = title
        row.findViewById<TextView>(R.id.action_desc).text = desc
        row.findViewById<TextView>(R.id.action_time).text = time
        // We can also change icons here if we had them
    }
}