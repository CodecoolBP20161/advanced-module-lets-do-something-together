package com.codecool.model.event;

import lombok.Data;

import javax.persistence.Embeddable;


@Embeddable
@Data
public class Coordinates {

    private float lng;
    private float lat;

    public Coordinates(Object lng, Object lat) {
        this.lng = Float.parseFloat(lng.toString());
        this.lat = Float.parseFloat(lat.toString());
    }

    public Coordinates() {
    }
}
