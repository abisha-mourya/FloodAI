package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.LocationProvider
import com.example.util.OfflineRoutingEngine
import com.example.util.OfflineTriageEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FloodAid", appName)
    }

    @Test
    fun `test offline triage search`() {
        val waterResults = OfflineTriageEngine.searchGuides("water")
        assertTrue("Expected water results", waterResults.isNotEmpty())

        val snakeResults = OfflineTriageEngine.searchGuides("snake")
        assertTrue("Expected snake results", snakeResults.isNotEmpty())
    }

    @Test
    fun `test distance calculation in km`() {
        val distKm = LocationProvider.calculateDistanceKm(37.7749, -122.4194, 37.7849, -122.4094)
        assertTrue("Distance should be positive", distKm > 0.1)
        assertTrue("Distance should be under 5 km", distKm < 5.0)
    }

    @Test
    fun `test offline road routing and navigation steps`() = kotlinx.coroutines.runBlocking {
        val route = OfflineRoutingEngine.calculateRoute(
            startLat = 37.7749,
            startLon = -122.4194,
            destLat = 37.7850,
            destLon = -122.4100,
            destName = "Central Emergency Hospital"
        )
        assertNotNull("Route should not be null", route)
        assertTrue("Route should have steps", route.steps.isNotEmpty())
        assertTrue("Route should have waypoints", route.waypoints.size >= 2)
        assertTrue("Distance should be > 0", route.totalDistanceKm > 0.0)
        assertEquals("Central Emergency Hospital", route.destinationName)
    }

    @Test
    fun `test offline road graph routing generates multiple road waypoints and turns`() {
        val route = OfflineRoutingEngine.calculateOfflineGraphRoute(
            startLat = 37.7749,
            startLon = -122.4194,
            destLat = 37.7850,
            destLon = -122.4100,
            destName = "Emergency Clinic"
        )
        assertNotNull("Offline route should not be null", route)
        assertTrue("Offline route should have multiple waypoints along roads", route.waypoints.size >= 3)
        assertTrue("Offline route should have navigation steps", route.steps.isNotEmpty())
    }

    @Test
    fun `test offline disaster LLM answers basic doubts without network`() {
        val waterQuery = com.example.util.OfflineDisasterLLM.ask("Can I drink flood water if I boil it?")
        assertNotNull("Response should not be null", waterQuery)
        assertTrue("Should contain boiling instructions", waterQuery.actionSteps.any { it.contains("BOIL", ignoreCase = true) })
        assertTrue("Should have fatal warnings", waterQuery.fatalWarnings.isNotEmpty())

        val snakeQuery = com.example.util.OfflineDisasterLLM.ask("Snake bite in water")
        assertNotNull(snakeQuery)
        assertTrue("Should mention ASV or anti-snake venom", snakeQuery.actionSteps.any { it.contains("Venom", ignoreCase = true) || it.contains("ASV", ignoreCase = true) })

        val cprQuery = com.example.util.OfflineDisasterLLM.ask("How to do CPR on drowned person")
        assertNotNull(cprQuery)
        assertTrue("Should mention chest compressions", cprQuery.actionSteps.any { it.contains("compressions", ignoreCase = true) })
    }
}
