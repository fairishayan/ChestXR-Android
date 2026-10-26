package com.example.chestxr.ui.antigravity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Data model representing a celestial or energy body suspended in a zero-gravity environment.
 *
 * @property id Unique identifier for the object.
 * @property name Descriptive title (e.g., "Kepler Cyan", "Solar Flare").
 * @property size Diameter of the spherical object in [Dp].
 * @property initialPosition Normalized relative screen coordinates (x: 0f..1f, y: 0f..1f)
 *                           ensuring responsive placement across various screen ratios.
 * @property color Primary Material 3-compatible core color.
 * @property secondaryColor Darker/complementary shade for 3D spherical depth shading.
 * @property glowColor Luminous color for repulsive flares, halos, and trailing particles.
 * @property driftXRange Max horizontal offset deviation during natural floating drift.
 * @property driftYRange Max vertical offset deviation during natural floating drift.
 * @property driftXDurationMs Cycle duration for horizontal floating motion.
 * @property driftYDurationMs Cycle duration for vertical floating motion.
 * @property rotationDurationMs Duration of a full 360-degree rotation.
 * @property rotationClockwise Direction of planetary spin.
 * @property style Visual archetype: PLANET_WITH_RING, NEBULA_SWIRL, SOLAR_CORE, CRYSTAL_ORB.
 */
data class FloatingObject(
    val id: Int,
    val name: String,
    val size: Dp,
    val initialPosition: Offset,
    val color: Color,
    val secondaryColor: Color = color,
    val glowColor: Color = color,
    val driftXRange: Dp = 24.dp,
    val driftYRange: Dp = 28.dp,
    val driftXDurationMs: Int = 4500,
    val driftYDurationMs: Int = 5200,
    val rotationDurationMs: Int = 18000,
    val rotationClockwise: Boolean = true,
    val style: PlanetStyle = PlanetStyle.PLANET_WITH_RING
)

/**
 * Visual styling archetype for unique planetary rendering.
 */
enum class PlanetStyle {
    PLANET_WITH_RING,
    NEBULA_SWIRL,
    SOLAR_CORE,
    CRYSTAL_ORB
}

/**
 * Pre-configured celestial bodies tuned for organic, asynchronous zero-g motion.
 */
object AntiGravityDefaults {
    fun defaultFloatingObjects(): List<FloatingObject> = listOf(
        FloatingObject(
            id = 1,
            name = "Kepler Cyan",
            size = 110.dp,
            initialPosition = Offset(0.24f, 0.26f),
            color = Color(0xFF00D2FF),         // Electric Cyan
            secondaryColor = Color(0xFF004E92),// Deep Cobalt
            glowColor = Color(0xFF80EAFF),
            driftXRange = 26.dp,
            driftYRange = 32.dp,
            driftXDurationMs = 4300,
            driftYDurationMs = 5700,
            rotationDurationMs = 16000,
            rotationClockwise = true,
            style = PlanetStyle.PLANET_WITH_RING
        ),
        FloatingObject(
            id = 2,
            name = "Nebula Violet",
            size = 85.dp,
            initialPosition = Offset(0.76f, 0.32f),
            color = Color(0xFFB5179E),         // Vivid Purple
            secondaryColor = Color(0xFF3F37C9),// Deep Indigo
            glowColor = Color(0xFFF72585),
            driftXRange = 30.dp,
            driftYRange = 22.dp,
            driftXDurationMs = 3800,
            driftYDurationMs = 4900,
            rotationDurationMs = 22000,
            rotationClockwise = false,
            style = PlanetStyle.NEBULA_SWIRL
        ),
        FloatingObject(
            id = 3,
            name = "Solar Flare",
            size = 125.dp,
            initialPosition = Offset(0.30f, 0.68f),
            color = Color(0xFFFF9E00),         // Amber Gold
            secondaryColor = Color(0xFFD00000),// Crimson Corona
            glowColor = Color(0xFFFFD166),
            driftXRange = 22.dp,
            driftYRange = 38.dp,
            driftXDurationMs = 6100,
            driftYDurationMs = 4200,
            rotationDurationMs = 14000,
            rotationClockwise = true,
            style = PlanetStyle.SOLAR_CORE
        ),
        FloatingObject(
            id = 4,
            name = "Emerald Pulsar",
            size = 92.dp,
            initialPosition = Offset(0.74f, 0.74f),
            color = Color(0xFF06D6A0),         // Neon Mint / Emerald
            secondaryColor = Color(0xFF073B4C),// Dark Abyssal Teal
            glowColor = Color(0xFF90F1D8),
            driftXRange = 28.dp,
            driftYRange = 26.dp,
            driftXDurationMs = 5200,
            driftYDurationMs = 3600,
            rotationDurationMs = 19000,
            rotationClockwise = false,
            style = PlanetStyle.CRYSTAL_ORB
        )
    )
}
