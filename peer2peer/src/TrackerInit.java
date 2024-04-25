import tracker.Tracker;

import java.io.IOException;

public class TrackerInit {
    /*Class that initializes a Tracker in this Machine*/
    public static void main(String[] args) {
        try {
            Tracker tracker = new Tracker(12345); // to port
            tracker.start(); // start ton tracker server
            System.out.println("Tracker started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
