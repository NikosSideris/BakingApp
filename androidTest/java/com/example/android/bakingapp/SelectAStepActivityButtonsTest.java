package com.example.android.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/*
Should pass in all other cases but fail for tablet && landscape
*/

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SelectAStepActivityButtonsTest {

    @Rule
    public ActivityTestRule<SelectARecipeActivity> mActivityTestRule = new ActivityTestRule<>(SelectARecipeActivity.class);

    @Test
    public void selectARecipeActivityButtonsTest() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.rv_select_recipe), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(allOf(withId(R.id.rv_select_step), childAtPosition(withId(R.id.fragment_select_a_step_container), 0)));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView = onView(allOf(withId(R.id.tv_step_long_description), withText("1. Preheat the oven to 350F. Butter a 9\" deep dish pie pan."), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 0), isDisplayed()));
        textView.check(matches(withText("1. Preheat the oven to 350F. Butter a 9\" deep dish pie pan.")));

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.bu_previous), withText("Previous Step"), childAtPosition(allOf(withId(R.id.ll_buttons), childAtPosition(withClassName(is("android.widget.LinearLayout")), 2)), 0), isDisplayed()));
        appCompatButton.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView2 = onView(allOf(withId(R.id.tv_step_long_description), withText("Recipe Introduction"), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 0), isDisplayed()));
        textView2.check(matches(withText("Recipe Introduction")));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.bu_next), withText("Next Step"), childAtPosition(allOf(withId(R.id.ll_buttons), childAtPosition(withClassName(is("android.widget.LinearLayout")), 2)), 1), isDisplayed()));
        appCompatButton2.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.bu_next), withText("Next Step"), childAtPosition(allOf(withId(R.id.ll_buttons), childAtPosition(withClassName(is("android.widget.LinearLayout")), 2)), 1), isDisplayed()));
        appCompatButton3.perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView3 = onView(allOf(withId(R.id.tv_step_long_description), withText("2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed."), childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1), 0), isDisplayed()));
        textView3.check(matches(withText("2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.")));

    }
    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
