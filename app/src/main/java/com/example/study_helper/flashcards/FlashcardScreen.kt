package com.example.study_helper.flashcards

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.study_helper.data.Flashcard
import com.example.study_helper.gemini.FlashcardUiState
import com.example.study_helper.gemini.FlashcardViewModel
import com.example.study_helper.ui.theme.AccentBlue
import com.example.study_helper.ui.theme.AnswerCardBg
import com.example.study_helper.ui.theme.AnswerCardBorder
import com.example.study_helper.ui.theme.BackgroundDark
import com.example.study_helper.ui.theme.BorderDark
import com.example.study_helper.ui.theme.ErrorRed
import com.example.study_helper.ui.theme.SuccessGreen
import com.example.study_helper.ui.theme.SurfaceDark
import com.example.study_helper.ui.theme.SurfaceDarkAlt
import com.example.study_helper.ui.theme.TextMuted
import com.example.study_helper.ui.theme.TextPrimary
import com.example.study_helper.ui.theme.TextSecondary
import com.example.study_helper.ui.theme.WarningAmber

@Composable
fun FlashcardScreen(
    navController: NavController,
    viewModel: FlashcardViewModel,
    onBack: () -> Unit = {
        viewModel.resetToInitial()
        navController.popBackStack()
    }
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is FlashcardUiState.Loading -> {
                CircularProgressIndicator(color = AccentBlue)
            }
            is FlashcardUiState.Success -> {
                FlashcardContent(
                    navController = navController,
                    topic = state.topic,
                    flashcards = state.flashcards
                )
            }
            is FlashcardUiState.Error -> {
                Text(text = state.message, color = ErrorRed)
            }
            is FlashcardUiState.Initial -> {
                Text(
                    text = "Generate flashcards from the input screen to get started!",
                    color = TextSecondary,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
fun FlashcardContent(
    navController: NavController,
    topic: String,
    flashcards: List<Flashcard>
) {
    var cards by remember(flashcards) { mutableStateOf(flashcards) }
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    val currentFlashcard = cards[currentCardIndex]

    LaunchedEffect(currentCardIndex, cards) {
        isFlipped = false
    }

    fun goToNext() {
        currentCardIndex = (currentCardIndex + 1) % cards.size
    }

    fun goToPrevious() {
        currentCardIndex = (currentCardIndex - 1 + cards.size) % cards.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 56.dp)
    ) {
        // ── Header ───────────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconCircleButton(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Back",
                onClick = { navController.popBackStack() }
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = "Card ${currentCardIndex + 1} of ${cards.size}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            IconCircleButton(
                icon = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                onClick = {
                    cards = cards.shuffled()
                    currentCardIndex = 0
                }
            )
            Spacer(Modifier.width(8.dp))
            IconCircleButton(
                icon = Icons.Default.MoreVert,
                contentDescription = "More options",
                onClick = {}
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Progress ─────────────────────────────────────────────────────
        val progress = (currentCardIndex + 1) / cards.size.toFloat()
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = AccentBlue,
            trackColor = SurfaceDarkAlt
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${currentCardIndex + 1} / ${cards.size}", color = TextMuted, fontSize = 12.sp)
            Text("${(progress * 100).toInt()}% done", color = TextMuted, fontSize = 12.sp)
        }

        Spacer(Modifier.height(20.dp))

        // ── Card ─────────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clickable { isFlipped = !isFlipped },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFlipped) AnswerCardBg else SurfaceDark
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isFlipped) AnswerCardBorder else BorderDark
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isFlipped) "ANSWER" else "QUESTION",
                        color = if (isFlipped) AccentBlue else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                    Icon(
                        imageVector = if (isFlipped) Icons.Default.CheckCircle else Icons.Default.Refresh,
                        contentDescription = null,
                        tint = if (isFlipped) AccentBlue else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = if (isFlipped) currentFlashcard.answer else currentFlashcard.question,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (!isFlipped) {
                    Text(
                        text = "Tap to reveal answer",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Grading (answer state only) ─────────────────────────────────
        if (isFlipped) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RatingButton(
                    label = "Hard",
                    icon = Icons.Default.Close,
                    color = ErrorRed,
                    modifier = Modifier.weight(1f),
                    onClick = { goToNext() }
                )
                RatingButton(
                    label = "Okay",
                    icon = Icons.Default.Remove,
                    color = WarningAmber,
                    modifier = Modifier.weight(1f),
                    onClick = { goToNext() }
                )
                RatingButton(
                    label = "Easy",
                    icon = Icons.Default.Check,
                    color = SuccessGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { goToNext() }
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // ── Navigation row ───────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconCircleButton(
                icon = Icons.Default.ChevronLeft,
                contentDescription = "Previous",
                enabled = cards.size > 1,
                onClick = { goToPrevious() }
            )
            Text(
                text = if (isFlipped) "How did you do?" else "Tap card to flip",
                color = TextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AccentBlue)
                    .clickable(enabled = cards.size > 1) { goToNext() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next",
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
        }
    }
}

@Composable
private fun IconCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(SurfaceDarkAlt)
            .border(0.5.dp, BorderDark, CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) TextSecondary else TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun RatingButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = color)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 13.sp)
    }
}
