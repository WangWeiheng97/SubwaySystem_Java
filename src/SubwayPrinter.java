import java.io.*;
import java.util.List;

public class SubwayPrinter {
    private PrintStream out;

    public SubwayPrinter(OutputStream out) {
        this.out = new PrintStream(out);
    }

    public void printDirections(List<Connection> route) {
        Connection conneciton = (Connection) route.get(0);
        String currentLine = conneciton.getLineName();
        String previousLine = currentLine;

        // We begin by printing the starting station...
        out.println("Start out at " + conneciton.getStation1().getName() + ".");

        // ...and the first line to get on, as well as the next station to travel towards.
        out.println("Get on the " + currentLine + " heading towards "
                        + conneciton.getStation2().getName() + ".");

        for (int i = 1; i < route.size(); i++) {
            conneciton = (Connection) route.get(i);
            currentLine = conneciton.getLineName();
            if (currentLine.equals(previousLine)) {
                out.println(" Continue past " + conneciton.getStation1().getName() + "...");
            } else {
                out.println("When you get to " + conneciton.getStation1().getName()
                                + ", get off the " + previousLine + ".");
                out.println("Switch over to the " + currentLine + ", heading towards "
                                + conneciton.getStation2().getName() + ".");
                previousLine = currentLine;
            }
        }

        out.println("Get off at " + conneciton.getStation2().getName() + " and enjoy yourself!");
    }

}
