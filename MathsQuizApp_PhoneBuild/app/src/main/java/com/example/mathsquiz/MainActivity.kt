package com.example.mathsquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class QuizQuestion(
    val question: String,
    val type: Type,
    val options: List<String> = emptyList(),
    val answer: String,
    val imageName: String? = null
) {
    enum class Type { MCQ, TRUE_FALSE, FILL_BLANK }
}

private val questions = listOf(
    QuizQuestion("What is 12 + 8?", QuizQuestion.Type.MCQ, listOf("18","20","22","24"), "20"),
    QuizQuestion("What is 7 × 6?", QuizQuestion.Type.MCQ, listOf("36","40","42","48"), "42"),
    QuizQuestion("What is 81 ÷ 9?", QuizQuestion.Type.MCQ, listOf("7","8","9","10"), "9"),
    QuizQuestion("15 − 23 is −8.", QuizQuestion.Type.TRUE_FALSE, answer = "True"),
    QuizQuestion("What is the value of 5²?", QuizQuestion.Type.MCQ, listOf("10","15","20","25"), "25"),
    QuizQuestion("Fill in: 3x + 2 = 11, x = ____", QuizQuestion.Type.FILL_BLANK, answer = "3"),
    QuizQuestion("A triangle has 3 sides.", QuizQuestion.Type.TRUE_FALSE, answer = "True"),
    QuizQuestion("What is 25% of 80?", QuizQuestion.Type.MCQ, listOf("10","15","20","25"), "20"),
    QuizQuestion("What is the perimeter of a square with side 6 cm?", QuizQuestion.Type.MCQ, listOf("12 cm","18 cm","24 cm","36 cm"), "24 cm"),
    QuizQuestion("Fill in: 2³ = ____", QuizQuestion.Type.FILL_BLANK, answer = "8"),
    QuizQuestion("The sum of angles in a triangle is 180°.", QuizQuestion.Type.TRUE_FALSE, answer = "True"),
    QuizQuestion("What is 144 ÷ 12?", QuizQuestion.Type.MCQ, listOf("10","11","12","14"), "12"),
    QuizQuestion("What is √49?", QuizQuestion.Type.MCQ, listOf("5","6","7","8"), "7"),
    QuizQuestion("Fill in: 9 × 9 = ____", QuizQuestion.Type.FILL_BLANK, answer = "81"),
    QuizQuestion("0 is an even number.", QuizQuestion.Type.TRUE_FALSE, answer = "True"),
    QuizQuestion("What is 3/4 as a percentage?", QuizQuestion.Type.MCQ, listOf("25%","50%","75%","80%"), "75%"),
    QuizQuestion("What is the area of a rectangle 8 cm × 5 cm?", QuizQuestion.Type.MCQ, listOf("13 cm²","26 cm²","40 cm²","80 cm²"), "40 cm²"),
    QuizQuestion("Fill in: 1000 − 375 = ____", QuizQuestion.Type.FILL_BLANK, answer = "625"),
    QuizQuestion("A circle has four corners.", QuizQuestion.Type.TRUE_FALSE, answer = "False"),
    QuizQuestion("What is 2 + 3 × 4?", QuizQuestion.Type.MCQ, listOf("20","14","24","10"), "14"),
    QuizQuestion("What is the next number: 2, 4, 6, 8, ____?", QuizQuestion.Type.MCQ, listOf("9","10","11","12"), "10"),
    QuizQuestion("Fill in: 1 kilometre = ____ metres", QuizQuestion.Type.FILL_BLANK, answer = "1000"),
    QuizQuestion("12 is a prime number.", QuizQuestion.Type.TRUE_FALSE, answer = "False"),
    QuizQuestion("What is 10% of 250?", QuizQuestion.Type.MCQ, listOf("15","20","25","30"), "25"),
    QuizQuestion("What is the mean of 4, 6 and 8?", QuizQuestion.Type.MCQ, listOf("5","6","7","8"), "6")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MathsQuizApp() }
    }
}

