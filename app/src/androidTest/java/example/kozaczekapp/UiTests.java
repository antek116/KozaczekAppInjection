package example.kozaczekapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static example.kozaczekapp.TestUtils.atPosition;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
/*
    @Test
    public void testRecyclerViewDBCompatibility() {

        // given
        ArrayList<Article> articles = activityRule.getActivity().getArticlesFromDB();
        if (articles != null) {
            int listPosition = 3;
            String title = articles.get(listPosition).getTitle();

            // when
            ViewInteraction viewInteraction = onView(withId(R.id.allTasks)).
                    perform(scrollToPosition(listPosition));
            // then
            viewInteraction.check(matches(atPosition(listPosition, hasDescendant(withText(title)))));
        }
    }*/

    @Test
    public void testIfServiceOnlyOnceStarted() {
        boolean isStartedServiceNumberTrue = (activityRule.getActivity().startingServiceCounter == 1);
        assertTrue(isStartedServiceNumberTrue);
    }

    @Test
    public void testIfServiceNotStartedWhenOrientationChanged() {
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        boolean isStartedRightNumberOfServices = (activityRule.getActivity().startingServiceCounter == 1);
        assertTrue(isStartedRightNumberOfServices);
    }
    @Test
    public void testCheckIfFragmentIsSameAfterRotation() {
        //given
        String fragmentString = activityRule.getActivity().listArticle.toString();
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        String fragmentStringAfterRotation = activityRule.getActivity().listArticle.toString();
        //when
        boolean isSame = fragmentString.equals(fragmentStringAfterRotation);
        //then
        assertTrue("After rotation Fragment should be the same as before",isSame);
    }

    @Test
    public void testButtonIsClickableAfterPullToRefresh(){
        //given
        onView(withId(R.id.allTasks)).perform(swipeDown());
        //when
        boolean isClickable = activityRule.getActivity().image.isClickable();
        //then
        assertFalse("Button should't be clickable after pull to refresh", isClickable);
    }

    @Test
    public void testIfButtonIsAnimatingAfterClick(){
        //given
        onView(withId(R.id.refresh)).perform(click());
        //when
        boolean isAnimating = activityRule.getActivity().isRefreshAnimating();
        //then
        assertTrue("Button animation should be running",isAnimating);
    }
}