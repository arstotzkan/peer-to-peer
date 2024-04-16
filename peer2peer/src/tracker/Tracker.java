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
            server = new ServerSocket(12345, 100); //socket for users

            while (true) {
                /* Accept the connection */
                req = server.accept();
                Thread reqThread = new TrackerRequestHandler(req, this.memory);
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
