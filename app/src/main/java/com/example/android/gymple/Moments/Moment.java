package com.example.android.gymple.Moments;

/**
 * Moment object will be used to populate the moment feed
 *
 * @author  Akarapu Bharadwaj
 * @version 1.0, 11 Nov 2019
 *
 */
public class Moment implements Comparable<Moment>{
    private String userName;
    private String userMomentDescription;
    private String userProfilePic;
    private String userPhoto;
    private long timestamp;


        /**
             * Moment Constructor:
             *<p>
         *     The moment constructor will be used in the momentsFragment to create moments.
             *</p>
             * @param userName is the name of the user from google accounts, userMomentDescription is the description the user inputs, userPhoto is the user's uploaded photo and timestamp is the time at which the moment is uploaded
             * @return moment object
             */
    Moment(String userName, String userMomentDescription, String userPhoto, long timestamp) {
        this.userName = userName;
        this.userMomentDescription = userMomentDescription;
        this.userPhoto = userPhoto;
        this.timestamp = timestamp;
    }

    /**
     * @return the userPhoto string.
     */
    public String getUserPhoto() {
        return userPhoto;
    }
    /**
     * @return the getUserMomentDescription string.
     */
    public String getUserMomentDescription() {
        return userMomentDescription;
    }
    /**
     * @return the userName string.
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userPhoto set the userPhoto
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     *
     * @param userMomentDescription set the userMomentDescription
     */
    public void setUserMomentDescription(String userMomentDescription) {
        this.userMomentDescription = userMomentDescription;
    }

    /**
     *
     * @param userName set the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return the timestamp long.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp set the timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Compare two moments for displaying them in decending order in momentsFragment View
     * @param moment for comparison
     * @return int for sorting
     */
    @Override
    public int compareTo(Moment moment) {
        if (this.timestamp < moment.getTimestamp())
            return 1;
        else if (this.timestamp > moment.getTimestamp())
            return -1;
        return 0;
    }
}