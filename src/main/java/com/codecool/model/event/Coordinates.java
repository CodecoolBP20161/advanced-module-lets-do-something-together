package com.codecool.model.event;

import lombok.Data;

import javax.persistence.Embeddable;


@Embeddable
@Data
public class Coordinates {

    private float x;
    private float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
