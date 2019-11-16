package com.example.android.gymple;

/**
 * The Photo class represents a Photos object.
 * @author  Hisyam J
 * @version 1.0, 15 Nov 2019
 *
 */

public class Photo implements Comparable<Photo> {
    private String placeID;
    private String url;
    private String userID;
    private String authorName;
    private long timestamp;
    private String authorPic;
    private String mImageUrl;


    public Photo(String imageurl)
    {
        mImageUrl = imageurl;
    }

    /**
     * Returns imageURL from Google Places API
     * @return imageURL
     */
    public String getmImageUrl() {
        return mImageUrl;
    }

    /**
     * Set image URL from Google Places API
     * @param mImageUrl imageURL in string
     */
    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    /**
     * Returns userID from Google places API
     * @return userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the userID
     * @param userID userID in string
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Returns the author of the name who took the picture from Google places API
     * @return author name
     */

    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the author name from GOogle places API
     * @param authorName author name in string
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Return the timestamp when the image was taken into Google places API
     * @return timestamp of the picture taken
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp at the time when the image was taken
     * @param timestamp timestamp in hours and minutes
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the picture URL of the author from Google places API
     * @return pictureURL from places API
     */
    public String getAuthorPic() {
        return authorPic;
    }

    /**
     * Sets the author picture
     * @param authorPic author pic in string URL format
     */
    public void setAuthorPic(String authorPic) {
        this.authorPic = authorPic;
    }

    /**
     * Returns the place ID string of the current activity centre
     * @return placeID
     */
    public String getPlaceID() {
        return placeID;
    }

    /**
     * Set the place ID of the activity centre
     * @param placeID in string
     */
    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    /**
     * Returns the url of the website for the activity centre
     * @return url of activity centre
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url of the activity centre
     * @param url in string
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Photo p) {
        if (timestamp < p.getTimestamp())
            return 1;
        else if (timestamp > p.getTimestamp())
            return -1;

        return 0;
    }
}