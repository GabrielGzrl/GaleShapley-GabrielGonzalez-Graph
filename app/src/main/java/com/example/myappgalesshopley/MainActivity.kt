package com.example.myappgalesshopley

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(
                    modifier = Modifier.fillMaxSize().systemBarsPadding(),
                    color = Color(0xFF0F172A)
                ) {
                    GaleShapleyComplexApp()
                }
            }
        }
    }
}

enum class ActionType { START, PROPOSAL, MATCH, SWAP, REJECTION, COMPLETE, ERROR }

data class Participant(val id: Int, val name: String, val preferences: List<Int>)
data class StepState(val description: String, val connections: Map<Int, Int>, val type: ActionType)

val colorSurface = Color(0xFF1E293B)
val colorPrimary = Color(0xFF38BDF8)
val colorSecondary = Color(0xFFF472B6)
val colorSuccess = Color(0xFF34D399)
val colorWarning = Color(0xFFFBBF24)
val colorError = Color(0xFFF87171)
val colorTextDim = Color(0xFF94A3B8)

@Composable
fun GaleShapleyComplexApp() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Simulador Algorítmico", "Teoría y Matemáticas")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFF0F172A),
            contentColor = colorPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = colorPrimary,
                    height = 4.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = {
                        Text(
                            title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTabIndex == index) colorPrimary else colorTextDim,
                            fontSize = 15.sp
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> SimulationScreen()
            1 -> TheoryScreen()
        }
    }
}

