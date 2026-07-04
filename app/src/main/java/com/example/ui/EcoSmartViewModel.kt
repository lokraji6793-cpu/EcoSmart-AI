package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiWasteScanner
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface ScanUiState {
    object Idle : ScanUiState
    object Scanning : ScanUiState
    data class Success(val result: GeminiWasteScanner.ScanResult, val scanId: Int, val bitmap: Bitmap) : ScanUiState
    data class Error(val message: String) : ScanUiState
}

sealed interface RedeemUiState {
    object Idle : RedeemUiState
    data class Success(val reward: RewardEntity) : RedeemUiState
    data class Error(val message: String) : RedeemUiState
}

class EcoSmartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DatabaseRepository

    val user: StateFlow<UserEntity?>
    val allScans: StateFlow<List<WasteScanEntity>>
    val allPickupRequests: StateFlow<List<PickupRequestEntity>>
    val allRewards: StateFlow<List<RewardEntity>>
    val allCenters: StateFlow<List<RecyclingCenterEntity>>

    private val _scanState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanState: StateFlow<ScanUiState> = _scanState.asStateFlow()

    private val _redeemState = MutableStateFlow<RedeemUiState>(RedeemUiState.Idle)
    val redeemState: StateFlow<RedeemUiState> = _redeemState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = DatabaseRepository(database)

        // Read data as flows, with robust caching
        user = repository.user.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        allScans = repository.allScans.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allPickupRequests = repository.allPickupRequests.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allRewards = repository.allRewards.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allCenters = repository.allCenters.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Login simulation
    fun loginOrSignUp(name: String, email: String) {
        viewModelScope.launch {
            repository.insertOrUpdateUser(
                UserEntity(
                    id = 1,
                    name = name.ifBlank { "SIH Innovator" },
                    email = email.ifBlank { "sih2026@sih.gov.in" },
                    ecoPoints = user.value?.ecoPoints ?: 350,
                    totalScans = user.value?.totalScans ?: 0,
                    co2Saved = user.value?.co2Saved ?: 0.0,
                    plasticRecycled = user.value?.plasticRecycled ?: 0.0
                )
            )
        }
    }

    // Clear Scan State
    fun clearScanState() {
        _scanState.value = ScanUiState.Idle
    }

    // Run AI scanner
    fun scanWaste(bitmap: Bitmap) {
        _scanState.value = ScanUiState.Scanning
        viewModelScope.launch {
            try {
                val result = GeminiWasteScanner.scanWasteImage(bitmap)

                // Convert bitmap to Base64 to save in database
                val baos = java.io.ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                val base64 = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.NO_WRAP)

                val scanEntity = WasteScanEntity(
                    timestamp = System.currentTimeMillis(),
                    category = result.category,
                    itemName = result.itemName,
                    isRecyclable = result.isRecyclable,
                    disposalMethod = result.disposalMethod,
                    environmentalImpact = result.environmentalImpact,
                    recyclingTips = result.recyclingTips,
                    safetyInstructions = result.safetyInstructions,
                    imageBase64 = base64,
                    pointsEarned = result.points
                )

                // Save scan to database
                val id = repository.insertScan(scanEntity).toInt()

                // Update user stats
                repository.updateStatsFromScan(
                    points = result.points,
                    co2 = result.co2Saved,
                    plastic = result.plasticRecycled
                )

                _scanState.value = ScanUiState.Success(result, id, bitmap)
            } catch (e: Exception) {
                _scanState.value = ScanUiState.Error(e.localizedMessage ?: "Unknown scanning error")
            }
        }
    }

    // Redeem a reward
    fun redeemReward(reward: RewardEntity) {
        _redeemState.value = RedeemUiState.Idle
        val currentUser = user.value ?: return

        if (currentUser.ecoPoints < reward.costPoints) {
            _redeemState.value = RedeemUiState.Error("Insufficient Eco Points. Earn more by scanning waste!")
            return
        }

        viewModelScope.launch {
            try {
                // Deduct points
                repository.deductPoints(reward.costPoints)
                // Mark reward as redeemed
                repository.redeemReward(reward.id)
                _redeemState.value = RedeemUiState.Success(reward.copy(isRedeemed = true))
            } catch (e: Exception) {
                _redeemState.value = RedeemUiState.Error(e.localizedMessage ?: "Error redeeming reward")
            }
        }
    }

    fun clearRedeemState() {
        _redeemState.value = RedeemUiState.Idle
    }

    // Book a pickup request
    fun createPickupRequest(wasteType: String, weight: String, address: String, date: String, slot: String) {
        viewModelScope.launch {
            val pointsEarned = 100 // standard reward for requesting pick up
            val request = PickupRequestEntity(
                wasteType = wasteType,
                weight = weight,
                address = address,
                date = date,
                timeSlot = slot,
                status = "Pending",
                ecoPointsAwarded = pointsEarned
            )
            repository.insertPickupRequest(request)
            // Add initial eco points when scheduled successfully!
            repository.addPoints(pointsEarned)
        }
    }

    // Complete/Cancel pickup request
    fun updatePickupStatus(requestId: Int, status: String) {
        viewModelScope.launch {
            repository.updatePickupRequestStatus(requestId, status)
        }
    }
}
