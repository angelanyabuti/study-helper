package com.example.study_helper.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.study_helper.gemini.FlashcardUiState
import com.example.study_helper.gemini.FlashcardViewModel
import com.example.study_helper.ui.theme.AccentBlue
import com.example.study_helper.ui.theme.BackgroundDark
import com.example.study_helper.ui.theme.BorderDark
import com.example.study_helper.ui.theme.SurfaceDark
import com.example.study_helper.ui.theme.SurfaceDarkAlt
import com.example.study_helper.ui.theme.TextMuted
import com.example.study_helper.ui.theme.TextPrimary
import com.example.study_helper.ui.theme.TextSecondary

// ── Palette (chip states) ────────────────────────────────────────────────
private val ChipSelectedBg     = AccentBlue.copy(alpha = 0.15f)
private val ChipSelectedBorder = AccentBlue
private val ChipSelectedText   = AccentBlue
private val ChipDefaultBg      = SurfaceDark
private val ChipDefaultBorder  = BorderDark
private val ChipDefaultText    = TextSecondary

@Composable
fun InputScreen(
    navController: NavController,
    viewModel: FlashcardViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    var topic          by remember { mutableStateOf("") }
    var selectedCount  by remember { mutableStateOf(5) }
    var selectedDiff   by remember { mutableStateOf("Beginner") }
    var showTopicError by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is FlashcardUiState.Success) {
            navController.navigate("flashcards") {
                popUpTo("input") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {

        when (uiState) {

            // ── Loading / Success (navigating away) ────────────────────────
            is FlashcardUiState.Loading, is FlashcardUiState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = AccentBlue)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Generating flashcards…",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }

            // ── Error ───────────────────────────────────────────────────────
            is FlashcardUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Something went wrong. Please try again.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { viewModel.generateFlashcards(topic, selectedCount, selectedDiff) },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
                    ) {
                        Text("Retry", color = Color.White)
                    }
                }
            }

            // ── Main form ───────────────────────────────────────────────────
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    // ── Header ───────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 56.dp, bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceDarkAlt)
                                .border(0.5.dp, BorderDark, CircleShape)
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "New topic",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Generate flashcards with AI",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(28.dp))

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                        // ── Topic input ──────────────────────────────────
                        Text(
                            "Topic",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = topic,
                            onValueChange = {
                                topic = it
                                if (it.isNotBlank()) showTopicError = false
                            },
                            placeholder = {
                                Text(
                                    "e.g. The Roman Empire",
                                    color = TextMuted,
                                    fontSize = 15.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentBlue,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = AccentBlue,
                                focusedContainerColor = SurfaceDark,
                                unfocusedContainerColor = SurfaceDark
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 15.sp)
                        )
                        if (showTopicError) {
                            Text(
                                "Enter a topic first.",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // ── Quick pick chips ─────────────────────────────
                        Text(
                            "Quick pick",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        val quickTopics = listOf(
                            "The Roman Empire",
                            "Photosynthesis",
                            "World War II",
                            "Algebra basics",
                            "Human anatomy"
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            quickTopics.forEach { suggestion ->
                                val isSelected = topic == suggestion
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(99.dp))
                                        .background(
                                            if (isSelected) ChipSelectedBg
                                            else ChipDefaultBg
                                        )
                                        .border(
                                            0.5.dp,
                                            if (isSelected) ChipSelectedBorder
                                            else ChipDefaultBorder,
                                            RoundedCornerShape(99.dp)
                                        )
                                        .clickable {
                                            topic = suggestion
                                            showTopicError = false
                                        }
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = suggestion,
                                        color = if (isSelected) ChipSelectedText
                                        else ChipDefaultText,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // ── Number of cards ──────────────────────────────
                        Text(
                            "Number of cards",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 20).forEach { count ->
                                val isSelected = selectedCount == count
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(99.dp))
                                        .background(
                                            if (isSelected) ChipSelectedBg
                                            else ChipDefaultBg
                                        )
                                        .border(
                                            0.5.dp,
                                            if (isSelected) ChipSelectedBorder
                                            else ChipDefaultBorder,
                                            RoundedCornerShape(99.dp)
                                        )
                                        .clickable { selectedCount = count }
                                        .padding(horizontal = 18.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = count.toString(),
                                        color = if (isSelected) ChipSelectedText
                                        else ChipDefaultText,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // ── Difficulty ───────────────────────────────────
                        Text(
                            "Difficulty",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { diff ->
                                val isSelected = selectedDiff == diff
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(99.dp))
                                        .background(
                                            if (isSelected) ChipSelectedBg
                                            else ChipDefaultBg
                                        )
                                        .border(
                                            0.5.dp,
                                            if (isSelected) ChipSelectedBorder
                                            else ChipDefaultBorder,
                                            RoundedCornerShape(99.dp)
                                        )
                                        .clickable { selectedDiff = diff }
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = diff,
                                        color = if (isSelected) ChipSelectedText
                                        else ChipDefaultText,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(28.dp))

                        // ── Generate button ──────────────────────────────
                        Button(
                            onClick = {
                                if (topic.isBlank()) {
                                    showTopicError = true
                                } else {
                                    viewModel.generateFlashcards(topic, selectedCount, selectedDiff)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentBlue,
                                disabledContainerColor = AccentBlue.copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                "Generate flashcards",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
