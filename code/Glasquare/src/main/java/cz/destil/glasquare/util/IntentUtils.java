package cz.destil.glasquare.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;

import java.util.List;

import cz.destil.glasquare.R;
import cz.destil.glasquare.activity.UsersActivity;
import cz.destil.glasquare.api.Users;

/**
 * Set of intent-related utils.
 *
 * @author David 'Destil' Vavra (david@vavra.me)
 */
public class IntentUtils {

    public static final int SHOUT_SPEECH_REQUEST = 0;
    public static final int TAKE_PICTURE_REQUEST = 1;
    public static final int USER_SEARCH_REQUEST = 2;
    public static final int USER_SPEECH_REQUEST = 3;

    public static enum SpeechRequestType {
        SHOUT,
        MENTION
    }

    public static void launchNavigation(Activity activity, double latitude, double longitude) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("google.navigation:q=" + latitude + "," + longitude));
        activity.startActivity(intent);
    }

    public static void startSpeechRecognition(Activity activity, SpeechRequestType type) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        switch(type) {
            case SHOUT:
                activity.startActivityForResult(intent, SHOUT_SPEECH_REQUEST);
                break;
            case MENTION:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.getString(R.string.voice_prompt_friend_name));
                activity.startActivityForResult(intent, USER_SPEECH_REQUEST);
                break;
        }

    }

    public static String processSpeechRecognitionResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SHOUT_SPEECH_REQUEST || requestCode == USER_SPEECH_REQUEST) && resultCode == Activity.RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
        }
        return null;
    }

    public static void takePicture(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    public static Users.FoursquareUser processSearchForUserResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_SEARCH_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra(UsersActivity.USER_EXTRA)) {
                return (Users.FoursquareUser) data.getParcelableExtra(UsersActivity.USER_EXTRA);
            }
        }

        return null;
    }

}
