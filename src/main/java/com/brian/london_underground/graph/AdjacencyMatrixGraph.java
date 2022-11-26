package com.brian.london_underground.graph;

import javafx.util.Pair;

import java.util.*;

public class AdjacencyMatrixGraph<E> implements IGraph<E>{

    private List<List<Double>> adjMatrix;
    private Map<Vertex<E>, Integer> mapIndex;
    private Map<E, Vertex<E>> vertices;

    private boolean isDirected;

    public AdjacencyMatrixGraph(boolean isDirected){
        adjMatrix = new ArrayList<>();
        mapIndex = new HashMap<>();
        vertices = new HashMap<>();
        this.isDirected = isDirected;
    }

    @Override
    public void addVertex(E element) {
        if(!vertices.containsKey(element)){
            Vertex<E> newVertex = new Vertex<>(element);
            vertices.put(element,newVertex);
            mapIndex.put(newVertex, adjMatrix.size());
            adjMatrix.add(new ArrayList<>(Collections.nCopies(adjMatrix.size(), Double.MAX_VALUE)));
            for (List<Double> matrix : adjMatrix) matrix.add(Double.MAX_VALUE);
        }
    }

    @Override
    public void addEdge(E source, E destination, double weight) {

        Integer sourceIndex = mapIndex.get(vertices.get(source));
        Integer destinationIndex = mapIndex.get(vertices.get(destination));

        if(sourceIndex == null || destinationIndex == null) return;

        adjMatrix.get(sourceIndex).set(destinationIndex, weight);

        if(!isDirected)
            adjMatrix.get(destinationIndex).set(sourceIndex, weight);

    }

    @Override
    public Vertex<E> searchVertex(E element){
        return vertices.get(element);
    }

    @Override
    public Double searchEdge(E source, E destination){

        Vertex<E> vSource = vertices.get(source);
        Vertex<E> vDestination = vertices.get(destination);

        if(vSource == null || vDestination == null)
            return null;

        List<Double> aux = adjMatrix.get(mapIndex.get(vertices.get(source)));

        if(aux != null)
            return aux.get(mapIndex.get(vertices.get(destination)));

        return null;
    }

    @Override
    public void deleteVertex(E element) {
        Integer elementIndex = mapIndex.get(vertices.get(element));

        if(elementIndex == null) return;

        adjMatrix.remove((int) elementIndex);
        vertices.remove(element);
        adjMatrix.forEach(row -> row.remove((int) elementIndex));

    }

    @Override
    public void deleteEdge(E source, E destination) {

        Integer sourceIndex = mapIndex.get(vertices.get(source));
        Integer destinationIndex = mapIndex.get(vertices.get(destination));

        if(sourceIndex == null || destinationIndex == null) return;

        adjMatrix.get(sourceIndex).set(destinationIndex, Double.MAX_VALUE);

        if(!isDirected)
            adjMatrix.get(destinationIndex).set(sourceIndex, Double.MAX_VALUE);
    }

    @Override
    public boolean BFS(E sourceElement) {

        Vertex<E> source = vertices.get(sourceElement);

        List<Vertex<E>> vertexList = new ArrayList<>(vertices.values());

        vertexList.forEach(vertex -> {
            vertex.setColor(Color.WHITE);
            vertex.setD(-1);
            vertex.setPredecessor(null);
        });

        source.setColor(Color.GRAY);
        source.setD(0);
        source.setPredecessor(null);

        Queue<Vertex<E>> queue = new LinkedList<>();
        queue.offer(source);

        while(!queue.isEmpty()){

            Vertex<E> u = queue.poll();
            int uIndex  = mapIndex.get(u);

            for (int i = 0; i < adjMatrix.size(); i++){

                Vertex<E> v = vertexList.get(i);

                if(adjMatrix.get(uIndex).get(i) != Double.MAX_VALUE && v.getColor() == Color.WHITE){
                    v.setColor(Color.GRAY);
                    v.setD(u.getD() + 1);
                    v.setPredecessor(u);
                    queue.offer(v);
                }
            }

        }

        boolean isConnected = true;
        for(int i = 0; i < vertexList.size() && isConnected; i++)
            if(vertexList.get(i).getColor() == Color.WHITE)
                isConnected = false;

        return isConnected;
    }

