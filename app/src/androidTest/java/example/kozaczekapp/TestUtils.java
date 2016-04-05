package example.kozaczekapp;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import example.kozaczekapp.Fragments.ArticleListAdapter;

import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;

public class TestUtils {

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                ArticleListAdapter.MyViewHolder viewHolder = (ArticleListAdapter.MyViewHolder) view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                boolean isMatching = itemMatcher.matches(viewHolder.itemView);
                return isMatching;
            }
        };
    }
}
