package com.brian.london_underground.graph;

import java.util.List;

public class Path<E> {

    private List<E> path;
    private double distance;

    public Path(List<E> path, double distance){
        this.path = path;
        this.distance = Math.round(distance * 1000.0) / 1000.0;
    }

    public double getDistance() {
        return distance;
    }

    public List<E> getPath() {
        return path;
    }
}
