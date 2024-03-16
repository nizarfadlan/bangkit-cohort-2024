package com.nizarfadlan.aplikasigithubuser.ui.detailsScreen

import android.view.KeyEvent
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nizarfadlan.aplikasigithubuser.R
import com.nizarfadlan.aplikasigithubuser.helpers.EspressoIdlingResource
import com.nizarfadlan.aplikasigithubuser.helpers.TypeSearchView.typeSearchViewText
import com.nizarfadlan.aplikasigithubuser.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailFragmentTest {
    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    private val username = "nizarfadlan"
    private val DELAY_SPLASH = 7000L

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    private fun waitingSplashAndSearch() {
        activityTestRule.scenario.moveToState(Lifecycle.State.RESUMED)
        EspressoIdlingResource.increment()
        Thread.sleep(DELAY_SPLASH)
        EspressoIdlingResource.decrement()

        onView(withId(R.id.rvUsers)).check(matches(isDisplayed()))
        onView(withId(R.id.searchBar)).perform(click())

        onView(withId(R.id.searchView))
            .check(matches(isDisplayed()))
            .perform(click())
            .perform(
                typeSearchViewText(username),
                pressKey(KeyEvent.KEYCODE_ENTER)
            )

        onView(withId(R.id.rvUsers))
            .check(matches(isDisplayed()))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
            )
    }

    @Test
    fun favoriteUser() {
        waitingSplashAndSearch()

        onView(withId(R.id.tvUsername))
            .check(matches(isDisplayed()))
            .check(matches(withText(username)))

        onView(withId(R.id.fabFavorite))
            .check(matches(isDisplayed()))

        setFavoriteUser()
    }

    private fun setFavoriteUser() {
        // Test add to favorite
        onView(withId(R.id.fabFavorite)).perform(click())

        onView(withId(R.id.fabFavorite))
            .check(matches(withContentDescription(R.string.unfavorite)))

        // Test remove from favorite
        onView(withId(R.id.fabFavorite)).perform(click())

        onView(withId(R.id.fabFavorite))
            .check(matches(withContentDescription(R.string.favorite)))
    }
}