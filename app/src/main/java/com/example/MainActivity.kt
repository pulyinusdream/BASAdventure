package com.example

import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.geometry.CornerRadius
import com.example.data.UserProgress
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import com.example.data.*
import com.example.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    BasaAdventureApp(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun BasaAdventureApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val progressState by viewModel.userProgress.collectAsState()

    val context = LocalContext.current
    var updateInfo by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<AppUpdateInfo?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        val info = UpdateChecker.checkForUpdates()
        if (info != null) {
            updateInfo = info
        }
    }

    if (updateInfo != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { /* Do nothing or allow dismiss */ },
            title = { androidx.compose.material3.Text("Bagong Update!") },
            text = { androidx.compose.material3.Text(updateInfo!!.releaseNotes) },
            confirmButton = {
                androidx.compose.material3.Button(onClick = {
                    UpdateChecker.openDownloadLink(context, updateInfo!!.downloadUrl)
                }) {
                    androidx.compose.material3.Text("I-update Ngayon")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { updateInfo = null }) {
                    androidx.compose.material3.Text("Mamaya na")
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Main Content Area with transition animations
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    },
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        is Screen.Landing -> LandingScreen(viewModel = viewModel)
                        is Screen.DataGathering -> DataGatheringScreen(viewModel = viewModel)
                        is Screen.Home -> HomeScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Level1Menu -> Level1MenuScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Learn -> LearnScreen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam1 -> KatinigExam1Screen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam2 -> KatinigExam2Screen(viewModel = viewModel, progress = progressState)
                        is Screen.KatinigExam3 -> KatinigExam3Screen(viewModel = viewModel, progress = progressState)
                        is Screen.Games -> GamesScreen(viewModel = viewModel)
                        is Screen.Progress -> ProgressScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Settings -> SettingsScreen(viewModel = viewModel, progress = progressState)
                        is Screen.Pantig -> PantigScreen(viewModel = viewModel)
                        is Screen.Salita -> SalitaScreen(viewModel = viewModel)
                        is Screen.Talata -> TalataScreen(viewModel = viewModel)
                        is Screen.Pangungusap -> PangungusapScreen(viewModel = viewModel)
                        is Screen.Kuwento -> KuwentoScreen(viewModel = viewModel)
                        is Screen.VoiceRecording -> VoiceRecordingScreen(viewModel = viewModel)
                    }
                }
            }

            // Bottom Navigation Bar matching the Natural Tones Design
            if (currentScreen != Screen.Landing && currentScreen != Screen.DataGathering) {
                AppBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    }
}

@Composable
fun AppBottomNavigation(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(width = 1.dp, color = CardBorderLevel),
        color = CardBgLevel
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf(
                NavigationItem("Home", Screen.Home, Icons.Default.Home),
                NavigationItem("Laro", Screen.Games, Icons.Default.Gamepad),
                NavigationItem("Bituin", Screen.Progress, Icons.Default.Star),
                NavigationItem("Settings", Screen.Settings, Icons.Default.Settings)
            )

            items.forEach { item ->
                val isSelected = currentScreen::class == item.screen::class
                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false, radius = 28.dp),
                            onClick = { onNavigate(item.screen) }
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) SecondaryPurple else Color.Transparent)
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) OnSecondaryPurple else TextDark.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) TextDark else TextDark.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val screen: Screen,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// --- HOME SCREEN IMPLEMENTATION ---
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val context = LocalContext.current
    var showAvatarDialog by remember { mutableStateOf(false) }
    var showAlphabetDialog by remember { mutableStateOf(false) }
    
    if (showAlphabetDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showAlphabetDialog = false },
            title = { Text("ALPHABETONG PILIPINO", fontWeight = FontWeight.Bold, color = PrimaryPurple) },
            text = { Text("Ipapakita dito ang arranged the Filipino Alphabet") },
            confirmButton = {
                Button(onClick = { 
                    showAlphabetDialog = false 
                    viewModel.navigateTo(Screen.Level1Menu)
                }) {
                    Text("Ipagpatuloy")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, CardBorderLevel.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📖", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "BASAdevnture",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark,
                                fontSize = 24.sp,
                                letterSpacing = (-0.5).sp
                            )
                        )
                        if (progress.firstName.isNotEmpty()) {
                            Text(
                                text = "Hi, ${progress.firstName}!",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            Text(
                                text = "Matuto ng mga letra nang masaya!",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = SubtitleGray,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                // Avatar / Settings button (Keeping avatar as it's personalized)
                IconButton(
                    onClick = { showAvatarDialog = true },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE3F2FD)) // Light blue background like the volume icon in the reference
                        .testTag("avatar_button")
                ) {
                    Text(text = progress.selectedAvatar, fontSize = 24.sp)
                }
            }
        }

        // Progress Bar & Info
        item {
            val masteredCount = progress.masteredLetters.split(",").filter { it.isNotEmpty() }.size
            val percent = (masteredCount.toFloat() / 20f).coerceIn(0f, 1f)
            
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                LinearProgressIndicator(
                    progress = { percent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = PrimaryPurple.copy(alpha = 0.6f),
                    trackColor = PrimaryPurple.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$masteredCount/20 letra natutunan · ${progress.stars} bituin",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = SubtitleGray
                    )
                )
            }
        }
        // Bento Level Cards Grid
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "ALPHABETO",
                        subtitle = "Mga Titik",
                        icon = Icons.Default.MenuBook,
                        bgColor = Color(0xFF4BA3E3), // Blue
                        onClick = { showAlphabetDialog = true },
                        modifier = Modifier.weight(1f).testTag("level_1_btn"),
                        isLocked = progress.unlockedLevel < 1
                    )
                    BentoLevelCard(
                        title = "PANTIG",
                        subtitle = "Syllables",
                        icon = Icons.Default.Extension,
                        bgColor = Color(0xFFF59D41), // Orange
                        onClick = { viewModel.navigateTo(Screen.Pantig) },
                        modifier = Modifier.weight(1f).testTag("level_2_btn"),
                        isLocked = progress.unlockedLevel < 2
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "SALITA",
                        subtitle = "Mga Salita",
                        icon = Icons.Default.Face,
                        bgColor = Color(0xFF9061F9), // Purple
                        onClick = { viewModel.navigateTo(Screen.Salita) },
                        modifier = Modifier.weight(1f).testTag("level_3_btn"),
                        isLocked = progress.unlockedLevel < 3
                    )
                    BentoLevelCard(
                        title = "TALATA",
                        subtitle = "Paragraphs",
                        icon = Icons.Default.FormatAlignLeft,
                        bgColor = Color(0xFF4BB543), // Green
                        onClick = { viewModel.navigateTo(Screen.Talata) },
                        modifier = Modifier.weight(1f).testTag("level_4_btn"),
                        isLocked = progress.unlockedLevel < 4
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "PANGUNGUSAP",
                        subtitle = "Sentences",
                        icon = Icons.Default.Subject,
                        bgColor = Color(0xFFF06292), // Pink
                        onClick = { viewModel.navigateTo(Screen.Pangungusap) },
                        modifier = Modifier.weight(1f).testTag("level_5_btn"),
                        isLocked = progress.unlockedLevel < 5
                    )
                    BentoLevelCard(
                        title = "KUWENTO",
                        subtitle = "Stories",
                        icon = Icons.Default.AutoStories,
                        bgColor = Color(0xFF26C6DA), // Cyan
                        onClick = { viewModel.navigateTo(Screen.Kuwento) },
                        modifier = Modifier.weight(1f).testTag("level_6_btn"),
                        isLocked = progress.unlockedLevel < 6
                    )
                }
            }
        }

        // Offline notice banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📲", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ligtas at Offline!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        )
                        Text(
                            text = "100% gumagana offline kahit walang internet.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFE53935))
                        )
                    }
                }
            }
        }
    }

    // Character Selection Dialog
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Pumili ng Karakter", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Piliin ang iyong makakasama sa pagbabasa:")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        viewModel.avatars.forEach { avatar ->
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(if (progress.selectedAvatar == avatar) SecondaryPurple else Color.Transparent)
                                    .border(
                                        width = if (progress.selectedAvatar == avatar) 2.dp else 1.dp,
                                        color = if (progress.selectedAvatar == avatar) PrimaryPurple else CardBorderLevel,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        viewModel.updateAvatar(avatar)
                                        showAvatarDialog = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = avatar, fontSize = 28.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Isara")
                }
            }
        )
    }
}

@Composable
fun BentoCard(
    title: String,
    subtitle: String,
    emoji: String,
    bgColor: Color,
    borderColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() }
            
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 18.sp)
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SubtitleGray,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

// --- LEARN SCREEN IMPLEMENTATION ---
@Composable
fun LearnScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val selectedIdx by viewModel.selectedLetterIdx.collectAsState()
    val isMastered by viewModel.isLetterMastered.collectAsState()
    val currentList by viewModel.currentLettersList.collectAsState()
    val currentLetter = if (selectedIdx in currentList.indices) currentList[selectedIdx] else currentList.firstOrNull() ?: viewModel.letters.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("learn_screen")
    ) {
        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "MGA LETRA",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        // Top Letter Selector row (Horizontal Scrolling circular pills matching Screenshot 2)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBgLevel)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(currentList) { idx, item ->
                val isSelected = idx == selectedIdx
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) PrimaryPurple else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) PrimaryPurple else CardBorderLevel,
                            shape = CircleShape
                        )
                        .clickable { viewModel.selectedLetterIdx.value = idx },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.char,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) Color.White else PrimaryPurple
                        )
                    )
                }
            }
        }

        // Letter Display Interactive Card
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(2.dp, PrimaryPurple),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top letter badge & Speaker sound controller
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${currentLetter.char} ${currentLetter.char.lowercase()}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp
                                )
                            )
                        }

                        IconButton(
                            onClick = { viewModel.speakLetterPhonetic(currentLetter.char) },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5252))
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Pakinggan ang letra",
                                tint = Color.White
                            )
                        }
                    }

                    // Procedural illustration Canvas (Custom vector arts)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.2f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        LetterIllustration(letterChar = currentLetter.char)
                    }

                    // Play Sound Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE8F5E9))
                            .clickable { viewModel.speakLetterSound(currentLetter) }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pakinggan ang tunog",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Pakinggan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        )
                    }
                }
            }

            // Bottom Controller Buttons (Prev, Master Toggle, Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val prev = (selectedIdx - 1 + currentList.size) % currentList.size
                        viewModel.selectedLetterIdx.value = prev
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(CardBgLevel)
                        .border(1.dp, CardBorderLevel, CircleShape)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nakaraan", tint = TextDark)
                }

                // Mastery Toggle Button matching Screenshot 2
                Button(
                    onClick = { viewModel.toggleLetterMastery(currentLetter.char) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMastered) Color(0xFF4CAF50) else PrimaryPurple
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isMastered) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = "Mastered",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isMastered) "Tapos na!" else "Markahang Tapos",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val next = (selectedIdx + 1) % currentList.size
                        viewModel.selectedLetterIdx.value = next
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(CardBgLevel)
                        .border(1.dp, CardBorderLevel, CircleShape)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Susunod", tint = TextDark)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
        }
    }
}

