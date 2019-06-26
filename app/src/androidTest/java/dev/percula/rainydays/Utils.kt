package dev.percula.rainydays

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.uiautomator.UiDevice
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Testing utility methods (I copy/pasted this from another project of mine)
 */

/**
 * Rotates the testing device from it's current orientation and back
 */
fun rotateBackAndForth() {
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.setOrientationLeft()
    waitFor(500)
    device.setOrientationNatural()
    waitFor(500)
}

/**
 * Gets the root view and waits for a specific time.
 * @param millis Milliseconds to wait
 */
fun waitFor(millis: Long) {
    onView(isRoot()).perform(actionWaitFor(millis))
}

fun waitFor(millis: Long, maxRetries: Int, until: () -> Boolean) {
    if (maxRetries == -1 || until()) return
    waitFor(millis)
    waitFor(millis, maxRetries - 1, until)
}

/**
 * Creates an action that waits for a specific time.
 * @param millis Milliseconds to wait
 */
fun actionWaitFor(millis: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "Wait for $millis milliseconds."
        }

        override fun perform(uiController: UiController, view: View) {
            uiController.loopMainThreadForAtLeast(millis)
        }
    }
}

/**
 * Returns a matcher for the first item, if multiple items are matched
 * @param matcher Matcher to return first item of
 */
fun <T> first(matcher: Matcher<T>): Matcher<T> {
    return object : BaseMatcher<T>() {
        internal var isFirst = true

        override fun matches(item: Any): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }

            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("should return first matching item")
        }
    }
}

/**
 * Gets the current test context
 */
fun getContext(): Context {
    return InstrumentationRegistry.getTargetContext()
}

/**
 * Gets the string for the provided resource id
 * @param id String resource id
 */
fun getString(id: Int): String {
    val targetContext = InstrumentationRegistry.getTargetContext()
    return targetContext.getString(id)
}

/**
 * Gets the string for the provided resource id
 * @param id String resource id
 * @param params Parameters for the formattable string
 */
fun getString(id: Int, vararg params: String): String {
    val targetContext = InstrumentationRegistry.getTargetContext()
    return targetContext.getString(id, *params)
}

/**
 * Gets the string for the provided resource id
 * @param id String resource id
 * @param paramResIds Resource Ids for parameters for the formattable string
 */
fun getString(id: Int, vararg paramResIds: Int): String {
    val params: Array<String> = paramResIds.map { getString(it) }.toTypedArray()
    return getString(id, *params)
}

/**
 * Gets the text from a Matcher
 */
fun ViewInteraction.getText(): String {
    val stringHolder = arrayOf("")
    this.perform(object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextView::class.java)
        }

        override fun getDescription(): String {
            return "getting text from a TextView"
        }

        override fun perform(uiController: UiController, view: View) {
            (view as? TextView)?.text?.toString()?.let { stringHolder[0] = it }
        }
    })
    return stringHolder[0]
}