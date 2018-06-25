
public class Station {

    private String name;

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        // Two stations are equal if they have the same name, ignoring cases.
        if (obj instanceof Station) {
            Station otherStation = (Station) obj;
            if (otherStation.getName().equals(name)) {
                return true;
            }
            /*if (otherStation.getName().equalsIgnoreCase(name)) {
                return true;
            }*/
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Override hasCode() when overriding equals() to ensure correct comparisons.
        //return name.toLowerCase().hashCode();
        return name.hashCode();
    }



}