@Composable
fun TheoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Fundamentación Teórica", color = colorPrimary, fontSize = 26.sp, fontWeight = FontWeight.Black)

        TheoryCard(title = "Contexto Histórico", borderColor = colorPrimary) {
            Text(
                text = "Desarrollado en 1962 por los matemáticos y economistas David Gale y Lloyd Shapley en su artículo histórico 'College Admissions and the Stability of Marriage'.\n\nEl impacto de este algoritmo en la teoría de juegos cooperativos y el diseño de mercados fue tan profundo que le otorgó a Lloyd Shapley el Premio Nobel Conmemorativo de Ciencias Económicas en el año 2012.",
                color = Color(0xFFF8FAFC), fontSize = 15.sp, lineHeight = 22.sp
            )
        }

        TheoryCard(title = "Formulación Matemática", borderColor = colorSecondary) {
            Text(
                text = "Sean dos conjuntos disjuntos A (Proponentes) y B (Receptores) donde |A| = |B| = N.\n\n" +
                        "1. Cada elemento a ∈ A tiene una relación de orden estricto y total ≻_a sobre los elementos de B.\n" +
                        "2. Un emparejamiento (matching) M es una función biyectiva f: A → B.\n\n" +
                        "Definición de Estabilidad:\n" +
                        "El emparejamiento M es estable si y solo si NO existe un «par bloqueador». Un par (a, b) es bloqueador si a y b no están emparejados en M, pero:\n" +
                        "b ≻_a f(a)   ∧   a ≻_b f⁻¹(b)\n\n" +
                        "Teorema de Gale-Shapley:\n" +
                        "Para cualquier conjunto de preferencias, siempre existe al menos un emparejamiento estable, y el algoritmo lo encuentra en complejidad polinomial O(N²).",
                color = Color(0xFFF8FAFC), fontSize = 14.sp, lineHeight = 24.sp, fontFamily = FontFamily.Monospace
            )
        }

        TheoryCard(title = "Propiedades del Grafo", borderColor = colorSuccess) {
            Text(
                text = "• Óptimo para el Proponente: De todos los emparejamientos estables posibles, el algoritmo produce aquel donde cada proponente (A) obtiene su pareja más preferida matemáticamente posible.\n" +
                        "• Pésimo para el Receptor: Como consecuencia, el conjunto receptor (B) termina con el peor emparejamiento estable posible para ellos.",
                color = Color(0xFFF8FAFC), fontSize = 15.sp, lineHeight = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun TheoryCard(title: String, borderColor: Color, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, color = borderColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
fun SimulationScreen() {
    var sizeText by remember { mutableStateOf("5") }
    var n by remember { mutableIntStateOf(5) }
    var simulationSpeed by remember { mutableFloatStateOf(1000f) }

    val preferencesA = remember { mutableStateMapOf<Int, String>() }
    val preferencesB = remember { mutableStateMapOf<Int, String>() }

    var steps by remember { mutableStateOf<List<StepState>>(emptyList()) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val randomizeData = {
        for (i in 1..n) {
            preferencesA[i] = (1..n).shuffled().joinToString(", ") { (it * 10).toString() }
            preferencesB[i * 10] = (1..n).shuffled().joinToString(", ")
        }
    }

    LaunchedEffect(n) {
        preferencesA.clear()
        preferencesB.clear()
        steps = emptyList()
        randomizeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = colorSurface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = sizeText,
                        onValueChange = {
                            sizeText = it
                            // Ampliado el límite hasta 12 para matrices complejas
                            it.toIntOrNull()?.let { newN -> if (newN in 1..12) n = newN }
                        },
                        label = { Text("Dimensión (N)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPrimary,
                            focusedLabelColor = colorPrimary,
                            unfocusedBorderColor = colorTextDim.copy(alpha = 0.5f)
                        )
                    )
                    Button(
                        onClick = { randomizeData() },
                        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary.copy(alpha = 0.2f), contentColor = colorPrimary),
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("↻ Aleatorio", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Column {
                    Text("Retardo de Simulación: ${simulationSpeed.toInt()} ms", color = colorTextDim, fontSize = 13.sp)
                    Slider(
                        value = simulationSpeed,
                        onValueChange = { simulationSpeed = it },
                        valueRange = 200f..2500f,
                        colors = SliderDefaults.colors(
                            thumbColor = colorPrimary,
                            activeTrackColor = colorPrimary,
                            inactiveTrackColor = colorTextDim.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Proponentes (A)", color = colorPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                for (i in 1..n) {
                    OutlinedTextField(
                        value = preferencesA[i] ?: "",
                        onValueChange = { preferencesA[i] = it },
                        textStyle = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Monospace),
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPrimary,
                            unfocusedBorderColor = colorSurface
                        )
                    )
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Receptores (B)", color = colorSecondary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                for (i in 1..n) {
                    OutlinedTextField(
                        value = preferencesB[i * 10] ?: "",
                        onValueChange = { preferencesB[i * 10] = it },
                        textStyle = TextStyle(fontSize = 14.sp, fontFamily = FontFamily.Monospace),
                        singleLine = true, modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorSecondary,
                            unfocusedBorderColor = colorSurface
                        )
                    )
                }
            }
        }

        Button(
            onClick = {
                if (isAnimating) return@Button
                try {
                    val groupA = (1..n).map { Participant(it, "A$it", (preferencesA[it] ?: "").split(",").mapNotNull { v -> v.trim().toIntOrNull() }) }
                    val groupB = (1..n).map { Participant(it * 10, "B${it * 10}", (preferencesB[it * 10] ?: "").split(",").mapNotNull { v -> v.trim().toIntOrNull() }) }

                    steps = GaleShapleyEngine(groupA, groupB).solveWithSteps()
                    coroutineScope.launch {
                        isAnimating = true
                        for (idx in steps.indices) {
                            currentStepIndex = idx
                            delay(simulationSpeed.toLong())
                        }
                        isAnimating = false
                    }
                } catch (e: Exception) {
                    steps = listOf(StepState("Error lógico en matrices.", emptyMap(), ActionType.ERROR))
                }
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimary, contentColor = Color(0xFF0F172A))
        ) {
            Text(if (isAnimating) "Procesando Algoritmo..." else "▶ Iniciar Simulación", fontWeight = FontWeight.Black, fontSize = 16.sp)
        }

        AnimatedVisibility(
            visible = steps.isNotEmpty(),
            enter = fadeIn(tween(500)) + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val currentStep = steps.getOrNull(currentStepIndex) ?: return@AnimatedVisibility

            Card(
                colors = CardDefaults.cardColors(containerColor = colorSurface),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    val logColor = when (currentStep.type) {
                        ActionType.MATCH, ActionType.COMPLETE -> colorSuccess
                        ActionType.REJECTION -> colorError
                        ActionType.SWAP -> colorWarning
                        else -> colorPrimary
                    }

                    Text("Paso ${currentStepIndex + 1} de ${steps.size}", color = colorTextDim, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentStep.description,
                        color = logColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().background(logColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)).padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    AdvancedBezierGraph(
                        n = n, activeConnections = currentStep.connections,
                        colorA = colorPrimary, colorB = colorSecondary,
                        modifier = Modifier.fillMaxWidth().height(360.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AdvancedBezierGraph(n: Int, activeConnections: Map<Int, Int>, colorA: Color, colorB: Color, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val leftX = w * 0.15f
        val rightX = w * 0.85f
        val stepY = h / (n + 1)

        // Escalamiento dinámico para que los nodos quepan en N altos (Ej: 9, 10, 11, 12)
        val nodeRadius = if (n >= 10) 14f else if (n >= 8) 18f else 26f
        val textSize = if (n >= 10) 9.sp else if (n >= 8) 11.sp else 13.sp
        val strokeGlow = if (n >= 10) 8f else 14f
        val strokeMain = if (n >= 10) 3f else 5f

        val nodeAOffset = (1..n).associateWith { Offset(leftX, stepY * it) }
        val nodeBOffset = (1..n).associateWith { (it * 10) to Offset(rightX, stepY * it) }.values.toMap()
        val controlPointOffset = w * 0.4f

        activeConnections.forEach { (bId, aId) ->
            val start = nodeAOffset[aId]
            val end = nodeBOffset[bId]
            if (start != null && end != null) {
                val path = Path().apply {
                    moveTo(start.x, start.y)
                    cubicTo(start.x + controlPointOffset, start.y, end.x - controlPointOffset, end.y, end.x, end.y)
                }
                drawPath(path = path, color = Color(0xFFE2E8F0).copy(alpha = 0.9f), style = Stroke(width = strokeMain, cap = StrokeCap.Round))
                drawPath(path = path, color = Color.White.copy(alpha = 0.1f), style = Stroke(width = strokeGlow, cap = StrokeCap.Round))
            }
        }

        val drawNode = { center: Offset, text: String, baseColor: Color ->
            drawCircle(color = baseColor.copy(alpha = 0.15f), radius = nodeRadius + (if(n>=8) 6f else 12f), center = center)
            drawCircle(color = baseColor, radius = nodeRadius, center = center)
            val layout = textMeasurer.measure(text, TextStyle(color = Color(0xFF0F172A), fontSize = textSize, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace))
            drawText(layout, topLeft = Offset(center.x - layout.size.width / 2, center.y - layout.size.height / 2))
        }

        nodeAOffset.forEach { (id, center) -> drawNode(center, "A$id", colorA) }
        nodeBOffset.forEach { (id, center) -> drawNode(center, "B$id", colorB) }
    }
}

class GaleShapleyEngine(private val groupA: List<Participant>, private val groupB: List<Participant>) {
    fun solveWithSteps(): List<StepState> {
        val steps = mutableListOf<StepState>()
        val matchesBtoA = mutableMapOf<Int, Int>()
        val nextProposalIndex = mutableMapOf<Int, Int>().apply { groupA.forEach { put(it.id, 0) } }
        val freeA = ArrayDeque<Int>().apply { addAll(groupA.map { it.id }) }
        val rankB = groupB.associate { b -> b.id to b.preferences.mapIndexed { rank, aId -> aId to rank }.toMap() }

        steps.add(StepState("Inicializando. Todos los nodos libres.", emptyMap(), ActionType.START))

        while (freeA.isNotEmpty()) {
            val aId = freeA.removeFirst()
            val aPerson = groupA.first { it.id == aId }
            val currentProposalIdx = nextProposalIndex[aId]!!

            if (currentProposalIdx < aPerson.preferences.size) {
                val preferredBId = aPerson.preferences[currentProposalIdx]
                nextProposalIndex[aId] = currentProposalIdx + 1

                steps.add(StepState("El nodo A$aId propone a B$preferredBId...", matchesBtoA.toMap(), ActionType.PROPOSAL))

                if (!matchesBtoA.containsKey(preferredBId)) {
                    matchesBtoA[preferredBId] = aId
                    steps.add(StepState("✓ B$preferredBId acepta a A$aId (Nodo libre).", matchesBtoA.toMap(), ActionType.MATCH))
                } else {
                    val currentPartnerAId = matchesBtoA[preferredBId]!!
                    val bPreferenceRank = rankB[preferredBId] ?: emptyMap()
                    val rankNew = bPreferenceRank[aId] ?: Int.MAX_VALUE
                    val rankCurrent = bPreferenceRank[currentPartnerAId] ?: Int.MAX_VALUE

                    if (rankNew < rankCurrent) {
                        matchesBtoA[preferredBId] = aId
                        freeA.add(currentPartnerAId)
                        steps.add(StepState("⚠ B$preferredBId ROMPE con A$currentPartnerAId y prefiere a A$aId.", matchesBtoA.toMap(), ActionType.SWAP))
                    } else {
                        freeA.add(aId)
                        steps.add(StepState("✗ B$preferredBId RECHAZA a A$aId (prefiere su pareja actual).", matchesBtoA.toMap(), ActionType.REJECTION))
                    }
                }
            }
        }
        steps.add(StepState("CONVERGENCIA ALCANZADA:\nEmparejamiento Óptimo y Estable", matchesBtoA.toMap(), ActionType.COMPLETE))
        return steps
    }
}
