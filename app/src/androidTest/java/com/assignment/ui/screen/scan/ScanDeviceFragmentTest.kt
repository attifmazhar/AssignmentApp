package com.assignment.ui.screen.scan

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.assignment.ui.scan.ScanDeviceFragment
import dagger.hilt.android.AndroidEntryPoint
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@AndroidEntryPoint
@RunWith(AndroidJUnit4::class)
@LargeTest
class ScanDeviceFragmentTest {

    @Test
    fun testNavigationToSecondScreen() {
        // Create a mock NavController
        val mockNavController = mock(NavController::class.java)

        // Create a graphical FragmentScenario for the FirstScreen
        val firstScenario = launchFragmentInContainer<ScanDeviceFragment>()

        // Set the NavController property on the fragment
        firstScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Verify that performing a click prompts the correct Navigation action
        onView(ViewMatchers.withId(com.assignment.R.id.btnScan)).perform(ViewActions.click())
        verify(mockNavController).navigate(com.assignment.R.id.toDeviceInfoList)
    }
}