package com.example.roomfinder.model;

import java.io.Serializable;

/**
 * Created by Madhusudan Sapkota on 7/9/2019.
 */
public class Room implements Serializable {
    private String image;
    private String name;
    private String type;
    private String price;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