// --- GAMES SCREEN IMPLEMENTATION ---
@Composable
fun GamesScreen(viewModel: AppViewModel) {
    var activeGame by remember { mutableStateOf<String?>(null) }
 // null, "match", "listen", "bubble", "memory"

    if (activeGame == null) {
        // Game Menu Selector Screen (Matches Screenshot 3)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryContainerPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎮", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Mga Larong ABAKADA",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GameMenuCard(
                        title = "Match the Picture",
                        description = "Piliin ang larawan ng tamang salita",
                        emoji = "🖼️",
                        bgColor = BentoAlphabetBg,
                        borderColor = BentoAlphabetBorder,
                        onClick = {
                            viewModel.initMatchPictureGame()
                            activeGame = "match"
                        }
                    )
                    GameMenuCard(
                        title = "Listen and Choose",
                        description = "Pakinggan ang tunog at piliin ang letra",
                        emoji = "👂",
                        bgColor = BentoListenBg,
                        borderColor = BentoListenBorder,
                        onClick = {
                            viewModel.initListenAndChooseGame()
                            activeGame = "listen"
                        }
                    )
                    GameMenuCard(
                        title = "Bubble Pop",
                        description = "Sabugin ang tamang bula na may letra",
                        emoji = "🫧",
                        bgColor = BentoWritingBg,
                        borderColor = BentoWritingBorder,
                        onClick = {
                            viewModel.initBubblePopGame()
                            activeGame = "bubble"
                        }
                    )
                    GameMenuCard(
                        title = "Memory Match",
                        description = "Ipares ang letra sa kaukulang larawan",
                        emoji = "🃏",
                        bgColor = BentoRewardsBg,
                        borderColor = BentoRewardsBorder,
                        onClick = {
                            viewModel.initMemoryGame()
                            activeGame = "memory"
                        }
                    )
                }
            }
        }
    } else {
        // Back to menu bar inside active games
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBgLevel)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { activeGame = null }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Isara ang Laro", tint = TextDark)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (activeGame) {
                        "match" -> "Match the Picture"
                        "listen" -> "Listen and Choose"
                        "bubble" -> "Bubble Pop"
                        "memory" -> "Memory Match"
                        else -> "Laro"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeGame) {
                    "match" -> PlayMatchPicture(viewModel)
                    "listen" -> PlayListenAndChoose(viewModel)
                    "bubble" -> PlayBubblePop(viewModel)
                    "memory" -> PlayMemoryGame(viewModel)
                         }
        }
    }

}
}
@Composable
fun GameMenuCard(
    title: String,
    description: String,
    emoji: String,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray)
                )
            }
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "I-play", tint = TextDark)
        }
    }
}

// --- ACTIVE GAME 1: MATCH PICTURE ---
@Composable
fun PlayMatchPicture(viewModel: AppViewModel) {
    val target by viewModel.matchQuestion.collectAsState()
    val options by viewModel.matchOptions.collectAsState()
    val feedback by viewModel.matchFeedback.collectAsState()
    val isCorrect by viewModel.isMatchCorrect.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Aling larawan ang nagsisimula sa letrang \"${target?.char ?: ""}\"?",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark),
            textAlign = TextAlign.Center
        )

        // Options Grid (4 options)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(options) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { viewModel.checkMatchAnswer(item) },
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (isCorrect == true && item.char == target?.char) Color(0xFF4CAF50) else CardBorderLevel
                    ),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF9F9F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            LetterIllustration(letterChar = item.char)
                        }
                        Text(
                            text = item.word,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Feedback overlay
        feedback?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = if (isCorrect == true) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                )
            )

            if (isCorrect == true) {
                Button(
                    onClick = { viewModel.initMatchPictureGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("SUSUNOD NA TANONG")
                }
            }
        }
    }
}

// --- ACTIVE GAME 2: LISTEN AND CHOOSE ---
@Composable
fun PlayListenAndChoose(viewModel: AppViewModel) {
    val target by viewModel.listenTargetLetter.collectAsState()
    val options by viewModel.listenOptions.collectAsState()
    val feedback by viewModel.listenFeedback.collectAsState()
    val isCorrect by viewModel.isListenCorrect.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Pakinggan ang tunog at piliin ang tamang letra!",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        // Repeat speaker button
        Button(
            onClick = { viewModel.playListenTargetSound() },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = CircleShape,
            modifier = Modifier.size(90.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Ulitin ang tunog",
                tint = Color.White,
                modifier = Modifier.size(44.dp)
            )
        }

        // Letter circular bubble selections
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEach { item ->
                val letterSelected = isCorrect == true && item.char == target?.char
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .background(if (letterSelected) Color(0xFFE8F5E9) else Color.White)
                        .border(
                            width = 2.dp,
                            color = if (letterSelected) Color(0xFF4CAF50) else PrimaryPurple,
                            shape = CircleShape
                        )
                        .clickable { viewModel.checkListenAnswer(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.char,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = if (letterSelected) Color(0xFF4CAF50) else PrimaryPurple
                        )
                    )
                }
            }
        }

        // Feedback
        feedback?.let { msg ->
            Text(
                text = msg,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = if (isCorrect == true) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                )
            )

            if (isCorrect == true) {
                Button(
                    onClick = { viewModel.initListenAndChooseGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("SUSUNOD NA TANONG")
                }
            }
        }
    }
}

// --- ACTIVE GAME 3: BUBBLE POP ---
@Composable
fun PlayBubblePop(viewModel: AppViewModel) {
    val targetLetter by viewModel.bubbleTargetChar.collectAsState()
    val score by viewModel.bubbleScore.collectAsState()
    val isWon by viewModel.isBubbleGameWon.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Score bar & Target instructions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "TARGET: Letrang \"$targetLetter\"",
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2E7D32)
                )
            }

            Text(
                text = "Puntos: $score / ${viewModel.bubbleTargetCount}",
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isWon) {
            // Interactive bubble playing canvas box area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE1F5FE))
                    .border(1.dp, Color(0xFFB3E5FC), RoundedCornerShape(24.dp))
            ) {
                viewModel.activeBubbles.forEach { bubble ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bubble.size.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(
                                    x = (bubble.xOffset * 280).dp,
                                    y = (bubble.yOffset * (-480)).dp
                                )
                                .size(bubble.size.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.White,
                                            Color(0xBB80DEEA),
                                            Color(0x3300838F)
                                        )
                                    )
                                )
                                .border(1.dp, Color(0xFF00ACC1), CircleShape)
                                .clickable { viewModel.popBubble(bubble) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = bubble.char,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF006064)
                                )
                            )                }
            }
        }
    }
} else {
            // Game Won View
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉", fontSize = 72.sp)
                Text(
                    text = "Napakagaling mo!",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black, color = Color(0xFF2E7D32))
                )
                Text(
                    text = "Nakuha mo ang lahat ng bula!",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { viewModel.initBubblePopGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("MAGLARO MULI")
                }
            }
        }
    }
}

// --- ACTIVE GAME 4: MEMORY MATCH ---
@Composable
fun PlayMemoryGame(viewModel: AppViewModel) {
    val moves by viewModel.memoryMatchMoves.collectAsState()
    val isWon by viewModel.isMemoryGameWon.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Mga galaw: $moves",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SubtitleGray)
        )

        if (!isWon) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(viewModel.memoryCards) { idx, card ->
                    val showContent = card.isFaceUp || card.isMatched
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clickable { viewModel.selectMemoryCard(idx) },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = 2.dp,
                            color = if (card.isMatched) Color(0xFF4CAF50) else PrimaryPurple
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (showContent) Color.White else PrimaryPurple
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (showContent) {
                                if (card.isImage) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(modifier = Modifier.size(54.dp)) {
                                            LetterIllustration(letterChar = card.matchId)
                                        }
                                        Text(text = card.content, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                    }
                                } else {
                                    Text(
                                        text = card.content,
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Black,
                                            color = PrimaryPurple
                                        )
                                    )
                                }
                            } else {
                                Text(
                                    text = "❓",
                                    fontSize = 28.sp,
                                    color = Color.White
                                )
                            }
                }
            }
        }
    }
} else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🏆", fontSize = 72.sp)
                Text(
                    text = "Tagumpay!",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black, color = Color(0xFF4CAF50))
                )
                Text(
                    text = "Nahanap mo ang lahat ng magkapareha sa $moves na galaw!",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { viewModel.initMemoryGame() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("MAGLARO MULI")
                }
            }
        }
    }
}

// --- PROGRESS SCREEN IMPLEMENTATION ---
@Composable
fun ProgressScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    val masteredLetters = progress.masteredLetters.split(",").filter { it.isNotEmpty() }
    val percent = (masteredLetters.size.toFloat() / 20f).coerceIn(0f, 1f)
    val gameRecords by viewModel.gameRecords.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("progress_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Star Scoreboard Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryContainerPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⭐️", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Aking Gantimpala",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(BentoListenBg)
                        .border(1.dp, BentoListenBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "⭐ ${progress.stars}", fontWeight = FontWeight.Black, color = BentoListenText)
                }
            }
        }

        item {
            // Student Information
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderLevel),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Impormasyon ng Estudyante",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Pangalan: ${progress.firstName} ${progress.middleInitial} ${progress.surname}")
                    Text(text = "Baitang: ${progress.gradeLevel}")
                    Text(text = "Kasarian: ${progress.gender}")
                }
            }
        }

        item {
            // Circular Level Percentage Dial
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBgLevel),
                border = BorderStroke(1.dp, CardBorderLevel),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Kabuuang Antas ng Pagkatuto",
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(130.dp)) {
                            drawArc(
                                color = Color.White,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx())
                            )
                            drawArc(
                                color = PrimaryPurple,
                                startAngle = -90f,
                                sweepAngle = percent * 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx())
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(percent * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = PrimaryPurple)
                            )
                            Text(
                                text = "${masteredLetters.size} / 20 Titik",
                                style = MaterialTheme.typography.labelSmall.copy(color = SubtitleGray)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            Text(
                text = "Talaan ng Laro (Game Records)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        items(gameRecords) { record ->
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = record.gameName, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(text = "Puntos: ${record.score} / ${record.totalItems}", style = MaterialTheme.typography.bodySmall)
                    }
                    Text(text = "+${record.starsEarned} ⭐", color = BentoRewardsText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- SETTINGS SCREEN IMPLEMENTATION (With Parents-lock math popup!) ---
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    progress: UserProgress
) {
    var showResetLock by remember { mutableStateOf(false) }
    var parentsAnswer by remember { mutableStateOf("") }
    var mathChallenge by remember { mutableStateOf(Pair(0, 0)) }
    var lockErrorMsg by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryContainerPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = PrimaryPurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Mga Pag-aayos (Settings)",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = TextDark)
                )
            }
        }

        // Sound Toggle Row
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Tunog at Pagbasa", fontWeight = FontWeight.Bold, color = TextDark)
                        Text(text = "Pakinggan ang boses at musika", style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray))
                    }
                    Switch(
                        checked = progress.soundEnabled,
                        onCheckedChange = { viewModel.toggleSound(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple, checkedTrackColor = SecondaryPurple)
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().clickable { viewModel.navigateTo(Screen.VoiceRecording) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "I-record ang Sariling Boses", fontWeight = FontWeight.Bold, color = TextDark)
                        Text(text = "Gumawa ng sariling tunog para sa mga letra", style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray))
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Pumunta", tint = PrimaryPurple)
                }
            }
        }

        // Voice speech speed selection
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Bilis ng Pagbasa (Speech Rate)", fontWeight = FontWeight.Bold, color = TextDark)
                    Text(text = "Ayusin kung gaano kabilis magbasa", style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray))
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val speeds = listOf(
                            Triple("Mabagal", 0.7f, BentoAlphabetBg),
                            Triple("Normal", 1.0f, BentoListenBg),
                            Triple("Mabilis", 1.3f, BentoWritingBg)
                        )

                        speeds.forEach { (label, value, color) ->
                            val isSelected = progress.ttsSpeed == value
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) color else Color(0xFFF1F1F1))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) PrimaryPurple else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.updateTtsSpeed(value) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TextDark else SubtitleGray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Reset progress Card (Locked for parents)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "I-reset ang Laro", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                    Text(text = "Buburahin ang lahat ng bituin at laro upang magsimulang muli.", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFC62828)))
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            mathChallenge = Pair((2..9).random(), (2..9).random())
                            parentsAnswer = ""
                            lockErrorMsg = ""
                            showResetLock = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Burahin ang Progress", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Parental Control Reset Locking Modal popup
    if (showResetLock) {
        AlertDialog(
            onDismissRequest = { showResetLock = false },
            title = { Text("Para sa Magulang Lamang", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) },
            text = {
                Column {
                    Text("Sagutin ang simpleng matematika upang makumpirma ang pag-reset ng progress:")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Magkano ang: ${mathChallenge.first} + ${mathChallenge.second}?",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = parentsAnswer,
                        onValueChange = { parentsAnswer = it },
                        placeholder = { Text("Isulat ang sagot") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (lockErrorMsg.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = lockErrorMsg, color = Color(0xFFD32F2F), style = MaterialTheme.typography.labelSmall)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val expected = mathChallenge.first + mathChallenge.second
                        val ansVal = parentsAnswer.trim().toIntOrNull()
                        if (ansVal == expected) {
                            viewModel.resetAllProgress()
                            showResetLock = false
                        } else {
                            lockErrorMsg = "Maling sagot! Subukang muli."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("I-reset Na!")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetLock = false }) {
                    Text("I-cancel")
                }
            }
        )
    }
}

