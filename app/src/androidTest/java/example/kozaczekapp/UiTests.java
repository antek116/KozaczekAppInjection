package example.kozaczekapp;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import example.kozaczekapp.KozaczekItems.Article;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;

/**
 * Class contains Tests for application UI
 */
@RunWith(AndroidJUnit4.class)
public class UiTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void testActivityVisibilityAfterOpenLinkInBrowser() {

        // given
        onView(withId(R.id.refresh))
                .perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.allTasks)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // when
        boolean activityVisibilityState = MainActivity.getActivityVisibilityState();

        // then
        assertFalse(activityVisibilityState);
    }

    @Test
    public void testClickedArticleTitle() {

        // given
        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        int listPosition = 3;
        Article article = activityRule.getActivity().getArticlesFromDB().get(listPosition);
        String title = article.getTitle();

        // when
        ViewInteraction viewInteraction = onView(withId(R.id.allTasks)).perform(
                RecyclerViewActions.actionOnItemAtPosition(listPosition, click()));

        // then
        viewInteraction.check(matches(withText(title)));
    }
}