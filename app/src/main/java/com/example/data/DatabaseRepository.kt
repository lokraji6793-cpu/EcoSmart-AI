package com.example.data

import kotlinx.coroutines.flow.Flow

class DatabaseRepository(private val database: AppDatabase) {
    val user: Flow<UserEntity?> = database.userDao().getUser()
    val allScans: Flow<List<WasteScanEntity>> = database.wasteScanDao().getAllScans()
    val allPickupRequests: Flow<List<PickupRequestEntity>> = database.pickupRequestDao().getAllPickupRequests()
    val allRewards: Flow<List<RewardEntity>> = database.rewardDao().getAllRewards()
    val allCenters: Flow<List<RecyclingCenterEntity>> = database.recyclingCenterDao().getAllCenters()

    suspend fun insertOrUpdateUser(user: UserEntity) {
        database.userDao().insertOrUpdateUser(user)
    }

    suspend fun updateStatsFromScan(points: Int, co2: Double, plastic: Double) {
        database.userDao().updateStatsFromScan(points, co2, plastic)
    }

    suspend fun deductPoints(points: Int) {
        database.userDao().deductPoints(points)
    }

    suspend fun addPoints(points: Int) {
        database.userDao().addPoints(points)
    }

    suspend fun insertScan(scan: WasteScanEntity): Long {
        return database.wasteScanDao().insertScan(scan)
    }

    suspend fun insertPickupRequest(request: PickupRequestEntity) {
        database.pickupRequestDao().insertPickupRequest(request)
    }

    suspend fun updatePickupRequestStatus(id: Int, status: String) {
        database.pickupRequestDao().updatePickupRequestStatus(id, status)
    }

    suspend fun redeemReward(id: Int) {
        database.rewardDao().redeemReward(id)
    }
}
