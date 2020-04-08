package com.example.acme_explorer.entity;

import com.example.acme_explorer.Constantes;

import java.util.ArrayList;
import java.util.Random;

public class Trip {
    private String title, description, imgUrl;
    private float price;

    public Trip(String title, String description, String imgUrl, float price) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public static ArrayList<Trip> generateTrips(){
        ArrayList<Trip> trips = new ArrayList<Trip>();
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            trips.add(new Trip(Constantes.ciudades[r.nextInt(Constantes.ciudades.length)],
                    "Lugar de salida: " + Constantes.lugarSalida[r.nextInt(6)],
                    Constantes.urlImagenes[r.nextInt(7)],
                    r.nextFloat()*1000));
        }
        return trips;
    }
}
