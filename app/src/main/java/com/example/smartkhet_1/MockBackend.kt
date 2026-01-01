package com.example.smartkhet_1

object MockBackend {

    // --- API 1: Get Farm Status ---
    fun getFarmHealth(): FarmHealth {
        // Later, replace this return with real sensor data
        return FarmHealth(
            soilMoisture = "Optimal",
            pestRisk = "Low",
            weatherAlert = "None",
            ecoScore = 78,
            waterEfficiency = "Good",
            carbonFootprint = "Improving"
        )
    }

    // --- API 2: Get Today's Actions ---
    fun getTodayActions(): List<FarmAction> {
        return listOf(
            FarmAction(1, "Irrigate Plot 1", "20 mm water recommended", "Today, 5:30 PM", "water"),
            FarmAction(2, "Apply organic pest spray", "Tomato field", "Today, 6:00 PM", "spray"),
            FarmAction(3, "Record Fertilizer", "Urea, 2 bags", "Today, 8:00 PM", "check"),
            FarmAction(4, "Check Soil pH", "North Field Sector 4", "Tomorrow, 7:00 AM", "check")
        )
    }

    // --- API 3: Get All Crops ---
    fun getCrops(): List<Crop> {
        return listOf(
            Crop(1, "Wheat (Sona)", "North Field • Plot A", "Healthy", 45, 120, 65),
            Crop(2, "Tomato (Hybrid)", "Greenhouse B", "Needs Water", 70, 90, 40),
            Crop(3, "Corn (Maize)", "East Field • Sector 2", "Healthy", 20, 100, 72)
        )
    }

    // --- API 4: Get Real-time Sensor Data ---
    fun getSensorReadings(): SensorData {
        return SensorData(
            moisture = 65,       // 65%
            temp = 31.5f,        // 31.5 Celsius
            ph = 6.8f,           // pH Level
            nitrogen = 80,       // N level
            phosphorus = 60,     // P level
            potassium = 40       // K level
        )
    }


}