    @Override
    public int DFS() {

        vertices.values().forEach(vertex -> {
            vertex.setColor(Color.WHITE);
            vertex.setPredecessor(null);
        });

        int time = 0;

        int treesNumber = 0;

        for(Vertex<E> u : vertices.values()){
            if(u.getColor() == Color.WHITE){
                time = DFSVisit(u, time);
                treesNumber++;
            }
        }

        return treesNumber;

    }

    private int DFSVisit(Vertex<E> u, int time){

        time++;
        u.setD(time);
        u.setColor(Color.GRAY);
        List<Vertex<E>> vertexList = new ArrayList<>(vertices.values());

        int uIndex = mapIndex.get(u);

        for (int i = 0; i < adjMatrix.size(); i++){

            Vertex<E> v = vertexList.get(i);

            if(adjMatrix.get(uIndex).get(i) != Double.MAX_VALUE && v.getColor() == Color.WHITE){
                v.setPredecessor(u);
                DFSVisit(v, time);
            }


        }

        u.setColor(Color.BLACK);
        time++;
        u.setF(time);

        return time;

    }

    @Override
    public Path<E> dijkstra(E eSource, E eDestination) {

        Vertex<E> source = vertices.get(eSource);
        Vertex<E> destination = vertices.get(eDestination);

        if(source == null || destination == null)
            return new Path<>(null, Double.MAX_VALUE);

        Map<Integer, Vertex<E>> aux = new HashMap<>();
        mapIndex.forEach((vertex, i) ->  aux.put(i, vertex));

        for(Vertex<E> v: vertices.values()){
            v.setD(Double.MAX_VALUE);
            v.setPredecessor(null);
        }

        List<Vertex<E>> vertexList = new ArrayList<>(vertices.values());

        Comparator<Vertex<E>> comparatorDistance = new Comparator<Vertex<E>>() {
            @Override
            public int compare(Vertex<E> v1, Vertex<E> v2) {
                return Double.compare(v1.getD(), v2.getD());
            }
        };

        source.setD(0);

        vertexList.sort(comparatorDistance);

        while(vertexList.size() != 0){

            Vertex<E> u = vertexList.remove(0);
            int uIndex = mapIndex.get(u);

            for (int i = 0; i < adjMatrix.size(); i++){

                if(adjMatrix.get(uIndex).get(i) != Double.MAX_VALUE){

                    double alt = u.getD() + adjMatrix.get(uIndex).get(i);

                    if(alt < aux.get(i).getD()){
                        aux.get(i).setD(alt);
                        aux.get(i).setPredecessor(u);
                    }

                }
            }

            vertexList.sort(comparatorDistance);

        }

        if(destination.getD() == Double.MAX_VALUE)
            return new Path<>(null, Double.MAX_VALUE);

        List<E> path = new ArrayList<>();
        Vertex<E> current = destination;
        while(current != source){
            path.add(current.getElement());
            current = current.getPredecessor();
        }

        path.add(source.getElement());
        Collections.reverse(path);

        return new Path<>(path, destination.getD());

    }

