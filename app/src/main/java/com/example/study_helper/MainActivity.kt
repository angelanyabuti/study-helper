package com.example.study_helper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.study_helper.flashcards.FlashcardScreen
import com.example.study_helper.gemini.FlashcardViewModel
import com.example.study_helper.pages.InputScreen
import com.example.study_helper.ui.theme.AccentBlue
import com.example.study_helper.ui.theme.AccentBlueBorder
import com.example.study_helper.ui.theme.BackgroundDark
import com.example.study_helper.ui.theme.BorderDark
import com.example.study_helper.ui.theme.Study_helperTheme
import com.example.study_helper.ui.theme.SuccessGreen
import com.example.study_helper.ui.theme.SurfaceDark
import com.example.study_helper.ui.theme.SurfaceDarkAlt
import com.example.study_helper.ui.theme.StreakBg
import com.example.study_helper.ui.theme.TextMuted
import com.example.study_helper.ui.theme.TextPrimary
import com.example.study_helper.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Study_helperTheme {
                val navController = rememberNavController()
                // Create the ViewModel here, so it's shared between the screens.
                val flashcardViewModel: FlashcardViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            Greeting(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }
                        composable("input") {
                            InputScreen(
                                navController = navController,
                                viewModel = flashcardViewModel
                            )
                        }
                        composable("flashcards") {
                            FlashcardScreen(
                                navController = navController,
                                viewModel = flashcardViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Study_helperTheme {
        Greeting(navController = rememberNavController())
    }
}

private data class SubjectTile(
    val icon: ImageVector,
    val title: String,
    val caption: String,
    val isAction: Boolean = false
)

private val subjectTiles = listOf(
    SubjectTile(Icons.Default.Functions, "Mathematics", "12 cards saved"),
    SubjectTile(Icons.Default.Biotech, "Biology", "8 cards saved"),
    SubjectTile(Icons.Default.Public, "World history", "15 cards saved"),
    SubjectTile(Icons.Default.Add, "New topic", "Generate cards", isAction = true)
)

private data class StudyMode(
    val icon: ImageVector,
    val label: String,
    val enabled: Boolean
)

private val studyModes = listOf(
    StudyMode(Icons.Default.Style, "Flashcards", enabled = true),
    StudyMode(Icons.Default.HelpOutline, "Quiz mode", enabled = false),
    StudyMode(Icons.Default.Assignment, "Practice", enabled = false)
)

@Composable
fun Greeting(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp, vertical = 56.dp)
    ) {
        Text(
            "GOOD MORNING",
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "What do you want to study?",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Pick a subject or enter your own topic.",
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            subjectTiles.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { tile ->
                        SubjectCard(
                            tile = tile,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { navController.navigate("input") }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        Text(
            "Study mode",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            studyModes.forEach { mode ->
                StudyModeButton(
                    mode = mode,
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (mode.enabled) {
                                Modifier.clickable { navController.navigate("input") }
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(StreakBg)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "3-day streak",
                    color = SuccessGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Keep it up — study today to maintain your streak.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SubjectCard(tile: SubjectTile, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(0.5.dp, BorderDark, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SurfaceDarkAlt),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = tile.icon,
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            tile.title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            tile.caption,
            color = TextMuted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun StudyModeButton(mode: StudyMode, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(
                0.5.dp,
                if (mode.enabled) AccentBlueBorder else BorderDark,
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = mode.icon,
            contentDescription = null,
            tint = if (mode.enabled) AccentBlue else TextMuted,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            mode.label,
            color = if (mode.enabled) TextPrimary else TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
