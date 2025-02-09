package com.example.coffeebar;

public class Coffee {
    private String name;
    private String description;
    private int imgResourceID;

    public static final Coffee[] coffees = {
            new Coffee("Black", "black coffee without milk", R.drawable.black),
            new Coffee("Latte", "espresso with steamed milk", R.drawable.latte),
            new Coffee("Cappuccino", "espresso with hot milk and steamed milk foam", R.drawable.cappuccino)
    };

    private Coffee (String name, String description, int imgResourceID){
        this.name = name;
        this.description = description;
        this.imgResourceID = imgResourceID;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getImgResourceID(){
        return imgResourceID;
    }

    public String toString(){
        return this.name;
    }
}
