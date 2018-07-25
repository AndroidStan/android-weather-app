package com.weather.android.util.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CityDetails {
    @PrimaryKey
    private Integer id;
    private String name;
    private String country;
    private Double longtitude;
    private Double latitude;
    private Integer zipcode;
    private String statename;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public Integer getZipcode(){
        return zipcode;
    }

    public void setZipcode(Integer zipcode){
        this.zipcode = zipcode;
    }

    public Double getLongtitude(){
        return longtitude;
    }

    public void setLongtitude(Double longtitude){
        this.longtitude = longtitude;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(Double longtitude){
        this.latitude = longtitude;
    }

    public String getStatename(){
        return statename;
    }

    public void setStatename(String statename){
        this.statename = statename;
    }
}
