package com.example.acme_explorer.entity;

import com.example.acme_explorer.Constantes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Trip implements Serializable {
    private String title, description, imgUrl;
    private Date startDate, endDate;
    private float price;

    public Trip(String title, String description, String imgUrl, Date startDate, Date endDate, float price) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static ArrayList<Trip> generateTrips(){
        ArrayList<Trip> trips = new ArrayList<Trip>();
        Random r = new Random();
        ThreadLocalRandom rd =ThreadLocalRandom.current();
        Calendar calendar = Calendar.getInstance();

        Date fechaInicio = new Date();
        calendar.setTime(fechaInicio);
        calendar.add(Calendar.MONTH, 3);
        Date fechaFin = calendar.getTime();
        for (int i = 0; i < 50; i++) {
            Date rDate = new Date(rd.nextLong(fechaInicio.getTime(), fechaFin.getTime()));
            calendar.setTime(rDate);
            calendar.add(Calendar.DAY_OF_YEAR,rd.nextInt(2,13));
            trips.add(new Trip(Constantes.ciudades[r.nextInt(Constantes.ciudades.length)],
                    "Lugar de salida: " + Constantes.lugarSalida[r.nextInt(6)],
                    Constantes.urlImagenes[r.nextInt(7)],
                    rDate,
                    calendar.getTime(),
                    r.nextFloat()*1000));
        }
        return trips;
    }
}
