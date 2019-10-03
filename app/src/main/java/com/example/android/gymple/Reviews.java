package com.example.android.gymple;

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

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

    public int getLikeCount() {
        return mLikes;
    }

    public String getAname() { return aname ;}

    public void setAname(String aname) {this.aname = aname ;}

    public String getText() { return text ;}

    public void setText(String text) {this.text = text ;}

    public String getRating() { return rating ;}

    public void setRating(String rating) {this.rating = rating ;}

    public String getrTime() {
        return rTime;
    }

    public void setrTime(String rTime) {
        this.rTime = rTime;
    }


    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