// --- PROCEDURAL ANIMALS / OBJECTS CANVAS DRAWINGS (100% vector, responsive, beautiful) ---
@Composable
fun LetterIllustration(letterChar: String) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        when (letterChar) {
            "A" -> { // Aso (Dog)
                // Head
                drawCircle(color = Color(0xFF8D6E63), radius = 60.dp.toPx(), center = center)
                // Inner ears
                drawEar(center, isLeft = true, Color(0xFF5D4037))
                drawEar(center, isLeft = false, Color(0xFF5D4037))
                // Eyes
                drawCircle(color = Color.White, radius = 10.dp.toPx(), center = Offset(center.x - 20.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.White, radius = 10.dp.toPx(), center = Offset(center.x + 20.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 15.dp.toPx()))
                // Snout
                drawCircle(color = Color(0xFFD7CCC8), radius = 22.dp.toPx(), center = Offset(center.x, center.y + 15.dp.toPx()))
                // Nose
                drawCircle(color = Color.Black, radius = 8.dp.toPx(), center = Offset(center.x, center.y + 10.dp.toPx()))
                // Tongue
                drawArc(
                    color = Color(0xFFFF5252),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    size = Size(20.dp.toPx(), 20.dp.toPx()),
                    topLeft = Offset(center.x - 10.dp.toPx(), center.y + 18.dp.toPx())
                )
            }
            "B" -> { // Baka (Cow)
                // Head
                drawRoundRect(
                    color = Color(0xFFEEEEEE),
                    topLeft = Offset(center.x - 50.dp.toPx(), center.y - 50.dp.toPx()),
                    size = Size(100.dp.toPx(), 90.dp.toPx()),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
                // Black Spots
                drawCircle(color = Color.Black, radius = 12.dp.toPx(), center = Offset(center.x - 35.dp.toPx(), center.y - 30.dp.toPx()))
                drawCircle(color = Color.Black, radius = 8.dp.toPx(), center = Offset(center.x + 30.dp.toPx(), center.y + 10.dp.toPx()))
                // Snout
                drawRoundRect(
                    color = Color(0xFFFFCDD2),
                    topLeft = Offset(center.x - 40.dp.toPx(), center.y + 10.dp.toPx()),
                    size = Size(80.dp.toPx(), 35.dp.toPx()),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
                // Nostrils
                drawCircle(color = Color(0xFFC2185B), radius = 4.dp.toPx(), center = Offset(center.x - 15.dp.toPx(), center.y + 26.dp.toPx()))
                drawCircle(color = Color(0xFFC2185B), radius = 4.dp.toPx(), center = Offset(center.x + 15.dp.toPx(), center.y + 26.dp.toPx()))
                // Eyes
                drawCircle(color = Color.Black, radius = 6.dp.toPx(), center = Offset(center.x - 20.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.Black, radius = 6.dp.toPx(), center = Offset(center.x + 20.dp.toPx(), center.y - 15.dp.toPx()))
                // Horns
                drawCowHorns(center)
            }
            "K" -> { // Kalabaw (Carabao / Water Buffalo)
                // Dark gray face
                drawCircle(color = Color(0xFF424242), radius = 55.dp.toPx(), center = center)
                // Huge horns
                drawCarabaoHorns(center)
                // Snout
                drawRoundRect(
                    color = Color(0xFF303030),
                    topLeft = Offset(center.x - 30.dp.toPx(), center.y + 10.dp.toPx()),
                    size = Size(60.dp.toPx(), 30.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
                // Eyes
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 12.dp.toPx()))
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 12.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x - 16.dp.toPx(), center.y - 12.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x + 16.dp.toPx(), center.y - 12.dp.toPx()))
            }
            "D" -> { // Daga (Mouse)
                // Ears
                drawCircle(color = Color(0xFF9E9E9E), radius = 30.dp.toPx(), center = Offset(center.x - 45.dp.toPx(), center.y - 35.dp.toPx()))
                drawCircle(color = Color(0xFFFFCDD2), radius = 18.dp.toPx(), center = Offset(center.x - 45.dp.toPx(), center.y - 35.dp.toPx()))
                drawCircle(color = Color(0xFF9E9E9E), radius = 30.dp.toPx(), center = Offset(center.x + 45.dp.toPx(), center.y - 35.dp.toPx()))
                drawCircle(color = Color(0xFFFFCDD2), radius = 18.dp.toPx(), center = Offset(center.x + 45.dp.toPx(), center.y - 35.dp.toPx()))
                // Face
                drawCircle(color = Color(0xFFE0E0E0), radius = 45.dp.toPx(), center = center)
                // Eyes
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x - 16.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x + 16.dp.toPx(), center.y - 10.dp.toPx()))
                // Nose
                drawCircle(color = Color(0xFFE91E63), radius = 8.dp.toPx(), center = Offset(center.x, center.y + 10.dp.toPx()))
                // Whiskers
                drawLine(color = Color.DarkGray, start = Offset(center.x - 15.dp.toPx(), center.y + 12.dp.toPx()), end = Offset(center.x - 55.dp.toPx(), center.y + 5.dp.toPx()), strokeWidth = 2.dp.toPx())
                drawLine(color = Color.DarkGray, start = Offset(center.x - 15.dp.toPx(), center.y + 15.dp.toPx()), end = Offset(center.x - 52.dp.toPx(), center.y + 20.dp.toPx()), strokeWidth = 2.dp.toPx())
                drawLine(color = Color.DarkGray, start = Offset(center.x + 15.dp.toPx(), center.y + 12.dp.toPx()), end = Offset(center.x + 55.dp.toPx(), center.y + 5.dp.toPx()), strokeWidth = 2.dp.toPx())
                drawLine(color = Color.DarkGray, start = Offset(center.x + 15.dp.toPx(), center.y + 15.dp.toPx()), end = Offset(center.x + 52.dp.toPx(), center.y + 20.dp.toPx()), strokeWidth = 2.dp.toPx())
            }
            "E" -> { // Elepante (Elephant)
                // Main head and large ears
                drawCircle(color = Color(0xFF90A4AE), radius = 70.dp.toPx(), center = center)
                drawCircle(color = Color(0xFF78909C), radius = 45.dp.toPx(), center = Offset(center.x - 70.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color(0xFF78909C), radius = 45.dp.toPx(), center = Offset(center.x + 70.dp.toPx(), center.y - 10.dp.toPx()))
                // Eyes
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x - 22.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x + 22.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x - 20.dp.toPx(), center.y - 15.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x + 20.dp.toPx(), center.y - 15.dp.toPx()))
                // Trunk
                drawRoundRect(
                    color = Color(0xFF90A4AE),
                    topLeft = Offset(center.x - 12.dp.toPx(), center.y),
                    size = Size(24.dp.toPx(), 65.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            }
            "G" -> { // Gatas (Milk Bottle)
                // Bottle outline
                drawRoundRect(
                    color = Color(0xFFECEFF1),
                    topLeft = Offset(center.x - 30.dp.toPx(), center.y - 65.dp.toPx()),
                    size = Size(60.dp.toPx(), 130.dp.toPx()),
                    cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx())
                )
                // Blue Label
                drawRect(
                    color = Color(0xFF29B6F6),
                    topLeft = Offset(center.x - 30.dp.toPx(), center.y - 15.dp.toPx()),
                    size = Size(60.dp.toPx(), 45.dp.toPx())
                )
                // Label star
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = center)
                // Bottle Cap
                drawRect(
                    color = Color(0xFFD32F2F),
                    topLeft = Offset(center.x - 18.dp.toPx(), center.y - 75.dp.toPx()),
                    size = Size(36.dp.toPx(), 12.dp.toPx())
                )
            }
            "H" -> { // Halaman (Plant in Pot)
                // Pot
                val path = Path().apply {
                    moveTo(center.x - 30.dp.toPx(), center.y + 10.dp.toPx())
                    lineTo(center.x + 30.dp.toPx(), center.y + 10.dp.toPx())
                    lineTo(center.x + 20.dp.toPx(), center.y + 60.dp.toPx())
                    lineTo(center.x - 20.dp.toPx(), center.y + 60.dp.toPx())
                    close()
                }
                drawPath(path = path, color = Color(0xFFD84315))
                // Stem
                drawRoundRect(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(center.x - 4.dp.toPx(), center.y - 55.dp.toPx()),
                    size = Size(8.dp.toPx(), 70.dp.toPx()),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                // Leaves
                drawOval(
                    color = Color(0xFF81C784),
                    topLeft = Offset(center.x - 40.dp.toPx(), center.y - 45.dp.toPx()),
                    size = Size(38.dp.toPx(), 22.dp.toPx())
                )
                drawOval(
                    color = Color(0xFF81C784),
                    topLeft = Offset(center.x + 2.dp.toPx(), center.y - 35.dp.toPx()),
                    size = Size(38.dp.toPx(), 22.dp.toPx())
                )
            }
            "I" -> { // Isda (Fish)
                // Tail fin
                val tailPath = Path().apply {
                    moveTo(center.x + 30.dp.toPx(), center.y)
                    lineTo(center.x + 65.dp.toPx(), center.y - 30.dp.toPx())
                    lineTo(center.x + 65.dp.toPx(), center.y + 30.dp.toPx())
                    close()
                }
                drawPath(path = tailPath, color = Color(0xFFFF7043))
                // Oval fish body
                drawOval(
                    color = Color(0xFFFF5722),
                    topLeft = Offset(center.x - 55.dp.toPx(), center.y - 35.dp.toPx()),
                    size = Size(95.dp.toPx(), 70.dp.toPx())
                )
                // Eye
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x - 30.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x - 32.dp.toPx(), center.y - 10.dp.toPx()))
                // Tiny water bubbles
                drawCircle(color = Color(0xFF00B0FF).copy(alpha = 0.5f), radius = 6.dp.toPx(), center = Offset(center.x - 70.dp.toPx(), center.y - 25.dp.toPx()))
                drawCircle(color = Color(0xFF00B0FF).copy(alpha = 0.4f), radius = 4.dp.toPx(), center = Offset(center.x - 80.dp.toPx(), center.y - 10.dp.toPx()))
            }
            "L" -> { // Laruan (Toy bear)
                drawCircle(color = Color(0xFFBCAAA4), radius = 50.dp.toPx(), center = center)
                // Ears
                drawCircle(color = Color(0xFF8D6E63), radius = 20.dp.toPx(), center = Offset(center.x - 42.dp.toPx(), center.y - 42.dp.toPx()))
                drawCircle(color = Color(0xFF8D6E63), radius = 20.dp.toPx(), center = Offset(center.x + 42.dp.toPx(), center.y - 42.dp.toPx()))
                // Inner ears
                drawCircle(color = Color(0xFFFFCDD2), radius = 10.dp.toPx(), center = Offset(center.x - 42.dp.toPx(), center.y - 42.dp.toPx()))
                drawCircle(color = Color(0xFFFFCDD2), radius = 10.dp.toPx(), center = Offset(center.x + 42.dp.toPx(), center.y - 42.dp.toPx()))
                // Snout
                drawCircle(color = Color.White, radius = 18.dp.toPx(), center = Offset(center.x, center.y + 12.dp.toPx()))
                // Nose & Eyes
                drawCircle(color = Color.Black, radius = 6.dp.toPx(), center = Offset(center.x, center.y + 6.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 10.dp.toPx()))
            }
            "M" -> { // Manok (Chicken)
                // Chicken body
                drawCircle(color = Color(0xFFFFF176), radius = 55.dp.toPx(), center = center)
                // Comb (top)
                drawCircle(color = Color(0xFFE53935), radius = 12.dp.toPx(), center = Offset(center.x, center.y - 60.dp.toPx()))
                drawCircle(color = Color(0xFFE53935), radius = 10.dp.toPx(), center = Offset(center.x - 15.dp.toPx(), center.y - 55.dp.toPx()))
                drawCircle(color = Color(0xFFE53935), radius = 10.dp.toPx(), center = Offset(center.x + 15.dp.toPx(), center.y - 55.dp.toPx()))
                // Eyes
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 8.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 8.dp.toPx()))
                // Beak (yellow triangle)
                val beakPath = Path().apply {
                    moveTo(center.x - 12.dp.toPx(), center.y + 4.dp.toPx())
                    lineTo(center.x + 12.dp.toPx(), center.y + 4.dp.toPx())
                    lineTo(center.x, center.y + 24.dp.toPx())
                    close()
                }
                drawPath(path = beakPath, color = Color(0xFFFB8C00))
            }
            "N" -> { // Niyog (Coconut)
                // Brown outer husk circle
                drawCircle(color = Color(0xFF6D4C41), radius = 55.dp.toPx(), center = center)
                // White inner meat
                drawCircle(color = Color.White, radius = 45.dp.toPx(), center = Offset(center.x + 5.dp.toPx(), center.y))
                // Water hollow (light blue center)
                drawCircle(color = Color(0xFFE0F7FA), radius = 35.dp.toPx(), center = Offset(center.x + 8.dp.toPx(), center.y))
            }
            "Ng" -> { // Ngipin (Tooth)
                val toothPath = Path().apply {
                    moveTo(center.x - 30.dp.toPx(), center.y - 40.dp.toPx())
                    quadraticTo(center.x, center.y - 50.dp.toPx(), center.x + 30.dp.toPx(), center.y - 40.dp.toPx())
                    lineTo(center.x + 30.dp.toPx(), center.y + 15.dp.toPx())
                    quadraticTo(center.x + 20.dp.toPx(), center.y + 55.dp.toPx(), center.x + 12.dp.toPx(), center.y + 50.dp.toPx())
                    quadraticTo(center.x, center.y + 15.dp.toPx(), center.x - 12.dp.toPx(), center.y + 50.dp.toPx())
                    quadraticTo(center.x - 20.dp.toPx(), center.y + 55.dp.toPx(), center.x - 30.dp.toPx(), center.y + 15.dp.toPx())
                    close()
                }
                drawPath(path = toothPath, color = Color(0xFFECEFF1))
                // Face eyes & blush smile
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x - 12.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 4.dp.toPx(), center = Offset(center.x + 12.dp.toPx(), center.y - 10.dp.toPx()))
                drawArc(
                    color = Color(0xFFFF4081),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    size = Size(14.dp.toPx(), 14.dp.toPx()),
                    topLeft = Offset(center.x - 7.dp.toPx(), center.y - 2.dp.toPx())
                )
            }
            "O" -> { // Orasan (Clock)
                // Outer ring
                drawCircle(color = Color(0xFF43A047), radius = 60.dp.toPx(), center = center)
                drawCircle(color = Color.White, radius = 50.dp.toPx(), center = center)
                // Hands
                drawLine(color = Color.Black, start = center, end = Offset(center.x, center.y - 35.dp.toPx()), strokeWidth = 4.dp.toPx())
                drawLine(color = Color.Red, start = center, end = Offset(center.x + 25.dp.toPx(), center.y + 15.dp.toPx()), strokeWidth = 3.dp.toPx())
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = center)
            }
            "P" -> { // Pusa (Cat)
                drawCircle(color = Color(0xFFFFB74D), radius = 55.dp.toPx(), center = center)
                // Ears
                val leftEar = Path().apply {
                    moveTo(center.x - 50.dp.toPx(), center.y - 20.dp.toPx())
                    lineTo(center.x - 52.dp.toPx(), center.y - 65.dp.toPx())
                    lineTo(center.x - 15.dp.toPx(), center.y - 45.dp.toPx())
                    close()
                }
                drawPath(path = leftEar, color = Color(0xFFE65100))
                val rightEar = Path().apply {
                    moveTo(center.x + 50.dp.toPx(), center.y - 20.dp.toPx())
                    lineTo(center.x + 52.dp.toPx(), center.y - 65.dp.toPx())
                    lineTo(center.x + 15.dp.toPx(), center.y - 45.dp.toPx())
                    close()
                }
                drawPath(path = rightEar, color = Color(0xFFE65100))
                // Eyes
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.White, radius = 8.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 10.dp.toPx()))
                drawCircle(color = Color.Black, radius = 5.dp.toPx(), center = Offset(center.x + 18.dp.toPx(), center.y - 10.dp.toPx()))
                // Nose & whiskers
                drawCircle(color = Color(0xFFFF4081), radius = 5.dp.toPx(), center = Offset(center.x, center.y + 6.dp.toPx()))
            }
            "R" -> { // Relo (Watch)
                // Straps
                drawRect(color = Color(0xFF37474F), topLeft = Offset(center.x - 14.dp.toPx(), center.y - 65.dp.toPx()), size = Size(28.dp.toPx(), 130.dp.toPx()))
                // Dial
                drawCircle(color = Color(0xFF78909C), radius = 38.dp.toPx(), center = center)
                drawCircle(color = Color.White, radius = 30.dp.toPx(), center = center)
                // Time markers
                drawCircle(color = Color(0xFFD32F2F), radius = 4.dp.toPx(), center = center)
                drawLine(color = Color.Black, start = center, end = Offset(center.x + 18.dp.toPx(), center.y - 10.dp.toPx()), strokeWidth = 3.dp.toPx())
            }
            "S" -> { // Saging (Banana)
                // Banana bunch drawn via curved lines
                drawArc(
                    color = Color(0xFFFFEB3B),
                    startAngle = 10f,
                    sweepAngle = 110f,
                    useCenter = false,
                    size = Size(90.dp.toPx(), 70.dp.toPx()),
                    topLeft = Offset(center.x - 50.dp.toPx(), center.y - 45.dp.toPx()),
                    style = Stroke(width = 16.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                )
                // Stem connector
                drawRect(color = Color(0xFF4E342E), topLeft = Offset(center.x + 30.dp.toPx(), center.y - 40.dp.toPx()), size = Size(10.dp.toPx(), 14.dp.toPx()))
            }
            "T" -> { // Tinapay (Bread Loaf)
                drawRoundRect(
                    color = Color(0xFFFFB74D),
                    topLeft = Offset(center.x - 45.dp.toPx(), center.y - 40.dp.toPx()),
                    size = Size(90.dp.toPx(), 80.dp.toPx()),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                )
                // Slices effect lines
                drawLine(color = Color(0xFFE65100), start = Offset(center.x - 20.dp.toPx(), center.y - 35.dp.toPx()), end = Offset(center.x - 20.dp.toPx(), center.y + 35.dp.toPx()), strokeWidth = 3.dp.toPx())
                drawLine(color = Color(0xFFE65100), start = Offset(center.x + 15.dp.toPx(), center.y - 35.dp.toPx()), end = Offset(center.x + 15.dp.toPx(), center.y + 35.dp.toPx()), strokeWidth = 3.dp.toPx())
            }
            "U" -> { // Ulan (Rain Cloud)
                // Rain cloud puffy circles
                drawCircle(color = Color(0xFFCFD8DC), radius = 35.dp.toPx(), center = center)
                drawCircle(color = Color(0xFFB0BEC5), radius = 28.dp.toPx(), center = Offset(center.x - 30.dp.toPx(), center.y + 5.dp.toPx()))
                drawCircle(color = Color(0xFFB0BEC5), radius = 28.dp.toPx(), center = Offset(center.x + 30.dp.toPx(), center.y + 5.dp.toPx()))
                // Droplets falling
                drawLine(color = Color(0xFF03A9F4), start = Offset(center.x - 25.dp.toPx(), center.y + 35.dp.toPx()), end = Offset(center.x - 25.dp.toPx(), center.y + 55.dp.toPx()), strokeWidth = 3.dp.toPx())
                drawLine(color = Color(0xFF03A9F4), start = Offset(center.x, center.y + 40.dp.toPx()), end = Offset(center.x, center.y + 60.dp.toPx()), strokeWidth = 3.dp.toPx())
                drawLine(color = Color(0xFF03A9F4), start = Offset(center.x + 25.dp.toPx(), center.y + 35.dp.toPx()), end = Offset(center.x + 25.dp.toPx(), center.y + 55.dp.toPx()), strokeWidth = 3.dp.toPx())
            }
            "W" -> { // Watawat (Flag)
                // Flagpole
                drawRect(color = Color(0xFF78909C), topLeft = Offset(center.x - 35.dp.toPx(), center.y - 65.dp.toPx()), size = Size(8.dp.toPx(), 130.dp.toPx()))
                // Blue top stripe, Red bottom stripe
                drawRect(color = Color(0xFF0D47A1), topLeft = Offset(center.x - 27.dp.toPx(), center.y - 55.dp.toPx()), size = Size(65.dp.toPx(), 22.dp.toPx()))
                drawRect(color = Color(0xFFB71C1C), topLeft = Offset(center.x - 27.dp.toPx(), center.y - 33.dp.toPx()), size = Size(65.dp.toPx(), 22.dp.toPx()))
                // Triangle
                val tri = Path().apply {
                    moveTo(center.x - 27.dp.toPx(), center.y - 55.dp.toPx())
                    lineTo(center.x + 5.dp.toPx(), center.y - 22.dp.toPx())
                    lineTo(center.x - 27.dp.toPx(), center.y + 11.dp.toPx())
                    close()
                }
                drawPath(path = tri, color = Color.White)
                // Yellow sun
                drawCircle(color = Color(0xFFFFEB3B), radius = 5.dp.toPx(), center = Offset(center.x - 18.dp.toPx(), center.y - 22.dp.toPx()))
            }
            "Y" -> { // Yoyo
                // Two outer rings
                drawCircle(color = Color(0xFFAB47BC), radius = 45.dp.toPx(), center = center)
                drawCircle(color = Color(0xFFE1BEE7), radius = 30.dp.toPx(), center = center)
                // String details
                drawLine(color = Color.White, start = center, end = Offset(center.x - 10.dp.toPx(), center.y - 70.dp.toPx()), strokeWidth = 3.dp.toPx())
                drawCircle(color = Color(0xFF8E24AA), radius = 8.dp.toPx(), center = center)
            }
            else -> {
                drawCircle(color = PrimaryPurple, radius = 45.dp.toPx(), center = center)
            }
        }
    }
}

