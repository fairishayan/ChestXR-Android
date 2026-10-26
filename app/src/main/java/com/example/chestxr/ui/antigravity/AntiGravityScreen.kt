package com.example.chestxr.ui.antigravity

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * Main Screen demonstrating a zero-gravity simulation with interactive floating elements.
 *
 * Key Features:
 * 1. Deep cosmic gradient space background with procedural twinkling stars and nebulae.
 * 2. Continuous asynchronous drift (X, Y) and rotation using [rememberInfiniteTransition].
 * 3. Physics-inspired magnetic repulsion upon touch or drag using [Modifier.pointerInput].
 * 4. Shockwave ripples, trailing motion glows, and 3D spherical rendering with lighting.
 * 5. Material 3 TopAppBar and zero-g agitation controls.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntiGravityScreen(
    modifier: Modifier = Modifier,
    floatingObjects: List<FloatingObject> = remember { AntiGravityDefaults.defaultFloatingObjects() }
) {
    // Trigger count to shake or agitate all bodies via a cosmic gravity wave
    var gravityAgitationTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E294B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF64D2FF),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Anti-Gravity Playground",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Zero-G Simulation • Touch to Repel",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF8E9CB8)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { gravityAgitationTrigger++ }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.RestartAlt,
                            contentDescription = "Reset / Pulse Field",
                            tint = Color(0xFF90CAF9)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xE6050918),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Layer 1: Space Backdrop with procedural twinkling stars and distant nebulae
            SpaceBackground(modifier = Modifier.fillMaxSize())

            // Layer 2: Interactive Floating Bodies in Zero Gravity
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val screenWidthPx = constraints.maxWidth.toFloat()
                val screenHeightPx = constraints.maxHeight.toFloat()
                val density = LocalDensity.current

                floatingObjects.forEach { celestialBody ->
                    FloatingPlanetItem(
                        floatingObject = celestialBody,
                        screenWidthPx = screenWidthPx,
                        screenHeightPx = screenHeightPx,
                        agitationTrigger = gravityAgitationTrigger
                    )
                }
            }

            // Layer 3: Glassmorphic Control HUD at bottom
            AntiGravityBottomHud(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                onPulseGravity = { gravityAgitationTrigger++ }
            )
        }
    }
}

/**
 * Individual celestial floating object handling continuous zero-g physics,
 * continuous independent drift, rotation, and touch-induced magnetic repulsion.
 */