@Composable
fun MathsQuizApp() {
    var started by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var current by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    if (!started) {
        HomeScreen {
            started = true
            finished = false
            current = 0
            score = 0
        }
    } else if (finished) {
        ResultScreen(score) {
            started = false
            finished = false
            current = 0
            score = 0
        }
    } else {
        QuizScreen(
            question = questions[current],
            number = current + 1,
            score = score,
            onAnswered = { correct ->
                if (correct) score++
                if (current == questions.lastIndex) finished = true
                else current++
            }
        )
    }
}

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF07152F), Color(0xFF123B68), Color(0xFF0A6B72))
                )
            )
    ) { content() }
}

@Composable
fun HomeScreen(onStart: () -> Unit) {
    AppBackground {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🧮", fontSize = 64.sp)
            Text("MATHS QUIZ", fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("25 Questions • 2 Minutes Each", color = Color(0xFFBCEBFF), fontSize = 17.sp)
            Spacer(Modifier.height(30.dp))
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha=.12f))) {
                Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Question types", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                    Spacer(Modifier.height(10.dp))
                    Text("🔘 Multiple Choice\n☑️ True / False\n✍️ Fill in the Blank\n⏱️ Auto timer & marking", color = Color.White, textAlign = TextAlign.Center, lineHeight = 28.sp)
                }
            }
            Spacer(Modifier.height(30.dp))
            Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp)) {
                Text("START QUIZ", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun QuizScreen(question: QuizQuestion, number: Int, score: Int, onAnswered: (Boolean) -> Unit) {
    var timeLeft by remember(question) { mutableIntStateOf(120) }
    var selected by remember(question) { mutableStateOf<String?>(null) }
    var textAnswer by remember(question) { mutableStateOf("") }
    var submitted by remember(question) { mutableStateOf(false) }

    LaunchedEffect(question) {
        while (timeLeft > 0 && !submitted) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0 && !submitted) {
            submitted = true
            onAnswered(false)
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    AppBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Question $number / 25", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("%02d:%02d".format(minutes, seconds), color = if (timeLeft <= 20) Color(0xFFFF7676) else Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(progress = { number / 25f }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(22.dp))

            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha=.94f))) {
                Column(Modifier.padding(22.dp)) {
                    Text(question.question, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color(0xFF13213D))
                    Spacer(Modifier.height(20.dp))

                    when (question.type) {
                        QuizQuestion.Type.MCQ -> question.options.forEach { option ->
                            OptionButton(option, selected == option && !submitted) { if (!submitted) selected = option }
                        }
                        QuizQuestion.Type.TRUE_FALSE -> listOf("True", "False").forEach { option ->
                            OptionButton(option, selected == option && !submitted) { if (!submitted) selected = option }
                        }
                        QuizQuestion.Type.FILL_BLANK -> {
                            OutlinedTextField(
                                value = textAnswer,
                                onValueChange = { if (!submitted) textAnswer = it },
                                label = { Text("Your answer") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(Modifier.height(22.dp))
                    Button(
                        enabled = !submitted && (question.type == QuizQuestion.Type.FILL_BLANK || selected != null),
                        onClick = {
                            submitted = true
                            val given = if (question.type == QuizQuestion.Type.FILL_BLANK) textAnswer.trim() else selected ?: ""
                            onAnswered(given.equals(question.answer, ignoreCase = true))
                        },
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("SUBMIT & NEXT", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Text("Current Score: $score", color = Color.White, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun OptionButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFD7F0FF) else Color(0xFFF2F5F9))
    ) {
        Text(text, modifier = Modifier.padding(17.dp), fontSize = 17.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ResultScreen(score: Int, onRestart: () -> Unit) {
    val percent = score * 100 / questions.size
    AppBackground {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🏆", fontSize = 70.sp)
            Text("QUIZ COMPLETED!", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha=.95f))) {
                Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$score / 25", fontSize = 46.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF123B68))
                    Text("$percent%", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text("Correct Answers : $score")
                    Text("Wrong Answers   : ${25 - score}")
                    Text("Total Questions : 25")
                }
            }
            Spacer(Modifier.height(26.dp))
            Button(onClick = onRestart, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(17.dp)) {
                Text("TRY AGAIN", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
