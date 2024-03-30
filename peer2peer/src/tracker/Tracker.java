package tracker;

import java.io.IOException;
import java.net.ServerSocket;

public class Tracker extends Thread{

    ServerSocket server;

    public Tracker(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    public void run(){
        /*Run server and handle requests*/
    }
}
