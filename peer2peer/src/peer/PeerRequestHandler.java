package peer;

import models.UploadedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

public class PeerRequestHandler extends Thread {
    /* Will be used mostly when sending files to another peer */
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String sharedDirPath;
    private String sender;

    public PeerRequestHandler(Socket req, String path) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.sender = req.getRemoteSocketAddress().toString();
        this.sharedDirPath = path;
    }

    public void run() {
        try {
            HashMap<String, String> request = (HashMap<String, String>) in.readObject();
            String message = request.get("type");

            switch (message) {
                case "simpleDownload":
                    handleSimpleDownload(request);
                    break;
                case "checkActive":
                    handleCheckActive();
                    break;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSimpleDownload(HashMap<String, String> request) throws IOException {
        HashMap<String, byte[]> response = new HashMap<>();

        try {
            // ruquest gia retrieve filename kai to number
            String baseFilename = request.get("filename");
            int fragmentNumber = Integer.parseInt(request.get("fragmentNumber"));

            // olo to path gia to fragment
            String fragmentFilename = this.sharedDirPath + File.separator + baseFilename + ".part" + fragmentNumber;
            File fragmentFile = new File(fragmentFilename);

            if (!fragmentFile.exists()) {
                throw new FileNotFoundException("Fragment file not found");
            }

            // Read
            byte[] fragment = Files.readAllBytes(fragmentFile.toPath());

            response.put("file", fragment);
            System.out.println("Download request for file " + fragmentFilename + " is successful");

        } catch (Exception e) {
            System.out.println("Download request for file " + request.get("filename") + " failed: " + e.getMessage());
            response.put("file", null);

        } finally {
            out.writeObject(response);
        }
    }

    public void handleCheckActive() throws IOException {
        HashMap<String, String> response = new HashMap<>();
        response.put("active", "true");
        out.writeObject(response);
    }
}
