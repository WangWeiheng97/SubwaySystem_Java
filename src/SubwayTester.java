import java.util.List;
import java.io.File;

import java.util.List;
import java.io.File;

public class SubwayTester {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Usage: SubwayTester [startStation] [endStation]");
            System.exit(-1);
        }

        try {
            SubwayLoader loader = new SubwayLoader();
            Subway objectville = loader.loadFromFile(new File("BeijingSubwayMap.txt"));

            if (!objectville.hasStation(args[0])) {
                System.err.println(args[0] + " is not a station in Objectville.");
                System.exit(-1);
            } else if (!objectville.hasStation(args[1])) {
                System.err.println(args[1] + " is not a station in Objectville.");
                System.exit(-1);
            }

            List<Connection> LeastStations =
                            objectville.getLeastStationsDirections(args[0], args[1]);
            List<Connection> LeastDisRoute = objectville.getLeastDisDirection(args[0], args[1]);
            List<Connection> LeastTransferRoute =
                            objectville.getLeastTransferDirection(args[0], args[1]);


            SubwayPrinter printer = new SubwayPrinter(System.out);

            System.out.println("Route for Least Stations: ");
            printer.printDirections(LeastStations);
            System.out.println();

            System.out.println("Route for Least Distance: ");
            printer.printDirections(LeastDisRoute);
            System.out.println();

            System.out.println("Route for Least Transfer: ");
            printer.printDirections(LeastTransferRoute);
            System.out.println();
            
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }
}