package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.R
import com.example.data.*
import kotlinx.coroutines.delay
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat
import java.util.*

// Styling helper colors (Natural Tones Theme)
val DarkGreen = Color(0xFF4F6D44)       // Forest Green
val MidGreen = Color(0xFF7DA06A)        // Sage Green
val LightGreen = Color(0xFFE5E9D3)      // Tea Green
val SoftGreen = Color(0xFF8EAA83)       // Soft Sage
val EmeraldAccent = Color(0xFF7DA06A)   // Sage Green Accent
val AmberGold = Color(0xFFD4A359)       // Earthy Amber
val PaleBackground = Color(0xFFFBFBF2)  // Warm Ivory Background
val CardBackgroundLight = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A2421)        // Earthy Dark Text
val LightIvory = Color(0xFFF7F9F2)      // Very light ivory/sage background

@Composable
fun EcoSmartApp(
    viewModel: EcoSmartViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController, viewModel)
        }
        composable("login") {
            LoginScreen(navController, viewModel)
        }
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("scanner") {
            ScannerScreen(navController, viewModel)
        }
        composable("scan_result/{scanId}") { backStackEntry ->
            val scanId = backStackEntry.arguments?.getString("scanId")?.toIntOrNull() ?: 0
            ScanResultScreen(navController, viewModel, scanId)
        }
        composable("recycling_centers") {
            RecyclingCentersScreen(navController, viewModel)
        }
        composable("pickup_request") {
            PickupRequestScreen(navController, viewModel)
        }
        composable("rewards") {
            RewardsScreen(navController, viewModel)
        }
        composable("dashboard") {
            DashboardScreen(navController, viewModel)
        }
        composable("profile") {
            ProfileScreen(navController, viewModel)
        }
    }
}

