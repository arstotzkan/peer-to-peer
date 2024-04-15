package peer;

import models.UploadedFile;
import tracker.TrackerMemory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PeerRequestHandler {
    /*Will be used mostly when sending files to another peer*/
    ObjectInputStream in;
    ObjectOutputStream out;
    String sender;
    TrackerMemory memory;

    public PeerRequestHandler(Socket req , TrackerMemory mem) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.sender =  req.getRemoteSocketAddress().toString();
        this.memory = mem;
    }

    public void run() throws IOException, ClassNotFoundException {
        HashMap<String,String> request = (HashMap<String,String>) in.readObject();
        String message = request.get("type");

        switch (message) {
            case "simpleDownload":
                this.handleSimpleDownload(request);
                break;
            case "checkActive":
                this.handleCheckActive();
                break;
        }
    }

    public void handleSimpleDownload(HashMap<String,String> request) throws IOException {
        String filename = request.get("filename");
        Path path = Paths.get(filename);
        byte[] content = Files.readAllBytes(path);

        HashMap<String, byte[]> response = new HashMap<>();
        response.put("file", content);
        out.writeObject(response);

    }

    public void handleCheckActive() throws IOException {
        HashMap<String, String> response = new HashMap<>();
        response.put("active", "true");
        out.writeObject(response);
    }
}
