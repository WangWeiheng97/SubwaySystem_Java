import java.io.File;
import java.util.*;

public class LoadTester {
    public static void main(String[] args) {
        
        try {
            SubwayLoader loader = new SubwayLoader();
            Subway objectville = loader.loadFromFile(new File("SimpleMapForTest.txt"));

            System.out.println("Testing stations...");

            if (objectville.hasStation("DRY Drive")
                            && objectville.hasStation("Weather-O-Rama, Inc.")
                            && objectville.hasStation("Boards 'R' Us")) {
                System.out.println("...station test passed successfully.");
            } else {
                System.out.println("...station test FAILED.");
                System.exit(-1);
            }

            System.out.println("\nTesting connections...");

            if (objectville.hasConnection("DRY Drive", "Head First Theater", "Meyer Line")
                            && objectville.hasConnection("Weather-O-Rama, Inc.", "XHTML Expressway",
                                            "Wirfs-Brock Line")
                            && objectville.hasConnection("Head First Theater", "Infinite Circle",
                                            "Rumbaugh Line")) {
                System.out.println("...connections test passed successfully.");
            } else {
                System.out.println("...connections test FAILED.");
                System.exit(-1);
            }

            System.out.println("\nTesting network...");

            // to test the key station "DRY Drive".
            Map a = objectville.getMap();

            List connectingStations = (List) (objectville.getMap().get("DRY Drive"));
            String names = "";

            for (Object s : connectingStations) {
                names = names + " / " + ((Station) s).getName();
            }
            System.out.println(names);

            List valueStations = new LinkedList();
            Station s1 = new Station("Head First Theater");
            valueStations.add(s1);
            Station s2 = new Station("WebDesignWay");
            valueStations.add(s2);
            Station s3 = new Station("Algebra Avenue");
            valueStations.add(s3);
            Station s4 = new Station("PMP Place");
            valueStations.add(s4);

            if (objectville.hasNetwork("DRY Drive", valueStations)) {
                System.out.println("...network test passed successfully.");
            } else {
                System.out.println("...network test FAILED.");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}