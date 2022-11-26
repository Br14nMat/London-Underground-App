package com.brian.london_underground.graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex<E> {

    private E element;

    private Vertex<E> predecessor;

    private List<Vertex<E>> adjList;

    private Color color;
    private double d;
    private int f;

    public Vertex(E element){
        this.element = element;
        this.d = 0;
        this.f = 0;

        adjList = new ArrayList<>();
    }
    public void addAdjacent(Vertex<E> adj){
        adjList.add(adj);
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Vertex<E> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex<E> predecessor) {
        this.predecessor = predecessor;
    }

    public E getElement() {
        return element;
    }

    public List<Vertex<E>> getAdjList() {
        return adjList;
    }

    public void setAdjList(List<Vertex<E>> adjList) {
        this.adjList = adjList;
    }
}