// Draw Dog ear Helper
private fun DrawScope.drawEar(headCenter: Offset, isLeft: Boolean, color: Color) {
    val earX = if (isLeft) headCenter.x - 45.dp.toPx() else headCenter.x + 45.dp.toPx()
    val earY = headCenter.y - 65.dp.toPx()
    drawOval(
        color = color,
        topLeft = Offset(earX - 15.dp.toPx(), earY),
        size = Size(30.dp.toPx(), 55.dp.toPx())
    )
}

// Draw Cow Horns Helper
private fun DrawScope.drawCowHorns(headCenter: Offset) {
    val leftHorn = Path().apply {
        moveTo(headCenter.x - 40.dp.toPx(), headCenter.y - 45.dp.toPx())
        quadraticTo(headCenter.x - 60.dp.toPx(), headCenter.y - 65.dp.toPx(), headCenter.x - 45.dp.toPx(), headCenter.y - 70.dp.toPx())
        quadraticTo(headCenter.x - 30.dp.toPx(), headCenter.y - 60.dp.toPx(), headCenter.x - 30.dp.toPx(), headCenter.y - 45.dp.toPx())
        close()
    }
    drawPath(path = leftHorn, color = Color(0xFF90A4AE))

    val rightHorn = Path().apply {
        moveTo(headCenter.x + 40.dp.toPx(), headCenter.y - 45.dp.toPx())
        quadraticTo(headCenter.x + 60.dp.toPx(), headCenter.y - 65.dp.toPx(), headCenter.x + 45.dp.toPx(), headCenter.y - 70.dp.toPx())
        quadraticTo(headCenter.x + 30.dp.toPx(), headCenter.y - 60.dp.toPx(), headCenter.x + 30.dp.toPx(), headCenter.y - 45.dp.toPx())
        close()
    }
    drawPath(path = rightHorn, color = Color(0xFF90A4AE))
}

