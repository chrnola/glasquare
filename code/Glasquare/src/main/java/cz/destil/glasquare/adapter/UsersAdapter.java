package cz.destil.glasquare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.widget.CardScrollAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.destil.glasquare.App;
import cz.destil.glasquare.R;
import cz.destil.glasquare.api.Users;

/**
 * Adapter for list of Users
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public class UsersAdapter extends CardScrollAdapter {

    public static int MAX_IMAGE_WIDTH = 213;
    public static int MAX_IMAGE_HEIGHT = 360;
    private List<Users.FoursquareUser> mUsers;

    public UsersAdapter(List<Users.FoursquareUser> users) {
        mUsers = users;
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null) {
            view = LayoutInflater.from(App.get()).inflate(R.layout.view_user, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Users.FoursquareUser user = mUsers.get(i);
        holder.firstName.setText(user.firstName);
        holder.lastName.setText(user.lastName);

        if(user.imageUrl != null) {
            Picasso.with(App.get()).load(user.imageUrl).resize(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT).centerCrop().placeholder(R.drawable
                    .ic_venue_placeholder).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public int findIdPosition(Object o) {
        return -1;
    }

    @Override
    public int findItemPosition(Object o) {
        return mUsers.indexOf(o);
    }

    static class ViewHolder {
        @InjectView(R.id.first_name)
        TextView firstName;
        @InjectView(R.id.last_name)
        TextView lastName;
        @InjectView(R.id.image)
        ImageView image;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
