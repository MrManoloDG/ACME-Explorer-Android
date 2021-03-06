package com.example.acme_explorer.entity;

import com.example.acme_explorer.FirebaseStorageActivity;
import com.example.acme_explorer.ListTripsActivity;
import com.example.acme_explorer.LocationActivity;
import com.example.acme_explorer.MapActivity;
import com.example.acme_explorer.R;
import com.example.acme_explorer.SelectedTripsActivity;

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
        menu.add(new MenuItem("Viajes seleccionados", R.drawable.ic_star_black_50dp, SelectedTripsActivity.class));
        // menu.add(new MenuItem("Location", R.drawable.ic_star_black_50dp, LocationActivity.class));
        //menu.add(new MenuItem("Maps", R.drawable.ic_star_black_50dp, MapActivity.class));
        return menu;
    }
}