// Draw Carabao Horns Helper
private fun DrawScope.drawCarabaoHorns(headCenter: Offset) {
    val leftHorn = Path().apply {
        moveTo(headCenter.x - 45.dp.toPx(), headCenter.y - 25.dp.toPx())
        quadraticTo(headCenter.x - 90.dp.toPx(), headCenter.y - 65.dp.toPx(), headCenter.x - 65.dp.toPx(), headCenter.y - 85.dp.toPx())
        quadraticTo(headCenter.x - 80.dp.toPx(), headCenter.y - 50.dp.toPx(), headCenter.x - 35.dp.toPx(), headCenter.y - 30.dp.toPx())
        close()
    }
    drawPath(path = leftHorn, color = Color(0xFFB0BEC5))

    val rightHorn = Path().apply {
        moveTo(headCenter.x + 45.dp.toPx(), headCenter.y - 25.dp.toPx())
        quadraticTo(headCenter.x + 90.dp.toPx(), headCenter.y - 65.dp.toPx(), headCenter.x + 65.dp.toPx(), headCenter.y - 85.dp.toPx())
        quadraticTo(headCenter.x + 80.dp.toPx(), headCenter.y - 50.dp.toPx(), headCenter.x + 35.dp.toPx(), headCenter.y - 30.dp.toPx())
        close()
    }
    drawPath(path = rightHorn, color = Color(0xFFB0BEC5))
}