@Composable
private fun FloatingPlanetItem(
    floatingObject: FloatingObject,
    screenWidthPx: Float,
    screenHeightPx: Float,
    agitationTrigger: Int,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val sizePx = with(density) { floatingObject.size.toPx() }
    val coroutineScope = rememberCoroutineScope()

    // -------------------------------------------------------------
    // 1. Natural Zero-G Continuous Float & Rotation
    // -------------------------------------------------------------
    val infiniteTransition = rememberInfiniteTransition(
        label = "OrbitTransition_${floatingObject.id}"
    )

    // Asynchronous horizontal drift
    val driftX by infiniteTransition.animateFloat(
        initialValue = -with(density) { floatingObject.driftXRange.toPx() },
        targetValue = with(density) { floatingObject.driftXRange.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = floatingObject.driftXDurationMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DriftX_${floatingObject.id}"
    )

    // Asynchronous vertical drift with different period for organic 2D Lissajous curve
    val driftY by infiniteTransition.animateFloat(
        initialValue = -with(density) { floatingObject.driftYRange.toPx() },
        targetValue = with(density) { floatingObject.driftYRange.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = floatingObject.driftYDurationMs,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DriftY_${floatingObject.id}"
    )

    // Subtle continuous rotation to simulate celestial planetary spin
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = if (floatingObject.rotationClockwise) 0f else 360f,
        targetValue = if (floatingObject.rotationClockwise) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = floatingObject.rotationDurationMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation_${floatingObject.id}"
    )

    // -------------------------------------------------------------
    // 2. Touch Physics: Magnetic Repulsion & Zero-Gravity Return
    // -------------------------------------------------------------
    // Animatable offset driven by user touch repulsion and returning to (0,0) equilibrium
    val repelOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    // Glow and shockwave visual states
    val glowAlpha = remember { Animatable(0f) }
    val shockwaveScale = remember { Animatable(1f) }

    // Helper to fire a radiant shockwave and glow burst
    fun triggerVisualGlow() {
        coroutineScope.launch {
            glowAlpha.snapTo(1f)
            shockwaveScale.snapTo(1f)
            launch {
                glowAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 850, easing = LinearOutSlowInEasing)
                )
            }
            launch {
                shockwaveScale.animateTo(
                    targetValue = 2.2f,
                    animationSpec = tween(durationMillis = 850, easing = LinearOutSlowInEasing)
                )
            }
        }
    }

    // React to global gravity wave pulses from the TopAppBar / HUD
    LaunchedEffect(agitationTrigger) {
        if (agitationTrigger > 0) {
            triggerVisualGlow()
            // Disperse outward randomly
            val angle = (floatingObject.id * 1.57f) + (agitationTrigger * 0.7f)
            val burstDistance = 140f
            val burstVector = Offset(cos(angle) * burstDistance, sin(angle) * burstDistance)

            launch {
                repelOffset.animateTo(
                    targetValue = burstVector,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                // Slowly float back into orbit
                repelOffset.animateTo(
                    targetValue = Offset.Zero,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
            }
        }
    }

    // Initial base center position in pixels
    val baseX = screenWidthPx * floatingObject.initialPosition.x - (sizePx / 2f)
    val baseY = screenHeightPx * floatingObject.initialPosition.y - (sizePx / 2f)

    Box(
        modifier = modifier
            // Use layout offset lambda to prevent unnecessary recompositions on animation ticks
            .offset {
                IntOffset(
                    x = (baseX + driftX + repelOffset.value.x).roundToInt(),
                    y = (baseY + driftY + repelOffset.value.y).roundToInt()
                )
            }
            .size(floatingObject.size)
            // Gesture handling for magnetic repulsion & drag deflection
            .pointerInput(floatingObject.id) {
                awaitPointerEventScope {
                    while (true) {
                        // 1. Detect touch down event
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val downPos = down.position
                        val centerPx = Offset(sizePx / 2f, sizePx / 2f)

                        // Vector from touch to center:
                        // Touching the top pushes downward, touching left pushes rightward (Magnetic Repulsion)
                        val fromTouchToCenter = centerPx - downPos
                        val distance = fromTouchToCenter.getDistance()

                        val repelDirection = if (distance > 1f) {
                            fromTouchToCenter / distance
                        } else {
                            // Dead-center touch: deflect upward/sideward
                            Offset(0f, -1f)
                        }

                        // Repulsion impulse magnitude proportional to object size
                        val impulseMagnitude = (sizePx * 1.25f).coerceIn(120f, 320f)
                        var currentDisplacement = repelDirection * impulseMagnitude

                        // Trigger shockwave & glow flare
                        triggerVisualGlow()

                        // Immediately launch animated magnetic deflection
                        coroutineScope.launch {
                            repelOffset.animateTo(
                                targetValue = currentDisplacement,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            )
                        }

                        // 2. Track drag stream if user drags finger across the screen
                        var lastPos = downPos
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.find { it.id == down.id } ?: break
                            if (!change.pressed) break // Touch released

                            if (change.positionChanged()) {
                                val dragDelta = change.position - lastPos
                                lastPos = change.position

                                // Object dynamically pushes away along finger movement
                                currentDisplacement += dragDelta * 0.85f

                                // Elastic resistance limit to keep within reasonable viewport
                                val maxDisplacement = 450f
                                if (currentDisplacement.getDistance() > maxDisplacement) {
                                    currentDisplacement = (currentDisplacement / currentDisplacement.getDistance()) * maxDisplacement
                                }

                                val targetSnap = currentDisplacement
                                coroutineScope.launch {
                                    repelOffset.snapTo(targetSnap)
                                }
                                change.consume()
                            }
                        }

                        // 3. User released: glide gently back to equilibrium with low-stiffness zero-g spring
                        coroutineScope.launch {
                            repelOffset.animateTo(
                                targetValue = Offset.Zero,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessVeryLow
                                )
                            )
                        }
                    }
                }
            }
    ) {
        // Render 3D shaded celestial body, trails, glow rings, and surface features
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            // -------------------------------------------------------------
            // A. Motion Trail Stream (visible during repulsive movement)
            // -------------------------------------------------------------
            val repelMagnitude = repelOffset.value.getDistance()
            if (repelMagnitude > 4f) {
                val trailUnit = -repelOffset.value / repelMagnitude
                val trailColor = floatingObject.glowColor

                // Trailing luminous ghost particles behind the object's travel direction
                val trailSteps = 3
                for (i in 1..trailSteps) {
                    val progress = i.toFloat() / trailSteps
                    val trailPos = center + (trailUnit * (progress * (radius * 0.9f)))
                    val trailRadius = radius * (1f - (progress * 0.35f))
                    val trailAlpha = (0.28f * (1f - progress) * (repelMagnitude / 200f).coerceIn(0f, 1f))

                    drawCircle(
                        color = trailColor.copy(alpha = trailAlpha),
                        radius = trailRadius,
                        center = trailPos
                    )
                }
            }

            // -------------------------------------------------------------
            // B. Expanding Shockwave Ring (fired upon tap)
            // -------------------------------------------------------------
            if (glowAlpha.value > 0.01f) {
                val waveRadius = radius * shockwaveScale.value
                drawCircle(
                    color = floatingObject.glowColor.copy(alpha = glowAlpha.value * 0.75f),
                    radius = waveRadius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Secondary soft expanding halo
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            floatingObject.glowColor.copy(alpha = glowAlpha.value * 0.5f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = waveRadius * 1.15f
                    ),
                    radius = waveRadius * 1.15f,
                    center = center
                )
            }

            // -------------------------------------------------------------
            // C. Ambient Zero-G Halo
            // -------------------------------------------------------------
            val ambientAuraAlpha = 0.22f + (glowAlpha.value * 0.45f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        floatingObject.glowColor.copy(alpha = ambientAuraAlpha),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.35f
                ),
                radius = radius * 1.35f,
                center = center
            )

            // -------------------------------------------------------------
            // D. 3D Spherical Planet Body
            // -------------------------------------------------------------
            // Specular light source offset in the upper-left (0.35, 0.32)
            val lightSource = Offset(size.width * 0.35f, size.height * 0.32f)

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.85f),
                        floatingObject.color,
                        floatingObject.secondaryColor,
                        Color(0xFF030712)
                    ),
                    center = lightSource,
                    radius = radius * 1.25f
                ),
                radius = radius,
                center = center
            )

            // -------------------------------------------------------------
            // E. Unique Visual Archetype Details (Rings, Swirls, Flares)
            // -------------------------------------------------------------
            rotate(degrees = rotationAngle, pivot = center) {
                when (floatingObject.style) {
                    PlanetStyle.PLANET_WITH_RING -> {
                        // Planetary Saturn-like Ring
                        val ringWidth = radius * 2.7f
                        val ringHeight = radius * 0.65f
                        drawOval(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    floatingObject.glowColor.copy(alpha = 0.85f),
                                    Color.White.copy(alpha = 0.9f),
                                    floatingObject.color.copy(alpha = 0.3f),
                                    floatingObject.glowColor.copy(alpha = 0.85f)
                                )
                            ),
                            topLeft = Offset(center.x - ringWidth / 2f, center.y - ringHeight / 2f),
                            size = Size(ringWidth, ringHeight),
                            style = Stroke(width = 3.5.dp.toPx())
                        )
                    }

                    PlanetStyle.NEBULA_SWIRL -> {
                        // Cosmic swirling bands
                        drawArc(
                            color = floatingObject.glowColor.copy(alpha = 0.65f),
                            startAngle = 45f,
                            sweepAngle = 180f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius * 0.7f, center.y - radius * 0.7f),
                            size = Size(radius * 1.4f, radius * 1.4f),
                            style = Stroke(width = 2.5.dp.toPx())
                        )
                        drawArc(
                            color = Color.White.copy(alpha = 0.5f),
                            startAngle = 225f,
                            sweepAngle = 140f,
                            useCenter = false,
                            topLeft = Offset(center.x - radius * 0.45f, center.y - radius * 0.45f),
                            size = Size(radius * 0.9f, radius * 0.9f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }

                    PlanetStyle.SOLAR_CORE -> {
                        // Solar flare coronal jets
                        val rayCount = 8
                        for (i in 0 until rayCount) {
                            val rayAngle = (i * (360f / rayCount)) * (Math.PI / 180f).toFloat()
                            val innerPoint = center + Offset(cos(rayAngle) * radius * 0.85f, sin(rayAngle) * radius * 0.85f)
                            val outerPoint = center + Offset(cos(rayAngle) * radius * 1.15f, sin(rayAngle) * radius * 1.15f)

                            drawLine(
                                color = floatingObject.glowColor.copy(alpha = 0.75f),
                                start = innerPoint,
                                end = outerPoint,
                                strokeWidth = 2.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    PlanetStyle.CRYSTAL_ORB -> {
                        // Concentric orbital rings and geometric highlights
                        drawCircle(
                            color = Color.White.copy(alpha = 0.35f),
                            radius = radius * 0.65f,
                            center = center,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                        drawCircle(
                            color = floatingObject.glowColor.copy(alpha = 0.5f),
                            radius = radius * 0.4f,
                            center = center,
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }
            }

            // -------------------------------------------------------------
            // F. Specular Bubble Reflection Dot
            // -------------------------------------------------------------
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = radius * 0.12f,
                center = Offset(center.x - radius * 0.45f, center.y - radius * 0.45f)
            )
        }
    }
}

/**
 * Deep space background featuring a dark blue/black gradient, subtle nebula dust,
 * and procedural twinkling stars.
 */
@Composable
private fun SpaceBackground(modifier: Modifier = Modifier) {
    // Generate a fixed set of cosmic stars
    val stars = remember {
        val random = Random(42) // Fixed seed for stable star map across recompositions
        List(70) {
            StarSpec(
                xPercent = random.nextFloat(),
                yPercent = random.nextFloat(),
                baseRadius = random.nextFloat() * 1.8f + 0.8f,
                twinkleSpeed = random.nextInt(1800, 4200),
                color = when (random.nextInt(4)) {
                    0 -> Color(0xFFE2E8F0) // Pure Starlight White
                    1 -> Color(0xFF93C5FD) // Pale Cosmic Blue
                    2 -> Color(0xFFFDE68A) // Soft Golden Star
                    else -> Color(0xFFC4B5FD) // Violet Dwarf
                }
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "StarTwinkle")
    val twinklePulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlobalTwinkle"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030712), // Abyssal space black
                        Color(0xFF070E24), // Midnight cosmic navy
                        Color(0xFF0C1434), // Deep indigo
                        Color(0xFF04060E)  // Deep bottom void
                    )
                )
            )
    ) {
        val width = size.width
        val height = size.height

        // 1. Distant ambient nebulae clouds
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x1A4F46E5), Color.Transparent),
                center = Offset(width * 0.2f, height * 0.25f),
                radius = width * 0.6f
            ),
            radius = width * 0.6f,
            center = Offset(width * 0.2f, height * 0.25f)
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x1806B6D4), Color.Transparent),
                center = Offset(width * 0.85f, height * 0.7f),
                radius = width * 0.7f
            ),
            radius = width * 0.7f,
            center = Offset(width * 0.85f, height * 0.7f)
        )

        // 2. Procedural twinkling stars
        stars.forEachIndexed { index, star ->
            val starAlpha = if (index % 2 == 0) twinklePulse else (1.4f - twinklePulse)
            drawCircle(
                color = star.color.copy(alpha = (starAlpha * 0.8f).coerceIn(0.2f, 1f)),
                radius = star.baseRadius.dp.toPx(),
                center = Offset(star.xPercent * width, star.yPercent * height)
            )
        }
    }
}

/**
 * Bottom Glassmorphic Control Card showing simulation feedback and a pulse trigger.
 */
@Composable
private fun AntiGravityBottomHud(
    modifier: Modifier = Modifier,
    onPulseGravity: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x8C0B1229))
            .border(1.dp, Color(0x3364D2FF), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                    )
                    Text(
                        text = "ZERO-GRAVITY FIELD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = Color(0xFF64D2FF)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tap or drag orbs to test magnetic repulsion",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD0D7E5)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            ElevatedButton(
                onClick = onPulseGravity,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFF1E294B),
                    contentColor = Color(0xFF64D2FF)
                ),
                contentPadding = ButtonDefaults.ContentPadding
            ) {
                Icon(
                    imageVector = Icons.Rounded.Waves,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Pulse",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

/**
 * Spec for individual procedural stars.
 */
private data class StarSpec(
    val xPercent: Float,
    val yPercent: Float,
    val baseRadius: Float,
    val twinkleSpeed: Int,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun AntiGravityScreenPreview() {
    AntiGravityScreen()
}
