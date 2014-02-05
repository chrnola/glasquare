package cz.destil.glasquare.adapter;

/**
 * Adapter for list of Specials
 *
 * @author Chris Pinola (chris@pinola.co)
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.glass.widget.CardScrollAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.destil.glasquare.App;
import cz.destil.glasquare.R;
import cz.destil.glasquare.api.ExploreVenues;

/**
 * Adapter for list of specials.
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public class SpecialsAdapter extends CardScrollAdapter {

    private ArrayList<ExploreVenues.FoursquareSpecial> mSpecials;

    public SpecialsAdapter(ArrayList<ExploreVenues.FoursquareSpecial> specials) {
        mSpecials = specials;
    }

    @Override
    public int getCount() {
        return mSpecials.size();
    }

    @Override
    public Object getItem(int i) {
        return mSpecials.get(i);
    }

    @Override
    public int findIdPosition(Object o) {
        return -1;
    }

    @Override
    public int findItemPosition(Object o) {
        return mSpecials.indexOf(o);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(App.get()).inflate(R.layout.view_special, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(mSpecials.get(i).title);
        holder.description.setText(mSpecials.get(i).description);
        holder.message.setText(mSpecials.get(i).message);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.description)
        TextView description;
        @InjectView(R.id.message)
        TextView message;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

