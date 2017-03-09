package com.codecool.model.event;

import lombok.Data;

import javax.persistence.Embeddable;


@Embeddable
@Data
public class Coordinates {

    private float x;
    private float y;

    public Coordinates(String xy) {
        String[] coords = xy.split(",");
        this.x = Float.parseFloat(coords[0]);
        this.y = Float.parseFloat(coords[1]);
    }

}
