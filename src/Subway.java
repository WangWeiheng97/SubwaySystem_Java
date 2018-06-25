
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;



public class Subway {
    private List<Station> stations;
    private List<Connection> connections;
    private Map<String, List<Station>> network; // to store each station, and a list of all the
                                                // stations that it connects to.

    int minTransfer = Integer.MAX_VALUE;
    public Subway() {
        this.stations = new LinkedList<>();
        this.connections = new LinkedList<>();
        this.network = new HashMap<>();
    }

    public void addStation(String stationName) {
        if (!this.hasStation(stationName)) {
            Station station = new Station(stationName);
            stations.add(station);
            // if(stationName.equals("苹果园") && this.hasStation("苹果园")) {
            // System.out.println("苹果园 added");
            // System.out.println("stationName's hashCode " + stationName.hashCode());
            // }
        }
    }

    // Check if a station is already in the subway's stations List.
    public boolean hasStation(String stationName) {
        return stations.contains(new Station(stationName));
    }

    public void addConnection(String station1Name, String station2Name, String lineName, int time) {
        // System.out.println("station1Name: " + station1Name);
        // System.out.println("station1Name's hashCode " + station1Name.hashCode());
        // System.out.println("苹果园's hashCode " + "苹果园".hashCode());
        // if(station1Name.equals("苹果园")) {
        // System.out.println("Stations have 苹果园");
        // }
        if ((this.hasStation(station1Name)) && (this.hasStation(station2Name))) {
            Station station1 = new Station(station1Name);
            Station station2 = new Station(station2Name);
            Connection connection = new Connection(station1, station2, lineName, time);
            connections.add(connection);

            // Subways run in two directions, we need to add two connections.
            connections.add(new Connection(station2, station1, lineName, time));

            // When adding connections, we need to update the Map of stations.
            addToNetwork(station1Name, station2);
            addToNetwork(station2Name, station1);
        } else {
            throw new RuntimeException("Invalid connection!");
        }
    }

    private void addToNetwork(String station1Name, Station station2) {
        // Our Map has each station as its keys. The value for that station is a List containing all
        // the stations that the key station connects to.
        if (network.containsKey(station1Name)) {
            List<Station> connectionStations = network.get(station1Name);
            if (!connectionStations.contains(station2)) {
                connectionStations.add(station2);
            }
        } else {
            List<Station> connectionStations = new LinkedList<>();
            connectionStations.add(station2);
            network.put(station1Name, connectionStations);
        }
    }

    public Map<String, List<Station>> getMap() {
        return network;
    }

    // Check if a particular connection exists, given the two station names and the line name for
    // that connection.
    public boolean hasConnection(String station1Name, String station2Name, String lineName) {
        Station station1 = new Station(station1Name);
        Station station2 = new Station(station2Name);

        for (Iterator<Connection> i = connections.iterator(); i.hasNext();) {
            Connection connection = (Connection) i.next();
            if (connection.getLineName().equalsIgnoreCase(lineName)) {
                if ((connection.getStation1().equals(station1))
                                && (connection.getStation2().equals(station2))) {
                    return true;
                }
            }
        }

        return false;
    }

