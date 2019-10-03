package com.example.android.gymple;

public class Details {
    private String mImageUrl;
    private String acName;
    private String maddr;
    private boolean mOpenow;

    public String getMaddr() {
        return maddr;
    }

    public void setMaddr(String maddr) {
        this.maddr = maddr;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }



    public boolean ismOpenow() {
        return mOpenow;
    }

    public void setmOpenow(boolean mOpenow) {
        this.mOpenow = mOpenow;
    }



    public Details(String imageUrl, String name, String addr) {
        mImageUrl = imageUrl;
        acName = name;
        maddr = addr;

    }

}
