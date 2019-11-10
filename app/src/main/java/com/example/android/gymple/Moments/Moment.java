package com.example.android.gymple.Moments;

public class Moment implements Comparable<Moment>{
    private String userName;
    private String userMomentDescription;
    private String userProfilePic;
    private String userPhoto;
    private long timestamp;

    Moment() {

    }

    Moment(String userName, String userMomentDescription, String userPhoto, long timestamp) {
        this.userName = userName;
        this.userMomentDescription = userMomentDescription;
        this.userPhoto = userPhoto;
        this.timestamp = timestamp;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public String getUserMomentDescription() {
        return userMomentDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public void setUserMomentDescription(String userMomentDescription) {
        this.userMomentDescription = userMomentDescription;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Moment moment) {
        if (timestamp < moment.getTimestamp())
            return 1;
        else if (timestamp > moment.getTimestamp())
            return -1;

        return 0;
    }
}