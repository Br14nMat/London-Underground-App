package com.brian.london_underground.graph;

import javafx.util.Pair;

import java.util.*;

public class AdjacencyListGraph<E> implements IGraph<E>{

    private Map<E, Vertex<E>> vertices;

    private Map<Vertex<E>, Map<Vertex<E>, Double>> edges;

    private boolean isDirected;

    public AdjacencyListGraph(boolean isDirected){
        vertices = new HashMap<>();
        edges = new HashMap<>();
        this.isDirected = isDirected;
    }

    @Override
    public void addVertex(E element) {
        if(!vertices.containsKey(element))
            vertices.put(element, new Vertex<>(element));
    }

    @Override
    public void addEdge(E source, E destination, double weight) {

        Vertex<E> vSource = vertices.get(source);
        Vertex<E> vDestination = vertices.get(destination);

        if(vSource != null && vDestination != null){
            vSource.addAdjacent(vDestination);

            Map<Vertex<E>, Double> aux1 = edges.get(vSource) != null
                    ? edges.get(vSource)
                    : new HashMap<>();

            aux1.put(vDestination, weight);
            edges.put(vSource, aux1);

            if(!isDirected){
                vDestination.addAdjacent(vSource);

                Map<Vertex<E>, Double> aux2 = edges.get(vDestination) != null
                        ? edges.get(vDestination)
                        : new HashMap<>();

                aux2.put(vSource, weight);
                edges.put(vDestination, aux2);

            }

        }

    }

    @Override
    public void deleteVertex(E element){
        if(vertices.containsKey(element)){
            edges.put(vertices.get(element), null);
            vertices.put(element, null);
        }
    }

    @Override
    public Vertex<E> searchVertex(E element){
        return vertices.get(element);
    }

    @Override
    public Double searchEdge(E source, E destination){
        Map<Vertex<E>, Double> aux = edges.get(vertices.get(source));

        if(aux != null)
            return aux.get(vertices.get(destination));

        return null;
    }

    @Override
    public void deleteEdge(E source, E destination){
        Vertex<E> vSource = vertices.get(source);
        Vertex<E> vDestination = vertices.get(destination);

        if(vSource != null && vDestination != null){
            edges.get(vSource).remove(vDestination);

            if(!isDirected)
                edges.get(vDestination).remove(vSource);
        }
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

            u.getAdjList().forEach(v -> {

                if(v.getColor() == Color.WHITE){
                    v.setColor(Color.GRAY);
                    v.setD(u.getD() + 1);
                    v.setPredecessor(u);
                    queue.offer(v);
                }

            });

            u.setColor(Color.BLACK);

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

        for(Vertex<E> v: u.getAdjList()){
            if(v.getColor() == Color.WHITE){
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
    public Path<E> dijkstra(E eSource, E eDestination){

        Vertex<E> source = vertices.get(eSource);
        Vertex<E> destination = vertices.get(eDestination);

        if(source == null || destination == null)
            return new Path<>(null, Double.MAX_VALUE);


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

            for(Vertex<E> neighbor: u.getAdjList()){

                double alt = u.getD() + edges.get(u).get(neighbor);

                if(alt < neighbor.getD()){
                    neighbor.setD(alt);
                    neighbor.setPredecessor(u);
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
    public Map<Pair<E, E>, Path<E>> floydWarshall(){

        int n = vertices.size();
        double [][] distance = new double[n][n];
        Vertex<E> [][] prev = new Vertex[n][n];
        List<Vertex<E>> vertexList = new ArrayList<>(vertices.values());
        Map<Vertex<E>, Integer> mapIndex = new HashMap<>();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                distance[i][j] = Double.MAX_VALUE;
            }
        }

        for(int i = 0; i < n; i++){
            mapIndex.put(vertexList.get(i), i);
            distance[i][i] = 0;
        }

        for(Vertex<E> v: vertexList){
            for(Vertex<E> u: v.getAdjList()){
                distance[mapIndex.get(v)][mapIndex.get(u)] = edges.get(v).get(u);
                prev[mapIndex.get(v)][mapIndex.get(u)] = v;
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
                listPath.add(vertexList.get(j).getElement());

                Vertex<E> current = prev[i][j];

                while(current != null && current != vertexList.get(i)){
                    listPath.add(current.getElement());
                    current = prev[i][mapIndex.get(current)];
                }
                listPath.add(vertexList.get(i).getElement());
                Collections.reverse(listPath);

                paths.put(new Pair<>(vertexList.get(i).getElement(), vertexList.get(j).getElement()),
                        new Path<>(listPath, distance[i][j]));

            }
        }

        return paths;

    }

    @Override
    public double prim(E eSource){

        Vertex<E> source = vertices.get(eSource);

        if(source == null)
            return -1.0;

        for(Vertex<E> vertex: vertices.values()){
            vertex.setColor(Color.WHITE);
            vertex.setD(Double.MAX_VALUE);
            vertex.setPredecessor(null);
        }

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

            for(Vertex<E> v: u.getAdjList()){

                if(v.getColor() == Color.WHITE && edges.get(u).get(v) < v.getD()){
                    v.setD(edges.get(u).get(v));
                    v.setPredecessor(u);
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

        int [] parent = new int[vertices.size()];

        List<Pair<Integer, Pair<Integer, Double>>> edgeList = new ArrayList<>();
        List<Vertex<E>> aux = new ArrayList<>(vertices.values());
        Map<Vertex<E>, Integer> mapIndex = new HashMap<>();

        for(int i = 0; i < vertices.size(); i++){
            mapIndex.put(aux.get(i), i);
            parent[i] = i;
        }


        edges.forEach((source, adj) -> {
            adj.forEach((destination, weight) -> {
                edgeList.add(new Pair<>(mapIndex.get(source), new Pair<>(mapIndex.get(destination), weight)));
            });
        });

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
