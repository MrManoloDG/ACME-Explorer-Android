package com.example.acme_explorer.entity;

import com.example.acme_explorer.ListTripsActivity;
import com.example.acme_explorer.R;

import java.util.ArrayList;

public class MenuItem {
    private String description;
    private int imageId;
    private Class aClass;

    public MenuItem(String description, int imageId, Class aClass) {
        this.description = description;
        this.imageId = imageId;
        this.aClass = aClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public static ArrayList<MenuItem> getMenu(){
        ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
        menu.add(new MenuItem("Listado de viajes", R.drawable.ic_airplanemode_active_black_24dp, ListTripsActivity.class));
        return menu;
    }
}
