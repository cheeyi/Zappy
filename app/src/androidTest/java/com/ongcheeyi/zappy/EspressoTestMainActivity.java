package com.ongcheeyi.zappy;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.support.test.espresso.Espresso;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;

/**
 * Created by CheeYi on 7/8/15.
 */
public class EspressoTestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public EspressoTestMainActivity() {
        super(MainActivity.class);
    }


    @SmallTest
    public void testRefresh() {
        Espresso.onView((ViewMatchers.withId(R.id.refreshImageView)))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.locationLabel))
                .check(ViewAssertions.matches(ViewMatchers.withText("Minneapolis")));
    }


    /* Inject an Instrumentation instance into your test class. This step is required in order for
    the Espresso test to run with the AndroidJUnitRunner test runner.*/
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

}
