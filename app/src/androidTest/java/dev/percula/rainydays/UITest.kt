package dev.percula.rainydays

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import dev.percula.rainydays.model.Location
import dev.percula.rainydays.ui.App
import dev.percula.rainydays.ui.BaseViewHolder
import dev.percula.rainydays.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITest {

    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    /**
     * Clear the local database before running the test
     */
    @Before
    fun before() {
        ApplicationProvider.getApplicationContext<App>().deleteDatabase("local-database")
    }

    /**
     * This test adds locations to the list and checks the first location to make sure it matches the expected location.
     */
    @Test
    fun add_location() {
        tapFAB()

        // Get current location
        val myLocation = ViewMatchers.withText(R.string.location_picker_samples)
        Espresso.onView(myLocation).perform(ViewActions.click())
        waitFor(1000)

        // Scroll to first position
        onView(ViewMatchers.withId(R.id.recyclerview))
            .perform(RecyclerViewActions.scrollToPosition<BaseViewHolder<Location>>(0))

        onView(ViewMatchers.withText("MINNEAPOLIS/ST PAUL AP")).check(ViewAssertions.matches(ViewMatchers.withText("MINNEAPOLIS/ST PAUL AP")))
    }

    fun tapFAB() {
        val view = ViewMatchers.withId(R.id.fab)
        Espresso.onView(view).perform(ViewActions.click())
        waitFor(500)
    }

}