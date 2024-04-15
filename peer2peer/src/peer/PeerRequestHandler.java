package peer;

import models.UploadedFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PeerRequestHandler extends Thread {
    /*Will be used mostly when sending files to another peer*/
    ObjectInputStream in;
    ObjectOutputStream out;
    String sender;

    public PeerRequestHandler(Socket req) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.sender =  req.getRemoteSocketAddress().toString();
    }

    public void run() {
        HashMap<String,String> request = null;
        try {
            request = (HashMap<String,String>) in.readObject();
            String message = request.get("type");

            switch (message) {
                case "simpleDownload":
                    this.handleSimpleDownload(request);
                    break;
                case "checkActive":
                    this.handleCheckActive();
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSimpleDownload(HashMap<String,String> request) throws IOException {
        //TODO: handle not existing file
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
