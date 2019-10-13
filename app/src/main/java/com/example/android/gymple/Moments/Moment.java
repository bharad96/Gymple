package com.example.android.gymple.Moments;

public class Moment {
    private String userName;
    private String userMomentDescription;
    private String userProfilePic;
    private String userPhoto;

    Moment() {

    }

    Moment(String userName, String userMomentDescription, String userPhoto) {
        this.userName = userName;
        this.userMomentDescription = userMomentDescription;
        this.userPhoto = userPhoto;
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
}