@Composable
fun LandingScreen(
    viewModel: AppViewModel
) {
    val context = LocalContext.current
    val progress by viewModel.userProgress.collectAsState()
    val isPlaying by viewModel.isMusicPlaying.collectAsState()

    // Breathing or pulsing animation for the "Magsimula" button
    val infiniteTransition = rememberInfiniteTransition(label = "button_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("landing_screen"),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.img_landing_bg_1783906015622),
            contentDescription = "BASAdventure Landing Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay to darken the bottom portion so the text and controls are highly readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.85f)
                        ),
                        startY = 300f
                    )
                )
        )

        // Bottom Controls Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // App Tagline Card (translucent frosted glass effect)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.25f)),
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BASAdventure",
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(2f, 4f),
                                blurRadius = 8f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Halika at tumuklas ng mga titik at salitang Pilipino sa masayang paraan!",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            // Row for the Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Background Music Toggle Button
                Button(
                    onClick = { viewModel.toggleMusic() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPlaying) PrimaryPurple else Color(0xFFE57373),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("music_toggle_button"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (isPlaying) "🎵" else "🔇",
                        fontSize = 24.sp
                    )
                }

                // Start Adventure Button
                Button(
                    onClick = {
                        viewModel.speakText("Simulan natin ang pakikipagsapalaran!")
                        if (progress.firstName.isNotEmpty() || progress.surname.isNotEmpty()) {
                            viewModel.navigateTo(Screen.Home)
                        } else {
                            viewModel.navigateTo(Screen.DataGathering)
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF81C784), // Beautiful playful green
                        contentColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .scale(scale)
                        .testTag("start_adventure_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "MAGSIMULA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Simulan",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                         }
        }
    }

}
}
@Composable
fun DataGatheringScreen(
    viewModel: AppViewModel
) {
    val progress by viewModel.userProgress.collectAsState()

    // Form states (initialized with current saved data if any)
    var gradeLevel by remember { mutableStateOf(progress.gradeLevel) }
    var surname by remember { mutableStateOf(progress.surname) }
    var firstName by remember { mutableStateOf(progress.firstName) }
    var middleInitial by remember { mutableStateOf(progress.middleInitial) }
    var gender by remember { mutableStateOf(progress.gender) }

    val isFormValid = gradeLevel.isNotEmpty() && surname.isNotBlank() && firstName.isNotBlank() && gender.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .testTag("data_gathering_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header card with back button integrated
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Landing) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, CardBorderLevel, CircleShape)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Bumalik",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Bumalik sa Landing Page",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )
            }

            // Header Card with illustration/emojis
            Card(
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, CardBorderLevel),
                colors = CardDefaults.cardColors(containerColor = CardBgLevel),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎒", fontSize = 48.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Impormasyon ng Estudyante",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Text(
                            text = "Kumpletuhin ang mga detalye sa ibaba upang makapagsimula.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = SubtitleGray
                            )
                        )
                    }
                }
            }

            // --- FIRST NAME INPUT ---
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Pangalan (First Name) *",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = { Text("Halimbawa: Paul") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("first_name_input"),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        unfocusedBorderColor = CardBorderLevel,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // --- MIDDLE INITIAL & SURNAME IN ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Middle Initial (take smaller width)
                Column(
                    modifier = Modifier.weight(0.35f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Gitnang Inisyal (M.I.)",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = middleInitial,
                        onValueChange = { if (it.length <= 2) middleInitial = it },
                        placeholder = { Text("A.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("middle_initial_input"),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLevel,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                // Surname (take larger width)
                Column(
                    modifier = Modifier.weight(0.65f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Apelyido (Surname) *",
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        placeholder = { Text("Halimbawa: Santos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("surname_input"),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLevel,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            // --- GRADE LEVEL SELECTION (Visual chips) ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Baitang (Grade Level) *",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                val gradeLevelsRow1 = listOf("Kinder", "Grade 1", "Grade 2", "Grade 3")
                val gradeLevelsRow2 = listOf("Grade 4", "Grade 5", "Grade 6")
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        gradeLevelsRow1.forEach { level ->
                            val isSelected = gradeLevel == level
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) PrimaryContainerPurple else Color.White)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PrimaryPurple else CardBorderLevel,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { gradeLevel = level }
                                    .padding(vertical = 12.dp)
                                    .testTag("grade_level_$level"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        gradeLevelsRow2.forEach { level ->
                            val isSelected = gradeLevel == level
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) PrimaryContainerPurple else Color.White)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) PrimaryPurple else CardBorderLevel,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable { gradeLevel = level }
                                    .padding(vertical = 12.dp)
                                    .testTag("grade_level_$level"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        // Add an empty weight-filler if needed to match the row above, 
                        // but weight(1f) will just expand them.
                        // For a consistent look, I'll add a spacer or just let them be wider.
                        // Let's just let them be wider for now.
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // --- GENDER SELECTION (Cards with illustrations) ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Kasarian (Gender) *",
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Male option
                    val isMaleSelected = gender == "Male"
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(
                            width = if (isMaleSelected) 2.dp else 1.dp,
                            color = if (isMaleSelected) PrimaryPurple else CardBorderLevel
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMaleSelected) BentoAlphabetBg else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { gender = "Male" }
                            .testTag("gender_male")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "👦", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Lalaki (Male)",
                                fontWeight = if (isMaleSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isMaleSelected) BentoAlphabetText else TextDark,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Female option
                    val isFemaleSelected = gender == "Female"
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(
                            width = if (isFemaleSelected) 2.dp else 1.dp,
                            color = if (isFemaleSelected) PrimaryPurple else CardBorderLevel
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFemaleSelected) BentoRewardsBg else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { gender = "Female" }
                            .testTag("gender_female")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "👧", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Babae (Female)",
                                fontWeight = if (isFemaleSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isFemaleSelected) BentoRewardsText else TextDark,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- SAVE AND START BUTTON ---
            Button(
                onClick = {
                    if (isFormValid) {
                        viewModel.saveGatheredData(
                            gradeLevel = gradeLevel,
                            surname = surname,
                            firstName = firstName,
                            middleInitial = middleInitial,
                            gender = gender
                        )
                        viewModel.navigateTo(Screen.Home)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    disabledContainerColor = CardBorderLevel.copy(alpha = 0.5f),
                    contentColor = Color.White,
                    disabledContentColor = TextDark.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_details_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "I-SAVE AT MAGSIMULA ➔",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun BentoLevelCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (isLocked) Color(0xFFE0E0E0) else bgColor),
        modifier = modifier
            .clickable(enabled = !isLocked) { onClick() }
            .fillMaxWidth()
            .aspectRatio(1.1f)
            
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = if (isLocked) 0.05f else 0.15f),
                    radius = size.width * 0.45f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.95f)
                )
            }
            
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isLocked) Color.Gray.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else icon,
                        contentDescription = null,
                        tint = if (isLocked) Color.DarkGray else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isLocked) Color.Gray else Color.White,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isLocked) "Naka-lock" else subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isLocked) Color.Gray.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PantigScreen(viewModel: AppViewModel) {
    var selectedConsonant by remember { mutableStateOf("B") }
    val vowels = listOf("A", "E", "I", "O", "U")
    val consonants = listOf("B", "K", "D", "G", "H", "L", "M", "N", "P", "R", "S", "T", "W", "Y")
    
    var practiceWordIdx by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var showSuccessEffect by remember { mutableStateOf(false) }

    val syllableWords = listOf(
        Triple("___", "sa", "basa"),
        Triple("pu", "___", "pusa"),
        Triple("___", "ta", "bata"),
        Triple("la", "___", "lapis"),
        Triple("ma", "___", "manok")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .testTag("pantig_screen")
    ) {
        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFB74D))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PANTIG (SYLLABLES)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, CardBorderLevel, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Bumalik",
                    tint = TextDark
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Antas 2: Mga Pantig",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            )
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, CardBorderLevel),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🦜", fontSize = 40.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Alamin ang Tunog ng mga Pantig!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                    Text(
                        text = "I-tap ang mga consonant at syllable upang pakinggan ang bigkas nito.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SubtitleGray)
                    )
                }
            }
        }

        Text(
            text = "Pumili ng Katinig (Consonant):",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            consonants.forEach { consonant ->
                val isSelected = selectedConsonant == consonant
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PrimaryPurple else Color.White)
                        .border(1.dp, if (isSelected) PrimaryPurple else CardBorderLevel, RoundedCornerShape(12.dp))
                        .clickable { selectedConsonant = consonant }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = consonant,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else TextDark,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, PrimaryPurple),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Pamilyang PANTIG ng [ $selectedConsonant ]",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryPurple)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    vowels.forEach { vowel ->
                        val syllable = "${selectedConsonant.lowercase()}$vowel"
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.speakText(syllable)
                                }
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryPurple)
                                    .border(2.dp, PrimaryPurple, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = syllable.uppercase(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 20.sp,
                                    color = OnPrimaryContainerPurple
                                )
                            }
                            Text(
                                text = "🔊 Pakinggan",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SubtitleGray
                                )
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorderLevel))

        Text(
            text = "🧩 Pagsasanay: Kumpletuhin ang Salita",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, Color(0xFF1D1B20)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val currentWord = syllableWords[practiceWordIdx]
                val formattedWord = when {
                    currentWord.first == "___" -> "___ - ${currentWord.second}"
                    currentWord.second == "___" -> "${currentWord.first} - ___"
                    else -> ""
                }
                
                val clueEmoji = when (currentWord.third) {
                    "basa" -> "📖"
                    "pusa" -> "🐱"
                    "bata" -> "👶"
                    "lapis" -> "✏️"
                    "manok" -> "🐔"
                    else -> "❔"
                }

                Text(text = clueEmoji, fontSize = 64.sp)

                Text(
                    text = formattedWord.uppercase(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = PrimaryPurple,
                        letterSpacing = 2.sp
                    )
                )

                Text(
                    text = "I-tap ang tamang pantig upang mabuo ang salita:",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubtitleGray),
                    textAlign = TextAlign.Center
                )

                val options = when (currentWord.third) {
                    "basa" -> listOf("ba", "ka", "ta")
                    "pusa" -> listOf("ko", "sa", "ma")
                    "bata" -> listOf("ba", "pa", "sa")
                    "lapis" -> listOf("pis", "tis", "lis")
                    "manok" -> listOf("nok", "dok", "lok")
                    else -> listOf("ba", "be", "bi")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    options.forEach { option ->
                        val isChosen = selectedOption == option
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isChosen) PrimaryContainerPurple else CardBgLevel)
                                .border(
                                    width = if (isChosen) 2.dp else 1.dp,
                                    color = if (isChosen) PrimaryPurple else CardBorderLevel,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    selectedOption = option
                                    val isCorrect = (currentWord.first == "___" && option == "ba") || 
                                                    (currentWord.first == "pu" && option == "sa") ||
                                                    (currentWord.first == "___" && option == "ba") ||
                                                    (currentWord.first == "la" && option == "pis") ||
                                                    (currentWord.first == "ma" && option == "nok")
                                                    
                                    if (isCorrect) {
                                        feedbackMessage = "Magaling! Ang tamang salita ay ${currentWord.third.uppercase()}!"
                                        viewModel.speakText("Magaling! ${currentWord.third}")
                                        viewModel.addStars(2)
                                        showSuccessEffect = true
                                    } else {
                                        feedbackMessage = "Subukan muli!"
                                        viewModel.speakText("Subukan muli")
                                        showSuccessEffect = false
                                    }
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option.uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryContainerPurple,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                if (feedbackMessage.isNotEmpty()) {
                    Text(
                        text = feedbackMessage,
                        fontWeight = FontWeight.Bold,
                        color = if (showSuccessEffect) Color(0xFF2E7D32) else Color(0xFFC62828),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Button(
                    onClick = {
                        val nextIdx = practiceWordIdx + 1
                        if (nextIdx < syllableWords.size) {
                            practiceWordIdx = nextIdx
                            selectedOption = ""
                            feedbackMessage = ""
                            showSuccessEffect = false
                        } else {
                            viewModel.unlockLevel(3)
                            viewModel.speakText("Natapos ang Pagsasanay! Bukas na ang Level 3.")
                            viewModel.navigateTo(Screen.Home)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text(if (practiceWordIdx + 1 < syllableWords.size) "Susunod na Tanong ➔" else "Tapusin ang Pagsasanay ➔", fontWeight = FontWeight.Bold)
                         }
        }
    }

}
}
}
@Composable
fun SalitaScreen(viewModel: AppViewModel) {
    val categories = listOf("Mga Hayop 🐶", "Mga Pagkain 🍌", "Mga Bagay ✏️")
    var selectedCategory by remember { mutableStateOf("Mga Hayop 🐶") }

    val wordsMap = mapOf(
        "Mga Hayop 🐶" to listOf(
            Pair("Aso", "🐶"),
            Pair("Pusa", "🐱"),
            Pair("Ibon", "🐦"),
            Pair("Manok", "🐔"),
            Pair("Isda", "🐟")
        ),
        "Mga Pagkain 🍌" to listOf(
            Pair("Saging", "🍌"),
            Pair("Mansanas", "🍎"),
            Pair("Tinapay", "🍞"),
            Pair("Gatas", "🥛"),
            Pair("Itlog", "🥚")
        ),
        "Mga Bagay ✏️" to listOf(
            Pair("Lapis", "✏️"),
            Pair("Papel", "📄"),
            Pair("Laruan", "🧸"),
            Pair("Sapatos", "👟"),
            Pair("Bag", "🎒")
        )
    )

    val spellingWords = listOf(
        Pair("PUSA", listOf("A", "U", "P", "S")),
        Pair("ASO", listOf("S", "O", "A")),
        Pair("ISDA", listOf("D", "I", "S", "A")),
        Pair("LAPIS", listOf("I", "P", "L", "A", "S")),
        Pair("GATAS", listOf("T", "A", "G", "A", "S"))
    )
    var currentSpellingIdx by remember { mutableStateOf(0) }
    var currentAssembledWord by remember { mutableStateOf("") }
    var spellingFeedbackMessage by remember { mutableStateOf("") }
    var spellingSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .testTag("salita_screen")
    ) {
        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCE93D8))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SALITA (WORDS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, CardBorderLevel, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Bumalik",
                    tint = TextDark
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Antas 3: Mga Salita",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) PrimaryContainerPurple else Color.White)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) PrimaryPurple else CardBorderLevel,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnPrimaryContainerPurple else TextDark,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Text(
            text = "Mag-aral ng Salita (Tap to listen):",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            wordsMap[selectedCategory]?.forEach { wordPair ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.speakText(wordPair.first)
                        },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, CardBorderLevel),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = wordPair.second, fontSize = 40.sp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = wordPair.first.uppercase(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = TextDark
                                )
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔊", fontSize = 24.sp)
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorderLevel))

        Text(
            text = "✏️ Pagsasanay: Baybayin Natin (Spelling Game)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, Color(0xFF1D1B20)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val currentPair = spellingWords[currentSpellingIdx]
                val targetWord = currentPair.first
                val lettersList = currentPair.second

                val emojiClue = when (targetWord) {
                    "PUSA" -> "🐱"
                    "ASO" -> "🐶"
                    "ISDA" -> "🐟"
                    "LAPIS" -> "✏️"
                    "GATAS" -> "🥛"
                    else -> "🍎"
                }

                Text(text = emojiClue, fontSize = 64.sp)

                Text(
                    text = currentAssembledWord.ifEmpty { "____" },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = PrimaryPurple,
                        letterSpacing = 4.sp
                    )
                )

                Text(
                    text = "I-tap ang mga letra sa tamang pagkakasunod-sunod:",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SubtitleGray),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    lettersList.forEach { letter ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SecondaryPurple)
                                .border(1.dp, PrimaryPurple, RoundedCornerShape(12.dp))
                                .clickable {
                                    if (currentAssembledWord.length < targetWord.length) {
                                        currentAssembledWord += letter
                                        viewModel.speakText(letter.lowercase())
                                        
                                        if (currentAssembledWord.length == targetWord.length) {
                                            if (currentAssembledWord == targetWord) {
                                                spellingFeedbackMessage = "Kahanga-hanga! Tama ang iyong baybay!"
                                                viewModel.speakText("Kahanga hanga! ${targetWord.lowercase()}")
                                                viewModel.addStars(3)
                                                spellingSuccess = true
                                            } else {
                                                spellingFeedbackMessage = "Mali ang baybay. Subukan muli!"
                                                viewModel.speakText("Mali, subukan muli")
                                                spellingSuccess = false
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryContainerPurple,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            currentAssembledWord = ""
                            spellingFeedbackMessage = ""
                            spellingSuccess = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Burahin", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val nextIdx = currentSpellingIdx + 1
                            if (nextIdx < spellingWords.size) {
                                currentSpellingIdx = nextIdx
                                currentAssembledWord = ""
                                spellingFeedbackMessage = ""
                                spellingSuccess = false
                            } else {
                                viewModel.unlockLevel(4)
                                viewModel.speakText("Natapos ang Pagsasanay! Bukas na ang Level 4.")
                                viewModel.navigateTo(Screen.Home)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentSpellingIdx + 1 < spellingWords.size) "Susunod ➔" else "Tapusin ➔", fontWeight = FontWeight.Bold)
                    }
                }

                if (spellingFeedbackMessage.isNotEmpty()) {
                    Text(
                        text = spellingFeedbackMessage,
                        fontWeight = FontWeight.Bold,
                        color = if (spellingSuccess) Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

}
@Composable
fun TalataScreen(viewModel: AppViewModel) {
    val paragraphs = listOf(
        Triple(
            "Ang Aking Pamilya 👨‍👩‍👧‍👦",
            "Mayroon akong masayang pamilya. Si Nanay at Tatay ay laging nag-aalaga sa amin. Tumutulong kami sa mga gawaing-bahay. Kami ay nagmamahalan nang lubos.",
            listOf("Sino ang laging nag-aalaga?", "Nanay at Tatay", "Mga kaibigan", "Mga aso")
        ),
        Triple(
            "Ang Masayang Aso 🐶",
            "Mayroon kaming alagang aso na si Puti. Laging masigla at tumatakbo si Puti sa bakuran. Gusto niyang maglaro ng bola at kumain ng buto araw-araw.",
            listOf("Ano ang paboritong laruan ni Puti?", "Bola", "Kotse-kotsehan", "Kutsara")
        ),
        Triple(
            "Ang Magandang Hardin ni Lola 🌸",
            "Maraming bulaklak sa hardin ni Lola Celia. Mayroong kulay pula, dilaw, at puti. Mababango ang mga bulaklak kaya masayang mamasyal doon.",
            listOf("Sino ang may magandang hardin?", "Lola Celia", "Tito Ben", "Nanay Maria")
        )
    )

    var currentIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var feedbackMsg by remember { mutableStateOf("") }
    var answerCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .testTag("talata_screen")
    ) {
        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81C784))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TALATA (PARAGRAPHS)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                paragraphs.forEachIndexed { idx, _ ->
                    val isSelected = currentIdx == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) PrimaryPurple else Color.Transparent)
                            .clickable { currentIdx = idx }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Talata ${idx + 1}",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextDark,
                            fontSize = 14.sp
                        )
                    }
                }
            }


        val currentParagraph = paragraphs[currentIdx]

        Card(
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(2.dp, PrimaryPurple),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = currentParagraph.first,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = PrimaryPurple
                    )
                )

                Text(
                    text = currentParagraph.second,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = TextDark,
                        lineHeight = 28.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                    ),
                    textAlign = TextAlign.Justify
                )

                Button(
                    onClick = {
                        viewModel.speakText(currentParagraph.second)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📢 Basahin nang Malakas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        Text(
            text = "❓ Sagutin ang Tanong (Comprehension):",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, Color(0xFF1D1B20)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = currentParagraph.third[0],
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                )

                val answersOptions = currentParagraph.third.subList(1, currentParagraph.third.size)

                answersOptions.forEach { option ->
                    val isChosen = selectedAnswer == option
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (isChosen) 2.dp else 1.dp,
                            color = if (isChosen) PrimaryPurple else CardBorderLevel
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChosen) PrimaryContainerPurple else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedAnswer = option
                                val correctAns = answersOptions[0]
                                if (option == correctAns) {
                                    feedbackMsg = "Mahusay! Ang iyong sagot ay tama!"
                                    viewModel.speakText("Mahusay! Tama ang iyong sagot!")
                                    viewModel.addStars(4)
                                    answerCorrect = true
                                } else {
                                    feedbackMsg = "Subukan muli ang pagbasa ng talata upang mahanap ang sagot!"
                                    viewModel.speakText("Subukan muli")
                                    answerCorrect = false
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isChosen,
                                onClick = {
                                    selectedAnswer = option
                                    val correctAns = answersOptions[0]
                                    if (option == correctAns) {
                                        feedbackMsg = "Mahusay! Ang iyong sagot ay tama!"
                                        viewModel.speakText("Mahusay!")
                                        viewModel.addStars(4)
                                        answerCorrect = true
                                    } else {
                                        feedbackMsg = "Mali, subukan muli."
                                        viewModel.speakText("Subukan muli")
                                        answerCorrect = false
                                    }
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurple)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option,
                                fontWeight = FontWeight.SemiBold,
                                color = TextDark,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (feedbackMsg.isNotEmpty()) {
                    Text(
                        text = feedbackMsg,
                        fontWeight = FontWeight.Bold,
                        color = if (answerCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
                
                Button(
                    onClick = {
                        val nextIdx = currentIdx + 1
                        if (nextIdx < paragraphs.size) {
                            currentIdx = nextIdx
                            selectedAnswer = ""
                            feedbackMsg = ""
                            answerCorrect = false
                        } else {
                            viewModel.unlockLevel(5) // Unlock Pangungusap
                            viewModel.speakText("Natapos ang Pagsasanay! Bukas na ang Level 5.")
                            viewModel.navigateTo(Screen.Home)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (currentIdx + 1 < paragraphs.size) "Susunod ➔" else "Tapusin ➔", fontWeight = FontWeight.Bold)
                         }
        }
    }

}
}
}
@Composable
fun PangungusapScreen(viewModel: AppViewModel) {
    val sentences = listOf(
        Pair("Ang bata ay masayang naglalaro.", listOf("naglalaro.", "ay", "masayang", "Ang", "bata")),
        Pair("Masarap kumain ng sariwang prutas.", listOf("sariwang", "prutas.", "Masarap", "ng", "kumain")),
        Pair("Malinis ang tubig sa ilog.", listOf("ilog.", "Malinis", "sa", "tubig", "ang")),
        Pair("Mabilis tumakbo ang aking pusa.", listOf("pusa.", "Mabilis", "aking", "ang", "tumakbo")),
        Pair("Maganda ang sikat ng araw ngayon.", listOf("ngayon.", "sikat", "ng", "Maganda", "ang", "araw"))
    )

    var currentIdx by remember { mutableStateOf(0) }
    var currentScrambledWords by remember { mutableStateOf(sentences[currentIdx].second) }
    var assembledWordsList by remember { mutableStateOf(listOf<String>()) }
    var feedbackMsg by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }

    LaunchedEffect(currentIdx) {
        currentScrambledWords = sentences[currentIdx].second
        assembledWordsList = listOf()
        feedbackMsg = ""
        isCorrect = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .testTag("pangungusap_screen")
    ) {
        // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF06292))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PANGUNGUSAP",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, CardBorderLevel, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Bumalik",
                    tint = TextDark
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Antas 5: Mga Pangungusap",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            )
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, CardBorderLevel),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎈", fontSize = 40.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Ayusin ang mga Salita!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                    )
                    Text(
                        text = "I-tap ang mga salita upang mabuo ang tamang pangungusap.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = SubtitleGray)
                    )
                }
            }
        }

        val targetSentence = sentences[currentIdx].first
        Card(
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(2.dp, PrimaryPurple),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Pangungusap #${currentIdx + 1}",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = SubtitleGray)
                )

                Text(
                    text = targetSentence,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                )

                Button(
                    onClick = { viewModel.speakText(targetSentence) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("🔊 Pakinggan ang Pangungusap", fontWeight = FontWeight.Bold)
                }
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, Color(0xFF1D1B20)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Gawin ang Pangungusap Dito:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SubtitleGray)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp)
                        .background(CardBgLevel, RoundedCornerShape(16.dp))
                        .border(1.dp, CardBorderLevel, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (assembledWordsList.isEmpty()) {
                        Text(
                            text = "I-tap ang mga salita sa ibaba...",
                            color = SubtitleGray.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = assembledWordsList.joinToString(" "),
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = PrimaryPurple,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Text(
                    text = "Mga pagpipiliang salita:",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = TextDark)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val chunkedList = currentScrambledWords.chunked(3)
                    chunkedList.forEach { rowChunk ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            rowChunk.forEach { word ->
                                val isUsed = assembledWordsList.contains(word)
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isUsed) Color.LightGray.copy(alpha = 0.5f) else SecondaryPurple)
                                        .border(1.dp, if (isUsed) Color.Transparent else PrimaryPurple, RoundedCornerShape(12.dp))
                                        .clickable(enabled = !isUsed) {
                                            assembledWordsList = assembledWordsList + word
                                            viewModel.speakText(word.replace(".", "").replace(",", ""))
                                            
                                            if (assembledWordsList.size == currentScrambledWords.size) {
                                                val assembledStr = assembledWordsList.joinToString(" ")
                                                if (assembledStr == targetSentence) {
                                                    feedbackMsg = "Napakahusay! Tama ang iyong binuong pangungusap!"
                                                    viewModel.speakText("Napakahusay! Tama")
                                                    viewModel.addStars(4)
                                                    isCorrect = true
                                                } else {
                                                    feedbackMsg = "Subukan muli!"
                                                    viewModel.speakText("Subukan muli")
                                                    isCorrect = false
                                                }
                                            }
                                        }
                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = word,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUsed) TextDark.copy(alpha = 0.4f) else OnPrimaryContainerPurple,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            assembledWordsList = listOf()
                            feedbackMsg = ""
                            isCorrect = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Burahin", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val nextIdx = currentIdx + 1
                            if (nextIdx < sentences.size) {
                                currentIdx = nextIdx
                            } else {
                                viewModel.unlockLevel(6) // Unlock Kuwento
                                viewModel.speakText("Natapos ang Pagsasanay! Bukas na ang Level 6.")
                                viewModel.navigateTo(Screen.Home)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentIdx + 1 < sentences.size) "Susunod ➔" else "Tapusin ➔", fontWeight = FontWeight.Bold)
                    }
                }

                if (feedbackMsg.isNotEmpty()) {
                    Text(
                        text = feedbackMsg,
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

}
@Composable
fun KuwentoScreen(viewModel: AppViewModel) {
    val stories = listOf(
        Triple(
            "Si Pagong at si Matsing 🐢🐒",
            listOf(
                Pair("Sina Pagong at Matsing ay matalik na magkaibigan. Isang araw, nakakita sila ng lumulutang na puno ng saging sa ilog.", "🐢 🐒 🌊 🍌"),
                Pair("Hinati nila ang puno ng saging. Dahil mapanlamang si Matsing, kinuha niya ang itaas na bahagi na may dahon. Kay Pagong naman ang ibaba na may ugat.", "🪓 🍌 🍂"),
                Pair("Itinim ni Pagong ang kanyang bahagi at ito ay lumaki at nagkaroon ng bunga. Ang kay Matsing naman ay nalanta dahil walang ugat.", "🌱 🪴 🥀 🍌"),
                Pair("Sa huli, si Pagong ay nagwagi dahil sa kanyang pagiging matiyaga at matalino, samantalang si Matsing ay nagsisi sa kanyang kasakiman.", "🏆 🐢 🍎 💭")
            ),
            "Laging maging matiyaga, matapat, at huwag maging mapanglamang sa kapwa."
        ),
        Triple(
            "Ang Langgam at ang Tipaklong 🐜🦗",
            listOf(
                Pair("Si Langgam ay masipag na nag-iipon ng pagkain sa buong tag-araw para maging handa kapag dumating ang tag-ulan.", "🐜 🌾 ☀️ 📦"),
                Pair("Si Tipaklong naman ay kumakanta, sumasayaw, at naglalaro lamang, tinatawanan pa niya ang pagod ni Langgam.", "🦗 🎸 🎶 🤪"),
                Pair("Nang dumating ang malakas na bagyo, nagutom si Tipaklong dahil wala siyang naipong pagkain habang si Langgam ay busog sa bahay.", "🌧️ 🌪️ 🦗 😢"),
                Pair("Humingi si Tipaklong ng tulong at binigyan siya ni Langgam ng pagkain. Natuto si Tipaklong na maging masipag at handa sa hinaharap.", "🤝 🐜 🍪 🦗")
            ),
            "Mag-aral at magtrabaho habang may oras pa upang magkaroon ng magandang bukas."
        )
    )

    var selectedStoryIdx by remember { mutableStateOf<Int?>(null) }
    var currentPageIdx by remember { mutableStateOf(0) }
    var showMoralDialog by remember { mutableStateOf(false) }

    if (selectedStoryIdx == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .verticalScroll(rememberScrollState())
                .testTag("kuwento_list_screen")
        ) {
            // Level Title with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4FC3F7))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MGA KUWENTO",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }
            
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, CardBorderLevel, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Bumalik",
                        tint = TextDark
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Antas 6: Mga Maikling Kuwento",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                )
            }

            Text(
                text = "Pumili ng Kuwentong Babasahin:",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
            )

            stories.forEachIndexed { idx, story ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedStoryIdx = idx
                            currentPageIdx = 0
                            viewModel.speakText("Kuwento ng " + story.first.split(" ")[1])
                        },
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, if (idx == 0) Color(0xFFD5A0D2) else Color(0xFF5CB85C)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = story.first,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Leksyon: ${story.third}",
                                style = MaterialTheme.typography.bodySmall.copy(color = SubtitleGray)
                            )
                        }
                        Text(text = "📖", fontSize = 32.sp)
                    }
                }
            }
        }
    }
} else {
        val activeStory = stories[selectedStoryIdx!!]
        val pages = activeStory.second
        val pageCount = pages.size

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag("kuwento_reading_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { selectedStoryIdx = null },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, CardBorderLevel, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Bumalik sa listahan",
                        tint = TextDark
                    )
                }
                Text(
                    text = "Pahina ${currentPageIdx + 1} ng $pageCount",
                    fontWeight = FontWeight.Bold,
                    color = SubtitleGray,
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(
                    onClick = {
                        viewModel.speakText(pages[currentPageIdx].first)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainerPurple)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Magsalita",
                        tint = OnPrimaryContainerPurple
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(3.dp, Color(0xFF1D1B20)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = pages[currentPageIdx].second, fontSize = 56.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = pages[currentPageIdx].first,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = TextDark,
                            lineHeight = 28.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                        ),
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (currentPageIdx > 0) currentPageIdx--
                            },
                            enabled = currentPageIdx > 0,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Bumalik", fontWeight = FontWeight.Bold, color = TextDark)
                        }

                        if (currentPageIdx < pageCount - 1) {
                            Button(
                                onClick = { currentPageIdx++ },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Susunod", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    showMoralDialog = true
                                    viewModel.speakText("Salamat sa pagbabasa! Ang galing mo!")
                                    viewModel.addStars(5)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Tapusin 🌟", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        if (showMoralDialog) {
            AlertDialog(
                onDismissRequest = {
                    showMoralDialog = false
                    selectedStoryIdx = null
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showMoralDialog = false
                            selectedStoryIdx = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Magpatuloy sa Home", fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Text(
                        text = "🎉 Pagbati!",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Nakakuha ka ng +5 Bituin ⭐!",
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Gintong Aral: ${activeStory.third}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = SubtitleGray,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White
            )
        }
    }
}