    @Override
    public Map<Pair<E, E>, Path<E>> floydWarshall() {

        int n = vertices.size();
        double [][] distance = new double[n][n];
        Vertex<E> [][] prev = new Vertex[n][n];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                distance[i][j] = Double.MAX_VALUE;
            }
            distance[i][i] = 0;
        }

        Map<Integer, Vertex<E>> aux = new HashMap<>();
        mapIndex.forEach((vertex, i) ->  aux.put(i, vertex));

        for (int i = 0; i < adjMatrix.size(); i++){
            for(int j = 0; j < adjMatrix.size(); j++){
                if(adjMatrix.get(i).get(j) != Double.MAX_VALUE){
                    distance[i][j] = adjMatrix.get(i).get(j);
                    prev[i][j] = aux.get(i);
                }
            }
        }


        for (int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++) {
                    if(distance[i][j] > distance[i][k] + distance[k][j]){
                        distance[i][j] = distance[i][k] + distance[k][j];
                        prev[i][j] = prev[k][j];
                    }
                }
            }
        }

        Map<Pair<E, E>, Path<E>> paths = new HashMap<>();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){

                if(i == j) continue;

                List<E> listPath = new ArrayList<>();
                listPath.add(aux.get(j).getElement());

                Vertex<E> current = prev[i][j];
                while(current != null && current != aux.get(i)){
                    listPath.add(current.getElement());
                    current = prev[i][mapIndex.get(current)];
                }
                listPath.add(aux.get(i).getElement());
                Collections.reverse(listPath);

                paths.put(new Pair<>(aux.get(i).getElement(), aux.get(j).getElement()),
                        new Path<>(listPath, distance[i][j]));

            }
        }

        return paths;

    }

    @Override
    public double prim(E eSource) {

        Vertex<E> source = vertices.get(eSource);

        if(source == null)
            return -1.0;

        for(Vertex<E> vertex: vertices.values()){
            vertex.setColor(Color.WHITE);
            vertex.setD(Double.MAX_VALUE);
            vertex.setPredecessor(null);
        }

        Map<Integer, Vertex<E>> aux = new HashMap<>();
        mapIndex.forEach((vertex, i) ->  aux.put(i, vertex));
        List<Vertex<E>> vertexList = new ArrayList<>(vertices.values());
        Comparator<Vertex<E>> comparatorDistance = new Comparator<Vertex<E>>() {
            @Override
            public int compare(Vertex<E> v1, Vertex<E> v2) {
                return Double.compare(v1.getD(), v2.getD());
            }
        };

        source.setD(0);
        source.setPredecessor(null);

        vertexList.sort(comparatorDistance);

        while (vertexList.size() != 0){

            Vertex<E> u = vertexList.remove(0);
            int uIndex = mapIndex.get(u);

            for (int i = 0; i < adjMatrix.size(); i++){
                if(adjMatrix.get(uIndex).get(i) != Double.MAX_VALUE){
                    Vertex<E> v = aux.get(i);

                    if(v.getColor() == Color.WHITE && adjMatrix.get(uIndex).get(i) < v.getD()){
                        v.setD(adjMatrix.get(uIndex).get(i));
                        v.setPredecessor(u);
                    }
                }
            }
            u.setColor(Color.BLACK);
            vertexList.sort(comparatorDistance);

        }

        double sum = 0;
        for(Vertex<E> vertex: vertices.values()){
            sum+= vertex.getD();
        }

        return sum;

    }

    @Override
    public double kruskal() {

        double total = 0;
        int[] parent = new int[vertices.size()];

        for(int i = 0; i < parent.length; i++){
            parent[i] = i;
        }

        List<Pair<Integer, Pair<Integer, Double>>> edgeList = new ArrayList<>();

        for(int i = 0; i < adjMatrix.size(); i++){
            for (int j = 0; j < adjMatrix.size(); j++){
                if(adjMatrix.get(i).get(j) != Double.MAX_VALUE){
                    edgeList.add(new Pair<>(i, new Pair<>(j, adjMatrix.get(i).get(j))));
                }
            }
        }

        Comparator<Pair<Integer, Pair<Integer, Double>>> comparator = new Comparator<Pair<Integer, Pair<Integer, Double>>>() {
            @Override
            public int compare(Pair<Integer, Pair<Integer, Double>> o1, Pair<Integer, Pair<Integer, Double>> o2) {
                return Double.compare(o1.getValue().getValue(), o2.getValue().getValue());
            }
        };

        edgeList.sort(comparator);

        for (Pair<Integer, Pair<Integer, Double>> e : edgeList) {
            if(find(e.getKey(), parent) != find(e.getValue().getKey(), parent)){
                total+= e.getValue().getValue();
                union(e.getKey(), e.getValue().getKey(), parent);
            }
        }

        return total;

    }

    public int find(int x, int[] parent) {
        while (parent[x] != x)
            x = parent[x];
        return x;
    }

    public void union(int u, int v, int[] parent) {
        int a = find(u, parent);
        int b = find(v, parent);
        parent[a] = b;
    }


}