// ==========================================
// 1. SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        delay(2200)
        if (user != null) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkGreen, MidGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "Eco Logo",
                    tint = EmeraldAccent,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "EcoSmart Ai",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Smart Waste Management Solution",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            CircularProgressIndicator(
                color = EmeraldAccent,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Smart India Hackathon 2026\nID: SIH25060",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// 2. LOGIN SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Background image from generated asset
                Image(
                    painter = painterResource(id = R.drawable.img_eco_banner_1783078521584),
                    contentDescription = "Eco Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, DarkGreen.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "EcoSmart Ai",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Empowering sustainable solid waste solutions",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Welcome Innovator!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
                Text(
                    text = "Sign in to join the smart waste recycling mission.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DarkGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("login_name_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        focusedLabelColor = DarkGreen
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Your Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = DarkGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                        .testTag("login_email_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        focusedLabelColor = DarkGreen
                    )
                )

                Button(
                    onClick = {
                        if (name.isBlank() || email.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.loginOrSignUp(name, email)
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Get Started", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(48.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "SIH Info",
                            tint = MidGreen,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SIH 2026 Innovation Hub",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DarkGreen
                            )
                            Text(
                                text = "Under problem statement SIH25060, this portal tracks household and corporate dry/wet waste cycles dynamically.",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. HOME SCREEN
// ==========================================
@Composable
fun HomeScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val scans by viewModel.allScans.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreen,
                        selectedTextColor = DarkGreen,
                        indicatorColor = PaleBackground
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("scanner") },
                    icon = { Icon(Icons.Default.QrCodeScanner, "Scan") },
                    label = { Text("Scan") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Default.BarChart, "Dashboard") },
                    label = { Text("Stats") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, "Profile") },
                    label = { Text("Profile") }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header bar in Natural Tones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "EcoSmart Ai",
                            color = MidGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Namaste, ${user?.name ?: "SIH Innovator"}",
                            color = TextDark,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Avatar layout from Design HTML
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(LightGreen)
                            .border(2.dp, MidGreen.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(MidGreen, DarkGreen)
                                    ),
                                    alpha = 0.8f
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (user?.name ?: "SIH").take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Total Impact section card (Design HTML)
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MidGreen),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Total Impact",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = String.format("%.1f", user?.co2Saved ?: 0.0),
                                        color = Color.White,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "kg CO₂",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }

                            // Dynamic points pill
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${user?.ecoPoints ?: 0} pts",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Weekly Goal progress bar (Design HTML style)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Weekly Goal Progress",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "75%",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(Color.Black.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(0.75f)
                                        .background(Color.White, CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid stats from design
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Waste Scanned",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${user?.totalScans ?: 0} items",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Plastic Recycled",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${String.format("%.2f", user?.plasticRecycled ?: 0.0)} kg",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Quick Actions section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Grid actions in Natural Tones design
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        icon = Icons.Default.PhotoCamera,
                        title = "Scan Waste",
                        description = "Identify & classify via AI",
                        backgroundColor = LightGreen,
                        tintColor = DarkGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("scan_waste_action")
                    ) {
                        navController.navigate("scanner")
                    }
                    ActionCard(
                        icon = Icons.Default.Map,
                        title = "Recycling Centers",
                        description = "Locate nearby hubs",
                        backgroundColor = Color.White,
                        tintColor = MidGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("recycling_centers_action")
                    ) {
                        navController.navigate("recycling_centers")
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        icon = Icons.Default.LocalShipping,
                        title = "Request Pickup",
                        description = "Doorstep green collection",
                        backgroundColor = Color.White,
                        tintColor = MidGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("request_pickup_action")
                    ) {
                        navController.navigate("pickup_request")
                    }
                    ActionCard(
                        icon = Icons.Default.CardGiftcard,
                        title = "Rewards Shop",
                        description = "Redeem your eco-points",
                        backgroundColor = Color.White,
                        tintColor = MidGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("rewards_action")
                    ) {
                        navController.navigate("rewards")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        icon = Icons.Default.BarChart,
                        title = "Dashboard",
                        description = "View weekly graphs & stats",
                        backgroundColor = Color.White,
                        tintColor = MidGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_action")
                    ) {
                        navController.navigate("dashboard")
                    }
                    ActionCard(
                        icon = Icons.Default.AccountCircle,
                        title = "Profile Settings",
                        description = "Manage account & SIH info",
                        backgroundColor = Color.White,
                        tintColor = MidGreen,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("profile_action")
                    ) {
                        navController.navigate("profile")
                    }
                }
            }

            // Green Hackathon motivation banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Smart India Hackathon Goal",
                        color = EmeraldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SIH25060 tackles garbage monitoring and resource recovery. Scan non-biodegradable, hazard or dry wastes to recover secondary raw materials and secure dynamic eco points.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // Recent activity preview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Recent Scans",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (scans.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DocumentScanner,
                                contentDescription = "Scan icon",
                                tint = Color.LightGray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No waste scanned yet!",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Tap 'Scan Waste' to classify and earn Eco Points.",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    scans.take(3).forEach { scan ->
                        RecentScanRow(scan) {
                            navController.navigate("scan_result/${scan.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImpactSummaryItem(icon: ImageVector, label: String, value: String, tint: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = TextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(
    icon: ImageVector,
    title: String,
    description: String,
    backgroundColor: Color,
    tintColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(24.dp)
    Card(
        onClick = onClick,
        modifier = modifier
            .height(115.dp)
            .then(
                if (backgroundColor == Color.White) {
                    Modifier.border(1.dp, LightGreen.copy(alpha = 0.6f), cardShape)
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(if (backgroundColor == Color.White) 0.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (backgroundColor == Color.White) LightIvory else Color.White,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = tintColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    lineHeight = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentScanRow(scan: WasteScanEntity, onClick: () -> Unit) {
    val dateText = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(scan.timestamp))

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Render small image if base64 exists, otherwise render icon
            if (scan.imageBase64.isNotBlank()) {
                val bytes = android.util.Base64.decode(scan.imageBase64, android.util.Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(bytes))
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = scan.itemName,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    FallbackCategoryIcon(scan.category)
                }
            } else {
                FallbackCategoryIcon(scan.category)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = scan.itemName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    CategoryChip(category = scan.category)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateText,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "+${scan.pointsEarned}",
                    color = MidGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "pts",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun FallbackCategoryIcon(category: String) {
    val color = getCategoryColor(category)
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getCategoryIcon(category),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Helpers for category visualizers
fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "plastic" -> Color(0xFF1976D2) // Blue
        "recyclable" -> Color(0xFF388E3C) // Green
        "biodegradable" -> Color(0xFF8D6E63) // Brown
        "e-waste" -> Color(0xFF5E35B1) // Purple
        "metal" -> Color(0xFF455A64) // Blue Gray
        "glass" -> Color(0xFF00ACC1) // Teal
        "hazardous" -> Color(0xFFD32F2F) // Red
        else -> Color(0xFF757575)
    }
}

fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "plastic" -> Icons.Default.Layers
        "recyclable" -> Icons.Default.Autorenew
        "biodegradable" -> Icons.Default.Eco
        "e-waste" -> Icons.Default.DeveloperBoard
        "metal" -> Icons.Default.Build
        "glass" -> Icons.Default.LocalDrink
        "hazardous" -> Icons.Default.Warning
        else -> Icons.Default.DeleteOutline
    }
}

@Composable
fun CategoryChip(category: String) {
    val color = getCategoryColor(category)
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = category,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ==========================================
// 4. SCANNER SCREEN (WITH GALLERY & PRE-DEFINED MOCKS)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val context = LocalContext.current
    val scanState by viewModel.scanState.collectAsStateWithLifecycle()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Launcher for photo pick
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                // Downscale bitmap before storing and sending to API to optimize
                selectedBitmap = scaleBitmapDown(bitmap, 800)
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Direct simulation selection list (MOCK DATA FOR EMULATOR USE CASES)
    val preDefinedMocks = listOf(
        Pair("Mineral Bottle", R.drawable.ic_launcher_foreground),
        Pair("Watermelon Peel", R.drawable.ic_launcher_background),
        Pair("Aluminum Can", R.drawable.ic_launcher_foreground),
        Pair("Faulty Smart Watch", R.drawable.ic_launcher_foreground),
        Pair("Shattered Glass Bowl", R.drawable.ic_launcher_foreground)
    )

    // Handle scan success route change
    LaunchedEffect(scanState) {
        if (scanState is ScanUiState.Success) {
            val success = scanState as ScanUiState.Success
            navController.navigate("scan_result/${success.scanId}") {
                popUpTo("scanner") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Waste Scanner") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedBitmap != null) {
                    Image(
                        bitmap = selectedBitmap!!.asImageBitmap(),
                        contentDescription = "Selected waste",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )

                    // Overlay scanning animation if loading
                    if (scanState is ScanUiState.Scanning) {
                        RadarScanAnimation()
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Upload",
                            tint = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No image selected",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Upload a photo from gallery or try a sample waste item below to run the AI Classifier.",
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Upload Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("gallery_picker_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = MidGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("From Gallery")
                }

                Button(
                    onClick = {
                        // For emulator we can create a simple demo dummy bitmap so they can trigger camera simulation
                        val dummyBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
                        val canvas = android.graphics.Canvas(dummyBitmap)
                        canvas.drawColor(android.graphics.Color.DKGRAY)
                        selectedBitmap = dummyBitmap
                        Toast.makeText(context, "Virtual snapshot captured!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("camera_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Snap Photo")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Analyze action
            if (selectedBitmap != null && scanState !is ScanUiState.Scanning) {
                Button(
                    onClick = { viewModel.scanWaste(selectedBitmap!!) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(52.dp)
                        .testTag("analyze_waste_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldAccent, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyze Waste with Gemini", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Mock selection library - CRITICAL FOR EMULATORS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Don't have an image? Try a Sample Item",
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Perfect for testing on Streaming Android Emulators directly in the browser.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        preDefinedMocks.forEach { mock ->
                            Box(
                                modifier = Modifier
                                    .background(PaleBackground, RoundedCornerShape(8.dp))
                                    .clickable {
                                        // Load corresponding dummy icon drawable as a sample bitmap
                                        try {
                                            val options = BitmapFactory.Options().apply { inMutable = true }
                                            val bmp = BitmapFactory.decodeResource(context.resources, mock.second, options)
                                            selectedBitmap = bmp ?: Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).apply {
                                                val c = android.graphics.Canvas(this)
                                                c.drawColor(android.graphics.Color.GREEN)
                                            }
                                            Toast.makeText(context, "Loaded sample: ${mock.first}", Toast.LENGTH_SHORT).show()
                                        } catch (e: Exception) {
                                            // Fallback
                                            selectedBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).apply {
                                                val c = android.graphics.Canvas(this)
                                                c.drawColor(android.graphics.Color.GREEN)
                                            }
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Category,
                                        contentDescription = null,
                                        tint = MidGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = mock.first,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (scanState is ScanUiState.Scanning) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = DarkGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Gemini AI is analyzing the molecular & visual structure of the waste...",
                            fontSize = 12.sp,
                            color = DarkGreen,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (scanState is ScanUiState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = (scanState as ScanUiState.Error).message,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RadarScanAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "Radar")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 280f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Radar line"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Drawing horizontal radar scanning line
                drawLine(
                    color = EmeraldAccent,
                    start = Offset(0f, offsetY.dp.toPx()),
                    end = Offset(size.width, offsetY.dp.toPx()),
                    strokeWidth = 6f
                )
                // Draw a light gradient overlay following the scanning line
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, EmeraldAccent.copy(alpha = 0.15f), Color.Transparent),
                        startY = (offsetY.dp.toPx() - 40f).coerceAtLeast(0f),
                        endY = (offsetY.dp.toPx() + 40f).coerceAtMost(size.height)
                    ),
                    topLeft = Offset.Zero,
                    size = size
                )
            }
    )
}

// scale down bitmap
fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
    val originalWidth = bitmap.width
    val originalHeight = bitmap.height
    var resizedWidth = maxDimension
    var resizedHeight = maxDimension
    if (originalHeight > originalWidth) {
        resizedHeight = maxDimension
        resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
    } else if (originalWidth > originalHeight) {
        resizedWidth = maxDimension
        resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
    } else if (originalHeight == originalWidth) {
        resizedHeight = maxDimension
        resizedWidth = maxDimension
    }
    return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
}

// ==========================================
// 5. SCAN RESULT SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController,
    viewModel: EcoSmartViewModel,
    scanId: Int
) {
    val scans by viewModel.allScans.collectAsStateWithLifecycle()
    val scan = scans.find { it.id == scanId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Analysis Result") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearScanState()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        if (scan == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Scan details not found.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Waste Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                if (scan.imageBase64.isNotBlank()) {
                    val bytes = android.util.Base64.decode(scan.imageBase64, android.util.Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(bytes))
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = scan.itemName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
            }

            // Identified Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = scan.itemName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = TextDark
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            CategoryChip(category = scan.category)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (scan.isRecyclable) EmeraldAccent.copy(alpha = 0.15f) else Color.LightGray,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (scan.isRecyclable) "RECYCLABLE" else "NON-RECYCLABLE",
                                    color = if (scan.isRecyclable) DarkGreen else Color.DarkGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Eco-points award pill
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AmberGold.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "+${scan.pointsEarned}",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color(0xFFD48C00)
                            )
                            Text(
                                text = "Eco Points",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB27A00)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info segments
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultInfoCard(
                    icon = Icons.Default.DirectionsRun,
                    title = "Disposal Method",
                    description = scan.disposalMethod,
                    color = getCategoryColor(scan.category)
                )

                ResultInfoCard(
                    icon = Icons.Default.Co2,
                    title = "Environmental Impact",
                    description = scan.environmentalImpact,
                    color = Color(0xFFD32F2F)
                )

                ResultInfoCard(
                    icon = Icons.Default.Lightbulb,
                    title = "Recycling Tips",
                    description = scan.recyclingTips,
                    color = AmberGold
                )

                ResultInfoCard(
                    icon = Icons.Default.Shield,
                    title = "Safety Instructions",
                    description = scan.safetyInstructions,
                    color = Color(0xFF00796B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.clearScanState()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("scan_result_done_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Return to Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ResultInfoCard(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ==========================================
// 6. NEARBY RECYCLING CENTERS SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecyclingCentersScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val centers by viewModel.allCenters.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val filteredCenters = centers.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.address.contains(searchQuery, ignoreCase = true) ||
                it.acceptedWaste.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recycling Centers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by city, materials, or name...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DarkGreen) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("centers_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredCenters.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.LocationOff, null, tint = Color.LightGray, modifier = Modifier.size(64.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("No centers matched your query", fontWeight = FontWeight.Bold, color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredCenters) { center ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = center.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = TextDark
                                        )
                                        Text(
                                            text = center.address,
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .background(MidGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = center.distance,
                                            color = DarkGreen,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Category,
                                        contentDescription = null,
                                        tint = MidGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Accepted: ${center.acceptedWaste}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            Toast.makeText(context, "Routing to ${center.name}...", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkGreen)
                                    ) {
                                        Icon(Icons.Default.Directions, null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Navigate", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            Toast.makeText(context, "Dialing ${center.contact}...", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                                    ) {
                                        Icon(Icons.Default.Phone, null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. DOORSTEP PICKUP REQUEST SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupRequestScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val pickupRequests by viewModel.allPickupRequests.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val categories = listOf("Plastic", "E-Waste", "Metal", "Glass", "Biodegradable", "Mix Dry Waste")
    val weightSlots = listOf("< 5 kg", "5 - 15 kg", "15 - 30 kg", "30+ kg")
    val timeSlots = listOf("9 AM - 12 PM", "12 PM - 3 PM", "3 PM - 6 PM")

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedWeight by remember { mutableStateOf(weightSlots[0]) }
    var selectedSlot by remember { mutableStateOf(timeSlots[0]) }

    var address by remember { mutableStateOf("") }
    var pickDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Doorstep Pickup") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Schedule New Pickup",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkGreen
                    )
                    Text(
                        text = "A municipal helper will retrieve dry/wet trash right from your doorstep.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Waste Category Chip Row
                    Text("Waste Type", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) DarkGreen else Color.LightGray.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color.White else TextDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Weight category chip row
                    Text("Estimated Weight", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        weightSlots.forEach { wt ->
                            val isSelected = selectedWeight == wt
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) DarkGreen else Color.LightGray.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedWeight = wt }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = wt,
                                    color = if (isSelected) Color.White else TextDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Time slot chip row
                    Text("Preferred Time Slot", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        timeSlots.forEach { slot ->
                            val isSelected = selectedSlot == slot
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isSelected) DarkGreen else Color.LightGray.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedSlot = slot }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = slot,
                                    color = if (isSelected) Color.White else TextDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = pickDate,
                        onValueChange = { pickDate = it },
                        label = { Text("Preferred Date (DD/MM/YYYY)") },
                        placeholder = { Text("e.g. 05/07/2026") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = DarkGreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("pickup_date_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Collection Address") },
                        leadingIcon = { Icon(Icons.Default.HomeWork, null, tint = DarkGreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 8.dp)
                            .testTag("pickup_address_input"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (pickDate.isBlank() || address.isBlank()) {
                                Toast.makeText(context, "Please complete the date and address fields", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.createPickupRequest(
                                    wasteType = selectedCategory,
                                    weight = selectedWeight,
                                    address = address,
                                    date = pickDate,
                                    slot = selectedSlot
                                )
                                Toast.makeText(context, "Green Doorstep Collection scheduled successfully!", Toast.LENGTH_LONG).show()
                                pickDate = ""
                                address = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("pickup_submit_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Schedule Doorstep Pickup (+100 pts)", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // List history
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "Collection History",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (pickupRequests.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "No prior pickups scheduled.",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(24.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    pickupRequests.forEach { request ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${request.wasteType} Collection",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextDark
                                    )
                                    Text(
                                        text = "Date: ${request.date} | Slot: ${request.timeSlot}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "To: ${request.address}",
                                        fontSize = 11.sp,
                                        color = Color.LightGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (request.status == "Pending") Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = request.status.uppercase(),
                                            color = if (request.status == "Pending") Color(0xFFE65100) else DarkGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    if (request.status == "Pending") {
                                        TextButton(
                                            onClick = {
                                                viewModel.updatePickupStatus(request.id, "Completed")
                                                Toast.makeText(context, "Status updated! Earned points.", Toast.LENGTH_SHORT).show()
                                            },
                                            contentPadding = PaddingValues(0.dp),
                                            modifier = Modifier.height(24.dp)
                                        ) {
                                            Text("Mark Complete", fontSize = 11.sp, color = MidGreen)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. REWARDS SHOP SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val rewards by viewModel.allRewards.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val redeemState by viewModel.redeemState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var activeRedeemedReward by remember { mutableStateOf<RewardEntity?>(null) }

    LaunchedEffect(redeemState) {
        if (redeemState is RedeemUiState.Success) {
            activeRedeemedReward = (redeemState as RedeemUiState.Success).reward
            Toast.makeText(context, "Reward Redeemed Successfully!", Toast.LENGTH_SHORT).show()
            viewModel.clearRedeemState()
        } else if (redeemState is RedeemUiState.Error) {
            Toast.makeText(context, (redeemState as RedeemUiState.Error).message, Toast.LENGTH_LONG).show()
            viewModel.clearRedeemState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eco Rewards Shop") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
        ) {
            // Eco Points Balance Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGreen)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("YOUR CURRENT BALANCE", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Eco, null, tint = EmeraldAccent, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${user?.ecoPoints ?: 0} PTS",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text("Earn more by sorting recyclables & requesting smart pickups.", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rewards) { reward ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(AmberGold.copy(alpha = 0.12f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (reward.isRedeemed) Icons.Default.CheckCircle else Icons.Default.CardGiftcard,
                                    contentDescription = null,
                                    tint = if (reward.isRedeemed) MidGreen else AmberGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = reward.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextDark
                                )
                                Text(
                                    text = reward.description,
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Eco, null, tint = MidGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${reward.costPoints} points",
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGreen,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            if (reward.isRedeemed) {
                                Button(
                                    onClick = { activeRedeemedReward = reward },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("View Code", fontSize = 11.sp, color = Color.DarkGray)
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.redeemReward(reward) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MidGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("redeem_btn_${reward.id}")
                                ) {
                                    Text("Redeem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Promo Code presentation dialog
        if (activeRedeemedReward != null) {
            Dialog(onDismissRequest = { activeRedeemedReward = null }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = "Success",
                            tint = AmberGold,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Redemption Confirmed!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = DarkGreen
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activeRedeemedReward!!.title,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Box showing the code
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PaleBackground, RoundedCornerShape(12.dp))
                                .border(1.dp, DarkGreen, RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = activeRedeemedReward!!.promoCode,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkGreen,
                                letterSpacing = 2.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Copy and paste this promotional code at checkout to claim your eco-conscious deal.",
                            fontSize = 11.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { activeRedeemedReward = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Done", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. DATA ANALYTICS & WEEKLY DASHBOARD SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val scans by viewModel.allScans.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eco Impact Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Stats summary card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Your Lifetime Contribution",
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CO₂ Saved", color = Color.Gray, fontSize = 11.sp)
                            Text("${String.format("%.2f", user?.co2Saved ?: 0.0)} kg", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFFD32F2F))
                        }
                        Column {
                            Text("Plastic Recovered", color = Color.Gray, fontSize = 11.sp)
                            Text("${String.format("%.2f", user?.plasticRecycled ?: 0.0)} kg", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = LightGreen)
                        }
                        Column {
                            Text("Total Scanned", color = Color.Gray, fontSize = 11.sp)
                            Text("${user?.totalScans ?: 0} items", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1565C0))
                        }
                    }
                }
            }

            // Custom progress graph
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Weekly Sorting Progress",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DarkGreen
                    )
                    Text(
                        text = "Scanned elements across the last 7 days",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Draw the custom bar chart using canvas drawing
                    // We can pre-calculate some realistic weekly sample points
                    val dailyScanPoints = listOf(
                        Pair("Mon", 1),
                        Pair("Tue", 3),
                        Pair("Wed", 2),
                        Pair("Thu", 5),
                        Pair("Fri", 4),
                        Pair("Sat", 7),
                        Pair("Sun", (user?.totalScans ?: 2).coerceAtMost(10)) // dynamic bar scaling based on real scan count!
                    )

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        val barWidth = 35.dp.toPx()
                        val spacing = (size.width - (barWidth * dailyScanPoints.size)) / (dailyScanPoints.size + 1)
                        val maxVal = 8f

                        // Draw baseline
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, size.height - 30f),
                            end = Offset(size.width, size.height - 30f),
                            strokeWidth = 2f
                        )

                        dailyScanPoints.forEachIndexed { index, pair ->
                            val xPos = spacing + index * (barWidth + spacing)
                            val barHeight = ((pair.second.toFloat() / maxVal) * (size.height - 60f)).coerceAtLeast(10f)
                            val yPos = size.height - 30f - barHeight

                            // Draw individual styled bar
                            drawRoundRect(
                                color = if (index == dailyScanPoints.size - 1) EmeraldAccent else MidGreen,
                                topLeft = Offset(xPos, yPos),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(8f, 8f)
                            )

                            // Draw data text value on top of bar
                            drawContext.canvas.nativeCanvas.apply {
                                val paint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.DKGRAY
                                    textSize = 28f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                                }
                                drawText(pair.second.toString(), xPos + barWidth / 2, yPos - 12f, paint)

                                // Draw day label below baseline
                                val labelPaint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.GRAY
                                    textSize = 24f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                                drawText(pair.first, xPos + barWidth / 2, size.height - 4f, labelPaint)
                            }
                        }
                    }
                }
            }

            // Gamified Badges / milestones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Sustainability Badges",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkGreen,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BadgeItem(
                        icon = Icons.Default.Terrain,
                        title = "Zero Landfill",
                        isUnlocked = (user?.totalScans ?: 0) >= 1,
                        modifier = Modifier.weight(1f)
                    )
                    BadgeItem(
                        icon = Icons.Default.Bolt,
                        title = "Eco Warrior",
                        isUnlocked = (user?.ecoPoints ?: 0) >= 500,
                        modifier = Modifier.weight(1f)
                    )
                    BadgeItem(
                        icon = Icons.Default.LocalShipping,
                        title = "Smart Citizen",
                        isUnlocked = scans.size >= 5,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeItem(
    icon: ImageVector,
    title: String,
    isUnlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color.White else Color.LightGray.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (isUnlocked) EmeraldAccent.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isUnlocked) DarkGreen else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (isUnlocked) TextDark else Color.Gray,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (isUnlocked) "UNLOCKED" else "LOCKED",
                fontSize = 8.sp,
                color = if (isUnlocked) MidGreen else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// 10. PROFILE SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController, viewModel: EcoSmartViewModel) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile & Hackathon Hub") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaleBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar circle
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(DarkGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (user?.name?.take(2)?.uppercase() ?: "SI"),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(EmeraldAccent, CircleShape)
                        .align(Alignment.BottomEnd)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, null, tint = DarkGreen, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.name ?: "SIH Innovator",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextDark
            )

            Text(
                text = user?.email ?: "sih2026@sih.gov.in",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // SIH Specific parameters card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Hackathon Specifications",
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    ProfileSpecRow(label = "Problem Statement ID", value = "SIH25060")
                    ProfileSpecRow(label = "Nodal Center", value = "AI Studio Virtual Sandbox")
                    ProfileSpecRow(label = "Category", value = "Smart Cities / Waste Management")
                    ProfileSpecRow(label = "Platform", value = "Android (Kotlin / Compose)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign out
            Button(
                onClick = {
                    viewModel.loginOrSignUp("SIH Innovator", "sih2026@sih.gov.in")
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                    Toast.makeText(context, "Logged Out Successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(48.dp)
                    .testTag("logout_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset Profile & Sign Out", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun ProfileSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 13.sp)
    }
}
