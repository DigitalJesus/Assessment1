package com.example.assessment1;

import android.support.design.widget.AppBarLayout;
import android.support.test.InstrumentationRegistry;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Time;
import java.util.Random;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

   @Rule
   public ActivityTestRule<MainActivity> activityActivityTestRule1 = new ActivityTestRule<>(MainActivity.class);


   @Test
   public void testChangeClass(){
      openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
      onView(withText("Change Student ID")).perform(click());

      onView(withId(R.id.btnAdminLogin)).perform(click());

      onView(withId(R.id.adminInput)).perform(typeText("password"));
      Espresso.closeSoftKeyboard();
      onView(withId(R.id.adminLoginButton)).perform(click());

      onData(anything()).inAdapterView(withId(R.id.listView_admin_editor)).atPosition(0).perform(click());

      onView(withId(R.id.mon_hr10)).perform(click());

      onView(withId(R.id.className)).perform(clearText()).perform(click()).perform(typeText("unique!"));
      Espresso.closeSoftKeyboard();

      onView(withId(R.id.saveBtn)).perform(click());

      onView(withId(R.id.mon_hr10)).check(matches(withText(containsString("unique!"))));

      onView(withId(R.id.mon_hr10)).perform(click());

      onView(withId(R.id.className)).perform(clearText()).perform(click()).perform(typeText("Networking"));
      Espresso.closeSoftKeyboard();

      onView(withId(R.id.saveBtn)).perform(click());
   }

   @Test
   public void testGetNextClass(){

   }


   @Before
   public void setUp() throws Exception {
   }

   @After
   public void tearDown() throws Exception {
   }
}