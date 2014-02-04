package cz.destil.glasquare.activity;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;

import cz.destil.glasquare.adapter.SpecialsAdapter;
import cz.destil.glasquare.api.ExploreVenues;

/**
 * Activity with list of specials.
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public class SpecialsActivity extends CardScrollActivity {

    public static final String EXTRA_SPECIALS = "specials";

    public static void call(Activity activity, ArrayList<ExploreVenues.FoursquareSpecial> specials) {
        Intent intent = new Intent(activity, SpecialsActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_SPECIALS, specials);
        activity.startActivity(intent);
    }

    @Override
    protected void loadData() {
        ArrayList<ExploreVenues.FoursquareSpecial> specials = getIntent().getParcelableArrayListExtra(EXTRA_SPECIALS);
        showContent(new SpecialsAdapter(specials), new CardSelectedListener() {

            @Override
            public void onCardSelected(Object item) {
                return;
            }
        });
    }

}
