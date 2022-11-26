package com.brian.london_underground.model;

import com.brian.london_underground.graph.AdjacencyListGraph;
import com.brian.london_underground.graph.IGraph;
import com.brian.london_underground.graph.Path;
import com.brian.london_underground.graph.Vertex;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

public class LondonUndergroundManager {

    private IGraph<Station> graph;

    private String PATH_STATIONS = "./db/stations.txt";
    private String PATH_CONNECTIONS = "./db/connections.txt";
    private Map<String, Station> stations;

    public LondonUndergroundManager(){
        graph = new AdjacencyListGraph<>(true);
        stations = new HashMap<>();

        loadStations();
        loadConnections();

    }

    public Path<Station> findFastestRoute(String from, String to){

        return graph.dijkstra(stations.get(from), stations.get(to));
    }

    public Path<Station> findShortestRoute(String from, String to){

       graph.BFS(stations.get(from));

        List<Station> shortestPath = new ArrayList<>();

        Vertex<Station> current = graph.searchVertex(stations.get(to));
        double numStations = current.getD() + 1;

        while (current != null){
            shortestPath.add(current.getElement());
            current = current.getPredecessor();
        }

        Collections.reverse(shortestPath);

        return new Path<>(shortestPath, numStations);

    }

    public void loadStations(){
        try{

            File file = new File(PATH_STATIONS);

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = reader.readLine()) != null){
                Station station = new Station(line);
                stations.put(line, station);
                graph.addVertex(station);
            }

            fis.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void loadConnections(){
        try{

            File file = new File(PATH_CONNECTIONS);

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = reader.readLine()) != null){
                String [] parts = line.split(",");
                graph.addEdge(
                        stations.get(parts[0]),
                        stations.get(parts[1]),
                        Double.parseDouble(parts[2])
                );
            }

            fis.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public Map<String, Station> getStations() {
        return stations;
    }

    public void showMap(String MAP_URL) {
        try {
            Desktop.getDesktop().browse(new URI(MAP_URL));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
