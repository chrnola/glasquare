package cz.destil.glasquare.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cz.destil.glasquare.adapter.UsersAdapter;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Processor for 4sq users API.
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public interface Users {

    @GET("/users/search?v=" + Api.BUILD_DATE)
    void search(@Query("oauth_token") String token, @Query("name") String name, Callback<UserSearchResponse> callback);

    public static class UserSearchResponse extends Api.FoursquareResponse {
        FoursquareContent response;

        public List<FoursquareUser> getFriends() {
            List<FoursquareUser> users = new ArrayList<FoursquareUser>();

            for(FoursquareUser item : response.results) {

                // Exclude users who are not friends with the current user
                if(item.relationship == null) { continue; }
                if(!item.relationship.equalsIgnoreCase("friend")) { continue; }

                String photo = null;

                if(item.photo != null) {
                    photo = item.photo.prefix + "cap" + UsersAdapter.MAX_IMAGE_HEIGHT + item.photo.suffix;
                }

                users.add(new FoursquareUser(item.id, item.firstName, item.lastName, photo));
            }

            return users;
        }

    }

    class FoursquareContent {
        List<FoursquareUser> results;
    }

    public static class FoursquareUser implements Parcelable {
        public String id;
        public String firstName;
        public String lastName;
        public FoursquarePhoto photo;
        public String imageUrl;
        public String relationship;

        public FoursquareUser(String id, String firstName, String lastName, String imageUrl) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.imageUrl = imageUrl;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(id);
            out.writeString(firstName);
            out.writeString(lastName);
            out.writeString(imageUrl);
        }

        public static final Parcelable.Creator<FoursquareUser> CREATOR = new Parcelable.Creator<FoursquareUser>() {
            public FoursquareUser createFromParcel(Parcel in) {
                return new FoursquareUser(in);
            }

            public FoursquareUser[] newArray(int size) {
                return new FoursquareUser[size];
            }
        };

        private FoursquareUser(Parcel in) {
            id = in.readString();
            firstName = in.readString();
            lastName = in.readString();
            imageUrl = in.readString();
        }

    }

    class FoursquarePhoto {
        public String prefix;
        public String suffix;
    }

}
