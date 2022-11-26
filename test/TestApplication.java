import com.brian.london_underground.graph.Path;
import com.brian.london_underground.model.LondonUndergroundManager;
import com.brian.london_underground.model.Station;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestApplication {

    private LondonUndergroundManager controller;

    public TestApplication(){
        controller = new LondonUndergroundManager();
    }

    public void setUpStage1(){

        controller.loadStations();
        controller.loadConnections();

    }

    @Test
    public void testFindFastestRoute1(){

        setUpStage1();

        double timeExpected = 9.86;
        String pathExpected [] = new String[]{
                "BUCKHURST HILL",
                "WOODFORD",
                "SOUTH WOODFORD",
                "SNARESBROOK",
                "LEYTONSTONE"};

        String from = "BUCKHURST HILL";
        String to = "LEYTONSTONE";

        Path<Station> path = controller.findFastestRoute(from, to);

        for(int i = 0; i < path.getPath().size(); i++){
            assertEquals(pathExpected[i], path.getPath().get(i).getName());
        }

        assertEquals(path.getDistance(), timeExpected);

    }

    @Test
    public void testFindFastestRoute2(){

        setUpStage1();

        String from = "WATERLOO";
        String to = "WARREN STREET";

        double timeExpected = 8.08;
        String pathExpected [] = new String[]{
                "WATERLOO",
                "WESTMINSTER",
                "GREEN PARK",
                "OXFORD CIRCUS",
                "WARREN STREET"};


        Path<Station> path = controller.findFastestRoute(from, to);

        for(int i = 0; i < path.getPath().size(); i++){
            assertEquals(pathExpected[i], path.getPath().get(i).getName());
        }

        assertEquals(path.getDistance(), timeExpected);

    }

    @Test
    public void testFindFastestRoute3(){

        setUpStage1();

        String from = "OLD STREET";
        String to = "WEST KENSINGTON";

        double timeExpected = 24.87;
        String pathExpected [] = new String[]{
                "OLD STREET",
                "MOORGATE",
                "BANK",
                "WATERLOO",
                "WESTMINSTER",
                "ST JAMES PARK",
                "VICTORIA",
                "SLOANE SQUARE",
                "SOUTH KENSINGTON",
                "GLOUCESTER ROAD",
                "EARLS COURT",
                "WEST KENSINGTON"
        };

        Path<Station> path = controller.findFastestRoute(from, to);

        for(int i = 0; i < path.getPath().size(); i++){
            assertEquals(pathExpected[i], path.getPath().get(i).getName());
        }

        assertEquals(path.getDistance(), timeExpected);

    }

    @Test
    public void testFindFastestRoute4(){

        setUpStage1();

        String from = "CAMDEN TOWN";
        String to = "WEST KENSINGTON";

        double timeExpected = 22.66;
        String pathExpected [] = new String[]{
                "CAMDEN TOWN",
                "MORNINGTON CRESCENT",
                "EUSTON (CX)",
                "WARREN STREET",
                "OXFORD CIRCUS",
                "GREEN PARK",
                "HYDE PARK CORNER",
                "KNIGHTSBRIDGE",
                "SOUTH KENSINGTON",
                "GLOUCESTER ROAD",
                "EARLS COURT",
                "WEST KENSINGTON"
        };

        Path<Station> path = controller.findFastestRoute(from, to);

        for(int i = 0; i < path.getPath().size(); i++){
            assertEquals(pathExpected[i], path.getPath().get(i).getName());
        }

        assertEquals(path.getDistance(), timeExpected);


    }

    @Test
    public void testFindFastestRoute5(){

        setUpStage1();

        String from = "WARWICK AVENUE";
        String to = "TEMPLE";

        double timeExpected = 17.48;
        String pathExpected [] = new String[]{
                "WARWICK AVENUE",
                "PADDINGTON",
                "EDGWARE ROAD",
                "MARYLEBONE",
                "BAKER STREET",
                "REGENTS PARK",
                "OXFORD CIRCUS",
                "PICCADILLY CIRCUS",
                "CHARING CROSS",
                "EMBANKMENT",
                "TEMPLE"
        };


        Path<Station> path = controller.findFastestRoute(from, to);

        for(int i = 0; i < path.getPath().size(); i++){
            assertEquals(pathExpected[i], path.getPath().get(i).getName());
        }

        assertEquals(path.getDistance(), timeExpected);

    }


}
