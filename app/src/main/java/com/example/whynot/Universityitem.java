package com.example.whynot;

import android.net.Uri;

public class Universityitem{
    public String universityName;
    public int followers;
    public String photo;

    public Universityitem(){
        universityName="";
        followers=0;
        photo="";
    }


    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public Uri getPhoto() {
        return Uri.parse(photo);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
