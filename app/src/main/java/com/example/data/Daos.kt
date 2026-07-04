package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("UPDATE users SET ecoPoints = ecoPoints + :points, totalScans = totalScans + 1, co2Saved = co2Saved + :co2, plasticRecycled = plasticRecycled + :plastic WHERE id = 1")
    suspend fun updateStatsFromScan(points: Int, co2: Double, plastic: Double)

    @Query("UPDATE users SET ecoPoints = ecoPoints - :points WHERE id = 1")
    suspend fun deductPoints(points: Int)

    @Query("UPDATE users SET ecoPoints = ecoPoints + :points WHERE id = 1")
    suspend fun addPoints(points: Int)
}

@Dao
interface WasteScanDao {
    @Query("SELECT * FROM waste_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<WasteScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: WasteScanEntity): Long

    @Query("SELECT * FROM waste_scans WHERE id = :id")
    fun getScanById(id: Int): Flow<WasteScanEntity?>
}

@Dao
interface PickupRequestDao {
    @Query("SELECT * FROM pickup_requests ORDER BY id DESC")
    fun getAllPickupRequests(): Flow<List<PickupRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPickupRequest(request: PickupRequestEntity)

    @Query("UPDATE pickup_requests SET status = :status WHERE id = :id")
    suspend fun updatePickupRequestStatus(id: Int, status: String)
}

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards ORDER BY costPoints ASC")
    fun getAllRewards(): Flow<List<RewardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewards(rewards: List<RewardEntity>)

    @Query("UPDATE rewards SET isRedeemed = 1 WHERE id = :id")
    suspend fun redeemReward(id: Int)
}

@Dao
interface RecyclingCenterDao {
    @Query("SELECT * FROM recycling_centers ORDER BY id ASC")
    fun getAllCenters(): Flow<List<RecyclingCenterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCenters(centers: List<RecyclingCenterEntity>)
}
