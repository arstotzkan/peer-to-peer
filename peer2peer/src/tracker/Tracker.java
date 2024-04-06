package tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Tracker extends Thread{

    ServerSocket server;
    TrackerMemory memory = new TrackerMemory();

    public Tracker(int port) throws IOException {
        this.server = new ServerSocket(port);
    }

    public void run(){
        this.openServer();
    }
    public void openServer() {
        Socket req = null;
        try {
            /* Create Server Socket */
            server = new ServerSocket(60000, 100); //socket for users
            System.out.println("File processing ready...");

            while (true) {
                /* Accept the connection */
                req = server.accept();
                Thread reqThread = new TrackerRequestHandler(req, this.memory); //TODO: add message from request (somehow)
                reqThread.start();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                req.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