@Composable
fun Level1MenuScreen(viewModel: AppViewModel, progress: UserProgress) {
    val isLevel2Unlocked by viewModel.isLevel2Unlocked.collectAsState()
    val isLevel3Unlocked by viewModel.isLevel3Unlocked.collectAsState()
    val isPagsasanayUnlocked by viewModel.isPagsasanayUnlocked.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mga Letra (Level 1)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("MGA URI NG LETRA", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "I. PATINIG",
                        subtitle = "A E I O U",
                        icon = Icons.Default.FontDownload,
                        bgColor = Color(0xFFF06292),
                        onClick = { 
                            viewModel.currentSubLevel.value = 1
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = false
                    )

                    BentoLevelCard(
                        title = "II. KATINIG",
                        subtitle = "Mga katinig",
                        icon = Icons.Default.SortByAlpha,
                        bgColor = Color(0xFF4BA3E3),
                        onClick = { if (isLevel2Unlocked) { 
                            viewModel.currentSubLevel.value = 2
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) } 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = !isLevel2Unlocked
                    )
                }
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "III. HIRAM NA LETRA",
                        subtitle = "C F J Q V X Z Ñ",
                        icon = Icons.Default.Public,
                        bgColor = Color(0xFF4BB543),
                        onClick = { if (isLevel3Unlocked) { 
                            viewModel.currentSubLevel.value = 3
                            viewModel.selectedLetterIdx.value = 0
                            viewModel.navigateTo(Screen.Learn) } 
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = !isLevel3Unlocked
                    )
                    
                    Spacer(modifier = Modifier.weight(1f)) // Empty placeholder
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("PAGSASANAY (MGA KATINIG)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }

            item {
                val exam1Passed by viewModel.exam1Passed.collectAsState()
                val exam2Passed by viewModel.exam2Passed.collectAsState()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BentoLevelCard(
                        title = "Pagsasanay 1",
                        subtitle = "Piliin ang Titik",
                        icon = Icons.Default.Assignment,
                        bgColor = Color(0xFF9061F9),
                        onClick = { if (isPagsasanayUnlocked) { 
                            viewModel.navigateTo(Screen.KatinigExam1) }
                        },
                        modifier = Modifier.weight(1f),
                        isLocked = !isPagsasanayUnlocked
                    )

                    if (exam1Passed) {
                        BentoLevelCard(
                            title = "Pagsasanay 2",
                            subtitle = "Itugma ang titik",
                            icon = Icons.Default.JoinInner,
                            bgColor = Color(0xFF9061F9),
                            onClick = { viewModel.navigateTo(Screen.KatinigExam2) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        BentoLevelCard(
                            title = "Pagsasanay 2",
                            subtitle = "Naka-lock",
                            icon = Icons.Default.Lock,
                            bgColor = Color.Gray,
                            onClick = { /* Locked */ },
                            modifier = Modifier.weight(1f),
                            isLocked = true
                        )
                    }
                }
            }
            
            item {
                val exam2Passed by viewModel.exam2Passed.collectAsState()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (exam2Passed) {
                        BentoLevelCard(
                            title = "Pagsasanay 3",
                            subtitle = "Hanapin ang titik",
                            icon = Icons.Default.Search,
                            bgColor = Color(0xFF9061F9),
                            onClick = { viewModel.navigateTo(Screen.KatinigExam3) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        BentoLevelCard(
                            title = "Pagsasanay 3",
                            subtitle = "Naka-lock",
                            icon = Icons.Default.Lock,
                            bgColor = Color.Gray,
                            onClick = { /* Locked */ },
                            modifier = Modifier.weight(1f),
                            isLocked = true
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ConfettiEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val particles = remember {
        List(30) {
            Triple(
                (0..100).random().toFloat() / 100f, // x position %
                (0..100).random().toFloat() / 100f, // delay %
                Color(
                    (0..255).random(),
                    (0..255).random(),
                    (0..255).random()
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { (xPos, delay, color) ->
            val yPos by infiniteTransition.animateFloat(
                initialValue = -0.1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, delayMillis = (delay * 2000).toInt(), easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "yPos"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawCircle(
                            color = color,
                            radius = 10.dp.toPx(),
                            center = Offset(xPos * size.width, yPos * size.height)
                        )
                    }
            )
        }
    }
}

@Composable
fun ExamSummaryDialog(
    score: Int,
    total: Int,
    viewModel: AppViewModel,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.speakText("Mahusay! Nakakuha ka ng $score sa $total.")
        try {
            val mediaPlayer = android.media.MediaPlayer().apply {
                setDataSource("https://www.soundjay.com/button/sounds/button-3.mp3")
                setAudioAttributes(
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                prepare()
                start()
            }
        } catch (e: Exception) {
            android.util.Log.e("ExamSummary", "Failed to play sound: ${e.message}")
        }
    }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                ConfettiEffect()
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Napakahusay!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = PrimaryPurple
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Iyong Iskor:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$score / $total",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = PrimaryPurple
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Magpatuloy", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OverallSummaryDialog(
    score1: Int,
    score2: Int,
    score3: Int,
    viewModel: AppViewModel,
    onDismiss: () -> Unit
) {
    val totalScore = score1 + score2 + score3
    val totalItems = 15

    LaunchedEffect(Unit) {
        viewModel.speakText("Binabati kita! Natapos mo ang lahat ng pagsasanay. Ang iyong kabuuang iskor ay $totalScore sa $totalItems.")
        try {
            val mediaPlayer = android.media.MediaPlayer().apply {
                setDataSource("https://www.soundjay.com/misc/sounds/success-fanfare-trumpets-10.mp3")
                setAudioAttributes(
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                prepare()
                start()
            }
        } catch (e: Exception) {
            android.util.Log.e("OverallSummary", "Failed to play sound: ${e.message}")
        }
    }

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            border = BorderStroke(4.dp, PrimaryPurple)
        ) {
            Box(contentAlignment = Alignment.Center) {
                ConfettiEffect()
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LEVEL 1 MASTER!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = PrimaryPurple
                        )
                    )
                    
                    Text(
                        text = "🎉 Tapos na ang Antas 1! 🎉",
                        style = MaterialTheme.typography.titleMedium,
                        color = SubtitleGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Detailed scores
                    Column(
                        modifier = Modifier
                            .background(CardBgLevel, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        SummaryRow("Pagsasanay 1", "$score1/5")
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorderLevel)
                        SummaryRow("Pagsasanay 2", "$score2/5")
                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorderLevel)
                        SummaryRow("Pagsasanay 3", "$score3/5")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "KABUUANG ISKOR",
                        style = MaterialTheme.typography.labelLarge,
                        color = SubtitleGray
                    )
                    Text(
                        text = "$totalScore / $totalItems",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2E7D32)
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("BUKSAN ANG LEVEL 2 (PANTIG)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, score: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = score, fontWeight = FontWeight.Bold, color = PrimaryPurple)
    }
}

@Composable
fun DetailStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Black, fontSize = 24.sp, color = PrimaryPurple)
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun RecordItem(title: String, subtitle: String, trailing: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SubtitleGray)
            }
            Text(trailing, fontWeight = FontWeight.Black, color = color)
        }
    }
}

@Composable
fun KatinigExam1Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    var showSummary by remember { mutableStateOf(false) }
    
    var timeLeft by remember { mutableStateOf(60) }
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && !showSummary) {
            kotlinx.coroutines.delay(1000)
            timeLeft--
        }
        if (timeLeft <= 0 && !showSummary) {
            showSummary = true
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect != null) {
            if (isCorrect == true) score++
            kotlinx.coroutines.delay(1500)
            if (currentQuestionIdx >= 4) {
                showSummary = true
            } else {
                currentQuestionIdx++
                selectedAnswer = null
                isCorrect = null
            }
        }
    }

    if (showSummary) {
        ExamSummaryDialog(
            score = score,
            total = 5,
            viewModel = viewModel,
            onDismiss = {
                viewModel.saveExamRecord("Pagsasanay 1", score, 5)
                viewModel.exam1Score.value = score
                viewModel.exam1Passed.value = true
                viewModel.navigateTo(Screen.Level1Menu)
            }
        )
    }

    val katinigLetters = viewModel.lettersKatinig
    // Generate 5 distinct letters for the exam
    val examLetters = remember { katinigLetters.shuffled().take(5) }
    val currentLetter = if (currentQuestionIdx < 5) examLetters[currentQuestionIdx] else examLetters.last()
    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char)
        val shuffledPool = katinigLetters.shuffled()
        var i = 0
        while (options.size < 4 && i < shuffledPool.size) {
            options.add(shuffledPool[i].char)
            i++
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 1: Piliin ang Titik",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "${timeLeft}s",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang simulang titik ng larawang ito?", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.emoji, fontSize = 60.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isCorrect != null) {
                Text(
                    text = if (isCorrect == true) "Tama ang iyong sagot!" else "Mali ang iyong sagot",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect == true) Color(0xFF2E7D32) else Color.Red
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(220.dp)
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun KatinigExam2Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    var showSummary by remember { mutableStateOf(false) }

    var timeLeft by remember { mutableStateOf(60) }
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && !showSummary) {
            kotlinx.coroutines.delay(1000)
            timeLeft--
        }
        if (timeLeft <= 0 && !showSummary) {
            showSummary = true
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect != null) {
            if (isCorrect == true) score++
            kotlinx.coroutines.delay(1500)
            if (currentQuestionIdx >= 4) {
                showSummary = true
            } else {
                currentQuestionIdx++
                selectedAnswer = null
                isCorrect = null
            }
        }
    }

    if (showSummary) {
        ExamSummaryDialog(
            score = score,
            total = 5,
            viewModel = viewModel,
            onDismiss = {
                viewModel.saveExamRecord("Pagsasanay 2", score, 5)
                viewModel.exam2Score.value = score
                viewModel.exam2Passed.value = true
                viewModel.navigateTo(Screen.Level1Menu)
            }
        )
    }

    val katinigLetters = viewModel.lettersKatinig
    // Generate 5 distinct letters for the exam
    val examLetters = remember { katinigLetters.shuffled().take(5) }
    val currentLetter = if (currentQuestionIdx < 5) examLetters[currentQuestionIdx] else examLetters.last()
    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char.lowercase(java.util.Locale.getDefault()))
        val shuffledPool = katinigLetters.shuffled()
        var i = 0
        while (options.size < 4 && i < shuffledPool.size) {
            options.add(shuffledPool[i].char.lowercase(java.util.Locale.getDefault()))
            i++
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 2: Itugma ang Titik",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "${timeLeft}s",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang maliit na titik nito?", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(currentLetter.char, fontSize = 60.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isCorrect != null) {
                Text(
                    text = if (isCorrect == true) "Tama ang iyong sagot!" else "Mali ang iyong sagot",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect == true) Color(0xFF2E7D32) else Color.Red
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(220.dp)
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char.lowercase(java.util.Locale.getDefault())
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun KatinigExam3Screen(viewModel: AppViewModel, progress: UserProgress) {
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    var showSummary by remember { mutableStateOf(false) }

    var timeLeft by remember { mutableStateOf(60) }
    LaunchedEffect(Unit) {
        while (timeLeft > 0 && !showSummary) {
            kotlinx.coroutines.delay(1000)
            timeLeft--
        }
        if (timeLeft <= 0 && !showSummary) {
            showSummary = true
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect != null) {
            if (isCorrect == true) score++
            kotlinx.coroutines.delay(1500)
            if (currentQuestionIdx >= 4) {
                showSummary = true
            } else {
                currentQuestionIdx++
                selectedAnswer = null
                isCorrect = null
            }
        }
    }

    var showOverallSummary by remember { mutableStateOf(false) }

    if (showSummary) {
        ExamSummaryDialog(
            score = score,
            total = 5,
            viewModel = viewModel,
            onDismiss = {
                viewModel.saveExamRecord("Pagsasanay 3", score, 5)
                viewModel.exam3Score.value = score
                viewModel.exam3Passed.value = true
                showSummary = false
                showOverallSummary = true
            }
        )
    }

    if (showOverallSummary) {
        val s1 by viewModel.exam1Score.collectAsState()
        val s2 by viewModel.exam2Score.collectAsState()
        val s3 by viewModel.exam3Score.collectAsState()
        
        OverallSummaryDialog(
            score1 = s1,
            score2 = s2,
            score3 = score, // using current session score for immediate feedback
            viewModel = viewModel,
            onDismiss = {
                viewModel.unlockLevel(2)
                viewModel.navigateTo(Screen.Home)
            }
        )
    }

    val katinigLetters = viewModel.lettersKatinig
    // Generate 5 distinct letters for the exam
    val examLetters = remember { katinigLetters.shuffled().take(5) }
    val currentLetter = if (currentQuestionIdx < 5) examLetters[currentQuestionIdx] else examLetters.last()
    val missingWord = remember(currentLetter) {
        val w = currentLetter.word
        if (w.lowercase(java.util.Locale.getDefault()).startsWith(currentLetter.char.lowercase(java.util.Locale.getDefault()))) {
            "_ " + w.substring(currentLetter.char.length)
        } else {
            "_ " + w.substring(1)
        }
    }
    val choices = remember(currentLetter) {
        val options = mutableSetOf(currentLetter.char)
        val shuffledPool = katinigLetters.shuffled()
        var i = 0
        while (options.size < 4 && i < shuffledPool.size) {
            options.add(shuffledPool[i].char)
            i++
        }
        options.toList().shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Level1Menu) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagsasanay 3: Nawawalang Titik",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "${timeLeft}s",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Ano ang nawawalang titik?", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentLetter.emoji, fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(missingWord, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isCorrect != null) {
                Text(
                    text = if (isCorrect == true) "Tama ang iyong sagot!" else "Mali ang iyong sagot",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isCorrect == true) Color(0xFF2E7D32) else Color.Red
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(220.dp)
            ) {
                items(choices) { choice ->
                    val isSelected = choice == selectedAnswer
                    val color = if (isSelected) {
                        if (isCorrect == true) Color.Green else Color.Red
                    } else Color.White

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable {
                                if (selectedAnswer == null) {
                                    selectedAnswer = choice
                                    isCorrect = choice == currentLetter.char
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(choice, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun VoiceRecordingScreen(viewModel: AppViewModel) {
    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("voice_recording_screen")
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryPurple)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Bumalik", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RECORD BOSES",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
        }

        if (!hasPermission) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Kailangan ng mikropono para makapag-record.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(viewModel.letters) { letter ->
                    VoiceRecordItem(letter = letter.char, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun VoiceRecordItem(letter: String, viewModel: AppViewModel) {
    var isRecording by remember { mutableStateOf(false) }
    var hasRecording by remember { mutableStateOf(viewModel.getRecordedAudioFile(letter).exists()) }

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CardBorderLevel),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Letrang $letter", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasRecording && !isRecording) {
                    IconButton(onClick = { viewModel.playRecordedAudio(letter) }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = PrimaryPurple)
                    }
                    IconButton(onClick = { 
                        viewModel.deleteRecordedAudio(letter) 
                        hasRecording = false
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
                
                IconButton(onClick = {
                    if (isRecording) {
                        viewModel.stopRecording()
                        isRecording = false
                        hasRecording = true
                    } else {
                        viewModel.startRecording(letter)
                        isRecording = true
                    }
                }) {
                    Icon(
                        if (isRecording) Icons.Default.Stop else Icons.Default.Mic, 
                        contentDescription = if (isRecording) "Stop" else "Record", 
                        tint = if (isRecording) Color.Red else PrimaryPurple
                    )
                }
            }
        }
    }
}
