package com.example.smartkhet_1

// 1. Data model for a Crop
data class Crop(
    val id: Int,
    val name: String,
    val location: String,
    val status: String,
    val currentDay: Int,
    val totalDays: Int,
    val moisture: Int
)

// 2. Data model for a Task/Action
data class FarmAction(
    val id: Int,
    val title: String,
    val desc: String,
    val time: String,
    val type: String // "water", "spray", "check" (used to pick icons)
)

// 3. Data model for Farm Health Stats
data class FarmHealth(
    val soilMoisture: String,
    val pestRisk: String,
    val weatherAlert: String,
    val ecoScore: Int,
    val waterEfficiency: String,
    val carbonFootprint: String
)

// 4. Data model for Sensor Readings
data class SensorData(
    val moisture: Int,
    val temp: Float,
    val ph: Float,
    val nitrogen: Int,
    val phosphorus: Int,
    val potassium: Int
)