package com.lab7.common.lab;

import com.lab7.common.parameters.numericalParameters.FloatParameter;
import com.lab7.common.parameters.numericalParameters.IntegerParameter;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Float x; //Поле не может быть null
    private Integer y; //Максимальное значение поля: 139, Поле не может быть null
    public static Coordinates DEFAULT = new Coordinates(0f, 0);

    public static class Params {
        public static FloatParameter x = new FloatParameter(0f);
        public static IntegerParameter y = new IntegerParameter(0).setLowerBound(-314);
    }

    public Coordinates(Float x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }


}

