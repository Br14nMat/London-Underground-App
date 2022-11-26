package com.brian.london_underground.graph;

import javafx.util.Pair;

import java.util.Map;

public interface IGraph<E> {

    public void addVertex(E element);
    public void addEdge(E source, E destination, double weight);

    public Vertex<E> searchVertex(E element);

    public Double searchEdge(E source, E destination);

    public void deleteVertex(E element);

    public void deleteEdge(E source, E destination);

    public boolean BFS(E sourceElement);
    public int DFS();
    public Path<E> dijkstra(E source, E destination);

    public Map<Pair<E, E>, Path<E>> floydWarshall();

    public double prim(E source);

    public double kruskal();

}

