package com.example.whynot;

import java.io.Serializable;

public class University implements Serializable {
    public String universityName;
    public int followers = 0;
    public String photo;

    University(){
        universityName="";
        followers=0;
        photo="";
    }

    University(String Name, String photo){
        this.universityName = Name;
        this.photo = photo;
    }

}
