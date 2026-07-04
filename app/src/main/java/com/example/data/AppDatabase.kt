package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        WasteScanEntity::class,
        PickupRequestEntity::class,
        RewardEntity::class,
        RecyclingCenterEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wasteScanDao(): WasteScanDao
    abstract fun pickupRequestDao(): PickupRequestDao
    abstract fun rewardDao(): RewardDao
    abstract fun recyclingCenterDao(): RecyclingCenterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecosmart_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            // Populate single User
            db.userDao().insertOrUpdateUser(
                UserEntity(
                    id = 1,
                    name = "SIH Innovator",
                    email = "sih2026@sih.gov.in",
                    ecoPoints = 350, // Give some starting points
                    totalScans = 0,
                    co2Saved = 0.0,
                    plasticRecycled = 0.0
                )
            )

            // Populate Rewards
            val initialRewards = listOf(
                RewardEntity(title = "₹100 Amazon Green Voucher", description = "Get ₹100 cashback voucher for environment-friendly products.", costPoints = 200, promoCode = "AMZGREEN100"),
                RewardEntity(title = "Free Home Compost Kit", description = "A complete 1kg home composting kit with microbial starter powder.", costPoints = 500, promoCode = "GROWORGANIC"),
                RewardEntity(title = "₹150 Uber Eco-Ride Voucher", description = "Get ₹150 off on Uber Electric rides across Tier 1 cities.", costPoints = 300, promoCode = "UBERECO150"),
                RewardEntity(title = "Bamboo Reusable Coffee Mug", description = "100% biodegradable and reusable premium coffee mug.", costPoints = 400, promoCode = "BAMBOOMUG"),
                RewardEntity(title = "Solar Mobile Charger (10k mAh)", description = "High speed portable charger with monocrystalline solar panels.", costPoints = 1200, promoCode = "SOLARCHG12")
            )
            db.rewardDao().insertRewards(initialRewards)

            // Populate Recycling Centers
            val initialCenters = listOf(
                RecyclingCenterEntity(name = "Delhi Eco-Recycling Depot", address = "Nehru Place, New Delhi, Delhi 110019", distance = "2.4 km", acceptedWaste = "E-Waste, Metal, Plastic", contact = "+91 98765 43210"),
                RecyclingCenterEntity(name = "Bengaluru Green-Cycle Facility", address = "Whitefield Industrial Area, Bengaluru, KA 560066", distance = "3.1 km", acceptedWaste = "Plastic, Glass, Paper", contact = "+91 91234 56789"),
                RecyclingCenterEntity(name = "Mumbai Swachh Recycle Hub", address = "Dharavi Industrial Area, Mumbai, MH 400017", distance = "4.5 km", acceptedWaste = "Paper, Plastics, Hazardous", contact = "+91 98111 22233"),
                RecyclingCenterEntity(name = "Chennai E-Waste Solutions", address = "Guindy Industrial Estate, Chennai, TN 600032", distance = "5.8 km", acceptedWaste = "E-Waste, Electronics, Metal", contact = "+91 94444 55555"),
                RecyclingCenterEntity(name = "Kolkata Waste-to-Worth Center", address = "Sector V, Salt Lake, Kolkata, WB 700091", distance = "6.2 km", acceptedWaste = "Biodegradable, Metal, Plastic", contact = "+91 93333 44444")
            )
            db.recyclingCenterDao().insertCenters(initialCenters)
        }
    }
}
