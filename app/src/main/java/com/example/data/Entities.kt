package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val ecoPoints: Int = 0,
    val totalScans: Int = 0,
    val co2Saved: Double = 0.0, // in kg
    val plasticRecycled: Double = 0.0 // in kg
)

@Entity(tableName = "waste_scans")
data class WasteScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val category: String,
    val itemName: String,
    val isRecyclable: Boolean,
    val disposalMethod: String,
    val environmentalImpact: String,
    val recyclingTips: String,
    val safetyInstructions: String,
    val imageBase64: String, // Storing base64 to show scanned image in result and history
    val pointsEarned: Int
)

@Entity(tableName = "pickup_requests")
data class PickupRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wasteType: String,
    val weight: String,
    val address: String,
    val date: String,
    val timeSlot: String,
    val status: String, // "Pending", "Completed"
    val ecoPointsAwarded: Int
)

@Entity(tableName = "rewards")
data class RewardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val costPoints: Int,
    val promoCode: String,
    val isRedeemed: Boolean = false
)

@Entity(tableName = "recycling_centers")
data class RecyclingCenterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val distance: String,
    val acceptedWaste: String,
    val contact: String
)
