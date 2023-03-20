package com.james.submissiononefundamentalandroiddicoding

import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class SearchTest {

    private val searchDummy = "jamesfangyauw"

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun searchApp() {
        val searchApp = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.james.submissiononefundamentalandroiddicoding", searchApp.packageName)
        onView(withId(R.id.search)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText(searchDummy), pressImeActionButton())

    }
}