    // Check if a particular network exists, given the key station name and the related value List.
    public boolean hasNetwork(String stationName, List<Station> valueStations) {
        if (network.keySet().contains(stationName)) {
            List<Station> connectingStations = network.get(stationName);
            if (connectingStations.size() == valueStations.size()) {
                for (Iterator<Station> k = valueStations.iterator(); k.hasNext();) {
                    if (!connectingStations.contains(k.next())) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }



    public List<Connection> getLeastDisDirection(String startStationName, String endStationName) {
        // To verify the starting and ending stations both exist.
        if (!this.hasStation(startStationName) || !this.hasStation(endStationName)) {
            throw new RuntimeException("Stations entered do not exist on this subway.");
        }


        List<Connection> route = new LinkedList<>();

        // Dijkstra's algorithm
        // set up data structure
        Map<Station, Station> predecessor = new HashMap<>();
        Map<Station, Boolean> visited = new HashMap<>();
        Map<Station, Integer> weight = new HashMap<>();
        for (Station cur : stations) {
            predecessor.put(cur, null);
            visited.put(cur, false);
            weight.put(cur, Integer.MAX_VALUE);
        }
        Station start = new Station(startStationName);
        weight.put(start, 0);
        Set<Station> processingStations = new HashSet<>();
        processingStations.add(start);


        // run algorithm
        while (!processingStations.isEmpty()) {

            Station temp = findMinWeightStation(processingStations, weight); // return the minimal
            visited.put(temp, true); // mark as visited
            List<Station> neighbors = network.get(temp.getName());// get neighbors
            for (Station neighbor : neighbors) { // for all neighbors
                // if it is linked and it is not visited
                if (!visited.get(neighbor)) {
                    // get Connection between the temp and the neighbor
                    Connection connection = getConnection(temp, neighbor);

                    // if we only need to care about the number of station
                    // then connection.getTime() can be set to 1 directly
                    Integer length = weight.get(temp) + connection.getTime(); // total weight if it
                                                                              // is linked
                    if (weight.get(neighbor).compareTo(length) > 0) {
                        // compare present weight and potential total weight
                        weight.put(neighbor, length);
                        predecessor.put(neighbor, temp);
                        // if it is already in the pq, refresh its weight and pred
                        if (!processingStations.contains(neighbor)) { // if it is not, add it
                            processingStations.add(neighbor);
                        }
                    }
                }
            }

        }


        // retrieve connections
        Station cur = new Station(endStationName);
        Station pre = new Station(predecessor.get(cur).getName());
        while (true) {
            route.add(0, getConnection(pre, cur));
            if (pre.equals(start)) {
                break;
            }
            cur = new Station(pre.getName());
            pre = new Station(predecessor.get(pre).getName());
        }


        return route;
    }



    public List<Connection> getLeastTransferDirection(String startStationName,
                    String endStationName) {
        List<List<Station>> routes = new ArrayList<>();
        ArrayList<Integer> numOfTransferList = new ArrayList<>();
        List<Station> resStations = new ArrayList<>();
        List<Connection> resConnections = new ArrayList<>();
        Map<Station, Boolean> visited = new HashMap<>();
        for (Station station : stations) {
            visited.put(station, false);
        }
        visited.put(new Station(startStationName), true);
        ArrayList<Station> temp = new ArrayList<>();
        temp.add(new Station(startStationName));
        getLeastTransferDirectionHelper(startStationName, endStationName, temp, null,
                        new Station(startStationName), routes, 0, numOfTransferList, visited);

        int minNumOftransfer = Integer.MAX_VALUE;
        int minNumOfStations = Integer.MAX_VALUE;
        for (int i = 0; i < routes.size(); i++) {
            if (numOfTransferList.get(i).compareTo(minNumOftransfer) <= 0) {
                if(numOfTransferList.get(i).compareTo(minNumOftransfer) == 0 && minNumOfStations>routes.get(i).size()) {
                    minNumOfStations = routes.get(i).size();
                    resStations = new ArrayList<>(routes.get(i));
                }else if (numOfTransferList.get(i).compareTo(minNumOftransfer) < 0){
                    resStations = new ArrayList<>(routes.get(i));
                    minNumOftransfer = numOfTransferList.get(i);
                    minNumOfStations = routes.get(i).size();
                }
            }
        }
        
//        System.out.println("minNumOftransfer: "+ minNumOftransfer);

        // retrieve connections
        int curIndex = 0;
        int nextIndex = 1;
        while (nextIndex<resStations.size()) {
            Station curStation = new Station(resStations.get(curIndex).getName());
            Station nextStation = new Station(resStations.get(nextIndex).getName());
            resConnections.add(getConnection(curStation, nextStation));
            curIndex++;
            nextIndex++;
        }
        
        return resConnections;


    }


    private void getLeastTransferDirectionHelper(String startStationName, String endStationName,
                    ArrayList<Station> temp, Station preStation, Station curStation,
                    List<List<Station>> routes, Integer numOfTransfer,
                    ArrayList<Integer> numOfTransferList, Map<Station, Boolean> visited) {
        if (numOfTransfer.compareTo(minTransfer)<=0) {
            if (curStation.getName().equals(endStationName)) {
                routes.add(new ArrayList<>(temp));
                numOfTransferList.add(numOfTransfer);
                minTransfer = Math.min(minTransfer, numOfTransfer);
//                System.out.println("One path added");
//                System.out.println("numOfTransfer: " + numOfTransfer);
//                System.out.println("minTransfer: " + minTransfer);
            } else {
                List<Station> neighbors = network.get(curStation.getName());
                String preLineName = new String();
                if (preStation != null) {
                    Connection preConnection = getConnection(preStation, curStation);
                    preLineName = new String(preConnection.getLineName());
                }
                for (Station neighbor : neighbors) {
                    if (!visited.get(neighbor)) {
                        Connection nextConnection = getConnection(curStation, neighbor);
                        String nextLineName = nextConnection.getLineName();
                        if (preLineName.length() == 0 || preLineName.equals(nextLineName)) {
                            temp.add(neighbor);
                            visited.put(neighbor, true);
                            getLeastTransferDirectionHelper(startStationName, endStationName, temp,
                                            curStation, neighbor, routes, numOfTransfer,
                                            numOfTransferList, visited);
                            temp.remove(temp.size() - 1);
                            visited.put(neighbor, false);
                        } else {
                            temp.add(neighbor);
                            visited.put(neighbor, true);
                            getLeastTransferDirectionHelper(startStationName, endStationName, temp,
                                            curStation, neighbor, routes, numOfTransfer + 1,
                                            numOfTransferList, visited);
                            temp.remove(temp.size() - 1);
                            visited.put(neighbor, false);
                        }
                    }
                }
            }
        }
    }



    private Station findMinWeightStation(Set<Station> processingStations,
                    Map<Station, Integer> weight) {
        Station tempMinStation = null;
        int minDis = Integer.MAX_VALUE;
        for (Station station : processingStations) {
            if (weight.get(station) < minDis) {
                minDis = weight.get(station);
                tempMinStation = new Station(station.getName());
            }
        }
        processingStations.remove(tempMinStation);
        return tempMinStation;
    }

    public List<Connection> getLeastStationsDirections(String startStationName,
                    String endStationName) {
        // To verify the starting and ending stations both exist.
        if (!this.hasStation(startStationName) || !this.hasStation(endStationName)) {
            throw new RuntimeException("Stations entered do not exist on this subway.");
        }

        // This method is based on a well-known bit of code called Dijkstra's algorithm, which
        // figures out the shortest path between two nodes on a graph.
        Station start = new Station(startStationName);
        Station end = new Station(endStationName);
        List<Connection> route = new LinkedList<>();
        List<Station> reachableStations = new LinkedList<>();
        Map<String, Station> previousStations = new HashMap<>();

        // This first part of the code handles the case when the end station is just one connection
        // away from the starting station.
        List<Station> neighbors = network.get(startStationName);

        for (Iterator<Station> i = neighbors.iterator(); i.hasNext();) {
            Station station = (Station) i.next();
            if (station.equals(end)) {
                route.add(getConnection(start, end));
                return route;
            } else {
                previousStations.put(station.getName(), start);
            }
        }

        List<Station> nextStations = new LinkedList<>();
        nextStations.addAll(neighbors);
        Station currentStation = start;
        reachableStations.add(currentStation);

        // These loops begin to iterate through each set of stations reachable by the starting
        // station,
        // and tries to find the least number of stations possible to connect the starting point and
        // the destination.
        searchLoop: for (int i = 1; i < stations.size(); i++) {
            List<Station> tmpNextStations = new LinkedList<>();

            for (Iterator<Station> j = nextStations.iterator(); j.hasNext();) {
                Station station = (Station) j.next();
                reachableStations.add(station);
                currentStation = station;
                List<Station> currentNeighbors = network.get(currentStation.getName());

                for (Iterator<Station> k = currentNeighbors.iterator(); k.hasNext();) {
                    Station neighbor = (Station) k.next();
                    if (!reachableStations.contains(neighbor)) {
                        if (neighbor.equals(end)) {
                            reachableStations.add(neighbor);
                            previousStations.put(neighbor.getName(), currentStation);
                            break searchLoop;
                        } else {
                            reachableStations.add(neighbor);
                            tmpNextStations.add(neighbor);
                            previousStations.put(neighbor.getName(), currentStation);
                        }
                    }
                }
            }

            nextStations = tmpNextStations;
        }

        // We've found the path by now.
        boolean keepLooping = true;
        Station keyStation = end;
        Station station;

        // Once we've got a path, we just "unwind" the path, and create a List of connections to get
        // from the starting station to the destination station.
        while (keepLooping) {
            station = (Station) previousStations.get(keyStation.getName());
            route.add(0, getConnection(station, keyStation));
            if (start.equals(station)) {
                keepLooping = false;
            }
            keyStation = station;
        }

        return route;
    }

    // This is a utility method that takes two stations, and looks for a connection between them (on
    // any line).
    private Connection getConnection(Station station1, Station station2) {
        for (Iterator<Connection> i = connections.iterator(); i.hasNext();) {
            Connection connection = (Connection) i.next();
            Station one = connection.getStation1();
            Station two = connection.getStation2();
            if ((station1.equals(one)) && (station2.equals(two))) {
                return connection;
            }
        }

        return null;
    }



}
