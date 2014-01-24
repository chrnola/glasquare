package cz.destil.glasquare.util;

import java.util.ArrayList;

import cz.destil.glasquare.api.Users;

/**
 * One-time-use class for creating a well-formed 'mentions' string.
 * Must inject shout message and list of Friends each time.
 *
 * @author Chris Pinola (chris@pinola.co)
 */
public class PrepareMention{

    private final String WITH_MESSAGE = " - with ";
    private final String COMMA = ",";
    private final String SEMICOLON = ";";
    private final String USER_SPACER = ", ";

    private ArrayList<Users.FoursquareUser> mFriends;
    private ArrayList<Mention> mMentions;

    private String mShout;
    private String mFinalShout;
    private String mFinalMentions;

    public PrepareMention(String shoutMessage, ArrayList<Users.FoursquareUser> friends) {
        mShout = shoutMessage;
        mFriends = friends;

        mMentions = new ArrayList<Mention>();

        mFinalShout = createShout();
        mFinalMentions = createMentions();
    }

    public String getShout() { return mFinalShout; }
    public String getMentions() { return mFinalMentions; }

    private String createShout()
    {
        if(mFriends == null) { return mShout; }
        if(mFriends.isEmpty()) { return mShout; }

        String shout = mShout + WITH_MESSAGE;

        int start = shout.length();
        int end = 0;
        boolean isFirst = true;

        for(Users.FoursquareUser user : mFriends) {
            if(!isFirst) {
                shout = shout + USER_SPACER;

                start = end + USER_SPACER.length();
            } else {
                isFirst = false;
            }

            shout = shout + user.firstName;
            end = start + user.firstName.length();

            mMentions.add(new Mention(start, end, user.id));
        }


        return shout;
    }

    private String createMentions()
    {
        String mentions = "";

        for(Mention mention : mMentions) {
            mentions = mentions + mention.start + COMMA + mention.end
                        + COMMA + mention.id + SEMICOLON;
        }

        return mentions;
    }

    // simple class to remember string positions as we construct the string
    class Mention {
        int start;
        int end;
        String id;

        Mention(int _start, int _end, String _id) {
            start = _start;
            end = _end;
            id = _id;
        }
    }
}
