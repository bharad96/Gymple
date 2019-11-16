package com.example.android.gymple;
/**
 * The ActivityCentre class represents a Details objects.
 * @author  Hisyam J
 * @version 1.0, 15 Nov 2019
 *
 */
public class Details {
    private String mImageUrl;
    private String acName;
    private String maddr;
    private boolean mOpenow;
    private String name;

    /**
     * Returns the address details from the Google Places API
     * @return address of Activity Centre
     */
    public String getMaddr() {
        return maddr;
    }

    /**
     * Returns the address from Google Places API
     * @param maddr full address details
     */
    public void setMaddr(String maddr) {
        this.maddr = maddr;
    }

    /**
     * Returns the image URL from Google places API
     * @return imageURL
     */
    public String getmImageUrl() {
        return mImageUrl;
    }

    /**
     * Set the image URL
     * @param mImageUrl imageURL from Google places API
     */
    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    /**
     * Returns Activity centre name from Google places API
     * @return name of Aactivity centre
     */
    public String getAcName() {
        return acName;
    }

    /**
     * Set activity centre name from Google places API
     * @param acName AC name
     */
    public void setAcName(String acName) {
        this.acName = acName;
    }

    /**
     * Returns Acrtivity Centre name from GOogle Geocode API
     * @return AC name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets AC name from geocode api
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns opening closed boolean Google places API
     * @return open closed binary value
     */
    public boolean ismOpenow() {
        return mOpenow;
    }

    /**
     * Set binary value open- 1 closed -0
     * @param mOpenow open/closed
     */
    public void setmOpenow(boolean mOpenow) {
        this.mOpenow = mOpenow;
    }


    /**
     * Set the details from Google places API
     * @param imageUrl the iamge URL of the Activity centre
     * @param name the name of activity centre
     * @param addr the address of activity centre
     */
    public Details(String imageUrl, String name, String addr) {
        mImageUrl = imageUrl;
        acName = name;
        maddr = addr;

    }

}
