package com.example.acme_explorer.entity;

import com.example.acme_explorer.Constantes;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Trip implements Serializable {
    private int id;

    private String title, description, imgUrl;

    @DocumentId
    private String documentId;
    private Date startDate, endDate;
    private float price;
    private boolean selected;
    private float latitude;
    private float longitude;


    public Trip(int id, String title, String description, String imgUrl, Date startDate, Date endDate, float price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.selected = false;
    }

    public Trip(String title, String description, String imgUrl, Date startDate, Date endDate, float price) {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.selected = false;
    }

    public Trip(int id, String title, String description, String imgUrl, Date startDate, Date endDate, float price, float latitude, float longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.selected = false;
    }


    public Trip() {
        this.startDate = new Date();
        this.endDate = new Date();
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getId() {
        return id;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Date getStartDateReturningDate() {
        return startDate;
    }

    /*
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }*/

    public void setStartDate(long startDate) {
        this.startDate.setTime(startDate);
    }

    public Date getEndDateReturningDate() {
        return endDate;
    }

    /*
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }*/

    public long getStartDate() {
        return startDate.getTime();
    }

    public long getEndDate() {
        return endDate.getTime();
    }

    public void setEndDate(long endDate) {
        this.endDate.setTime(endDate);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (id != trip.id) return false;
        if (Float.compare(trip.price, price) != 0) return false;
        if (selected != trip.selected) return false;
        if (title != null ? !title.equals(trip.title) : trip.title != null) return false;
        if (description != null ? !description.equals(trip.description) : trip.description != null)
            return false;
        if (imgUrl != null ? !imgUrl.equals(trip.imgUrl) : trip.imgUrl != null) return false;
        if (startDate != null ? !startDate.equals(trip.startDate) : trip.startDate != null)
            return false;
        return endDate != null ? endDate.equals(trip.endDate) : trip.endDate == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", selected=" + selected +
                ", documentId='" + documentId+ '\'' +
                '}';
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
            trips.add(new Trip(i,Constantes.ciudades[r.nextInt(Constantes.ciudades.length)],
                    "Lugar de salida: " + Constantes.lugarSalida[r.nextInt(6)],
                    Constantes.urlImagenes[r.nextInt(7)],
                    rDate,
                    calendar.getTime(),
                    r.nextFloat()*1000));
        }
        return trips;
    }
}
