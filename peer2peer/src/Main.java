import peer.CreatePeers;
import tracker.TrackerInit;

public class Main {
    /*maybe setting up everything here will be useful for development*/
    public static void main(String[] args) {
        try{
            String[] a = {"127.0.0.1"};
            TrackerInit.main();
            CreatePeers.main(a);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}