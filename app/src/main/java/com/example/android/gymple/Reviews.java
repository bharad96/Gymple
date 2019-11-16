package com.example.android.gymple;

/**
 * The Reviews class represents a Reviews objects
 * @author  Hisyam Jukifli
 * @version 1.1, 15 Oct 2019
 *
 */

public class Reviews {

    private String mImageUrl;
    private String aname;
    private String text;
    private String rTime;
    private String mCreator;
    private int mLikes;
    private String rating;
    private String cName;

    public Reviews(String imageUrl, String name, String textm, String rate, String time, String acname) {
        mImageUrl = imageUrl;
        aname = name;
        text = textm;
        rating = rate;
        rTime = time;
        cName = acname;
    }

    /**
     * Returns the image URL of the Activity Centre from Google places API
     * @return image URL in string
     */
    public String getImageUrl() {
        return mImageUrl;
    }
    /**
     * Returns the Reviewers name from Google places API
     * @return reviewer name in string
     */
    public String getCreator() {
        return mCreator;
    }

    /**
     * Returns the the rating of the review
     * @return rating in double
     */
    public int getLikeCount() {
        return mLikes;
    }
    /**
     * Returns the Authors name of the Google review
     * @aname The current Authiors name in GOogle review
     */

    public String getAname() { return aname ;}

    /**
     * Set the author name of the Google review
     * @param aname
     */
    public void setAname(String aname) {this.aname = aname ;}

    /**
     * Returns the Google review text details
     * @return review text details
     */
    public String getText() { return text ;}

    /**
     * Set the Google review text
     * @param text text review details
     */
    public void setText(String text) {this.text = text ;}

    /**
     * Returns the Google review rating
     * @return review rating
     */
    public String getRating() { return rating ;}

    /**
     * Set the google review rating
     * @param rating google review rating
     */
    public void setRating(String rating) {this.rating = rating ;}

    /**
     * Returns the time the review was written
     * @return time review was written
     */
    public String getrTime() {
        return rTime;
    }

    /**
     * Set the time the review was writting
     * @param rTime time in string
     */
    public void setrTime(String rTime) {
        this.rTime = rTime;
    }

    /**
     * Set the author name of the Google review
     * @return return author name
     */
    public String getcName() {
        return cName;
    }
    /**
     * Set the author name of the Google review
     * @param cName name of author of review
     */
    public void setcName(String cName) {
        this.cName = cName;
    }
}
