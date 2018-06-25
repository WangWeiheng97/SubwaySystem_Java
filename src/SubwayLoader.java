import java.io.*;

public class SubwayLoader {
    private Subway subway;

    public SubwayLoader() {
        this.subway = new Subway();
    }

    public Subway loadFromFile(File subwayFile) throws IOException {
//        InputStreamReader isr = new InputStreamReader(new FileInputStream(subwayFile), "GBK");
//        BufferedReader reader = new BufferedReader(isr);
        BufferedReader reader = new BufferedReader(new FileReader(subwayFile));

        loadStations(subway, reader);

        String lineName = reader.readLine();

        while ((lineName != null) && (lineName.length() > 0)) {           
            loadLine(subway, reader, lineName);
            lineName = reader.readLine();
        }

        return subway;
    }

    private void loadStations(Subway subway, BufferedReader reader) throws IOException {
        String currentStation;
        currentStation = reader.readLine();

        while (currentStation.length() > 0) {        
            subway.addStation(currentStation);
            currentStation = reader.readLine();
        }
    }

    private void loadLine(Subway subway, BufferedReader reader, String lineName)
                    throws IOException {
        String station1Name, station2Name,timeString;
        station2Name = "";
        int time = 0;
        station1Name = reader.readLine();
        timeString=reader.readLine();
       
        if((timeString != null) && (timeString.length() > 0)) {
            station2Name = reader.readLine();
            
            try {
                time = Integer.parseInt(timeString);
            } catch(NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        while ((timeString != null) && (timeString.length() > 0)) {
            subway.addConnection(station1Name, station2Name, lineName, time);
            station1Name = station2Name;
            timeString=reader.readLine();
            
            if((timeString != null) && (timeString.length() > 0)) {
                station2Name = reader.readLine();
                
                try {
                    time = Integer.parseInt(timeString);
                } catch(NumberFormatException e) {
                    e.printStackTrace();
                }
            }          
        }
    }
}
