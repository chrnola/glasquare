package cz.destil.glasquare.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import cz.destil.glasquare.R;
import cz.destil.glasquare.adapter.UsersAdapter;
import cz.destil.glasquare.api.Api;
import cz.destil.glasquare.api.Auth;
import cz.destil.glasquare.api.Users;
import cz.destil.glasquare.util.DebugLog;
import cz.destil.glasquare.util.IntentUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity with list of Users.
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public class UsersActivity extends CardScrollActivity {

    public static final String USER_EXTRA = "USER_EXTRA";
    public static final String USER_TO_FIND = "USER_TO_FIND";

    public static void call(Activity activity, String userToSearchFor) {
        Intent intent = new Intent(activity, UsersActivity.class);
        intent.putExtra(USER_TO_FIND, userToSearchFor);
        activity.startActivityForResult(intent, IntentUtils.USER_SEARCH_REQUEST);
    }

    @Override
    protected void loadData() {
        showProgress(R.string.loading);
        searchForUser(getIntent().getStringExtra(USER_TO_FIND));
    }

    private void searchForUser(String user) {

        Callback<Users.UserSearchResponse> callback = new Callback<Users.UserSearchResponse>() {
            @Override
            public void success(Users.UserSearchResponse userSearchResponse, Response response) {
                if(userSearchResponse.getFriends().size() == 0) {
                    showError(R.string.no_friends_found);
                } else {
                    showContent(new UsersAdapter(userSearchResponse.getFriends()), new CardSelectedListener() {
                        @Override
                        public void onCardSelected(Object item) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(USER_EXTRA, (Users.FoursquareUser) item);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                showError(R.string.error_please_try_again);
                DebugLog.e(retrofitError);
            }
        };

        Api.get().create(Users.class).search(Auth.getToken(), user, callback);

    }

}
