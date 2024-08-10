package com.example.hotel.Core.Model;

import java.io.Serializable;

public class Data implements Serializable {
    int Dp_image;
    String name, /*Description,*/ MessageCount;

    public Data(int dp_image, String name/*, String description*/) {
        Dp_image = dp_image;
        this.name = name;
      //  Description = description;
    }

    public int getDp_image() {
        return Dp_image;
    }

    public void setDp_image(int dp_image) {
        Dp_image = dp_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   /* public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }*/

    public String getMessageCount() {
        return MessageCount;
    }

    public void setMessageCount(String messageCount) {
        MessageCount = messageCount;
    }
}