package cz.destil.glasquare.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cz.destil.glasquare.adapter.VenuesAdapter;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Processor for 4sq venues/explore API.
 *
 * @author David 'Destil' Vavra (david@vavra.me)
 */
public interface ExploreVenues {

    public static int LIMIT_VENUES = 10;

    @GET("/venues/explore?openNow=1&sortByDistance=1&venuePhotos=1&limit=" + LIMIT_VENUES)
    void best(@Query("ll") String ll, Callback<ExploreVenuesResponse> callback);

    @GET("/venues/explore?sortByDistance=1&venuePhotos=1&limit=" + LIMIT_VENUES)
    void search(@Query("ll") String ll, @Query("query") String query, Callback<ExploreVenuesResponse> callback);

    public static class ExploreVenuesResponse extends Api.FoursquareResponse {
        FoursquareContent response;

        public List<Venue> getVenues() {
            List<Venue> venues = new ArrayList<Venue>();
            for (FoursquareItem group : response.groups.get(0).items) {
                String photo = null;
                ArrayList<FoursquareSpecial> specials = new ArrayList<FoursquareSpecial>();
                if (group.venue.photos.groups.size() > 0) {
                    FoursquarePhotoGroupItem item = group.venue.photos.groups.get(0).items.get(0);
                    photo = item.prefix + "cap" + VenuesAdapter.MAX_IMAGE_HEIGHT + item.suffix;
                }
                boolean hasTips = (group.tips != null && group.tips.size() > 0);
                String hours = (group.venue.hours != null) ? group.venue.hours.status : null;
                String category = "";
                if (group.venue.categories.size() > 0) {
                    category = group.venue.categories.get(0).name;
                }
                if(group.venue.specials.count > 0){
                    specials.addAll(group.venue.specials.items);
                }
                venues.add(new Venue(group.venue.name, category, photo, group.venue.location.distance,
                        group.venue.location.lat, group.venue.location.lng, hours, group.venue.id, hasTips, specials));
            }
            return venues;
        }
    }

    public static class Venue {
        public String id;
        public String name;
        public String category;
        public String imageUrl;
        public int distance;
        public double latitude;
        public double longitude;
        public String hours;
        public boolean hasTips;
        public ArrayList<FoursquareSpecial> specials;

        public Venue(String name, String category, String imageUrl, int distance, double latitude, double longitude, String hours, String id,
                     boolean hasTips, ArrayList<FoursquareSpecial> specials) {
            this.name = name;
            this.category = category;
            this.imageUrl = imageUrl;
            this.distance = distance;
            this.latitude = latitude;
            this.longitude = longitude;
            this.hours = hours;
            this.id = id;
            this.hasTips = hasTips;
            this.specials = specials;
        }

        public boolean hasSpecials() {
            return !specials.isEmpty();
        }

        @Override
        public String toString() {
            return "Venue{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", category='" + category + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", distance=" + distance +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", hours='" + hours + '\'' +
                    ", hasTips=" + hasTips +
                    '}';
        }
    }

    // parsing classes:

    class FoursquareContent {
        List<FoursquareGroup> groups;
    }

    class FoursquareGroup {
        List<FoursquareItem> items;
    }

    class FoursquareItem {
        FoursquareVenue venue;
        List<FoursquareTip> tips;
    }

    class FoursquareTip {

    }

    class FoursquareVenue {
        String id;
        String name;
        List<FoursquareCategory> categories;
        FoursquarePhotos photos;
        FoursquareLocation location;
        FoursquareHours hours;
        FoursquareSpecials specials;
    }

    class FoursquareHours {
        String status;
    }

    class FoursquareLocation {
        int distance;
        double lat;
        double lng;
    }

    class FoursquareCategory {
        String name;
    }

    class FoursquarePhotos {
        List<FoursquarePhotoGroup> groups;
    }

    class FoursquarePhotoGroup {
        List<FoursquarePhotoGroupItem> items;
    }

    class FoursquarePhotoGroupItem {
        String prefix;
        String suffix;
    }

    class FoursquareSpecials {
        int count;
        List<FoursquareSpecial> items;
    }

    class FoursquareSpecial implements Parcelable {
        public String title;
        public String message;
        public String description;
        public String icon;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(message);
            dest.writeString(description);
            dest.writeString(icon);
        }

        public static final Creator<FoursquareSpecial> CREATOR = new Creator<FoursquareSpecial>() {
            @Override
            public FoursquareSpecial createFromParcel(Parcel source) {
                return new FoursquareSpecial(source);
            }

            @Override
            public FoursquareSpecial[] newArray(int size) {
                return new FoursquareSpecial[size];
            }
        };

        private FoursquareSpecial(Parcel in) {
            this.title = in.readString();
            this.message = in.readString();
            this.description = in.readString();
            this.icon = in.readString();
        }

        public boolean hasIcon()
        {
            return (this.icon != null) && !this.icon.isEmpty();
        }

        public String getFullUrl()
        {
            return "http://foursquare.com/img/specials/" + this.icon + ".png";
        }

    }
}
