package peer;

import models.UploadedFile;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.RandomAccessFile;

public class PeerRequestHandler extends Thread {
    /*Will be used mostly when sending files to another peer*/
    ObjectInputStream in;
    ObjectOutputStream out;

    String sharedDirPath;
    String sender;

    public PeerRequestHandler(Socket req, String path) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.sender =  req.getRemoteSocketAddress().toString();
        this.sharedDirPath = path;
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
        HashMap<String, byte[]> response = new HashMap<>();
        // first validate the byte range (startByte and endByte) to ensure they are within the bounds of the file size.
        //Then, we use file.seek(startByte) to move the file pointer to the start position of the desired fragment.
        //We read the specified fragment size into a byte array using file.readFully(fragment)
        RandomAccessFile file = null;
        try {
            String filepath = this.sharedDirPath + File.separator + request.get("filename");
            long startByte = Long.parseLong(request.get("startByte"));
            long endByte = Long.parseLong(request.get("endByte"));

            Path path = Paths.get(filepath);
            long fileSize = Files.size(path);

            if (startByte < 0 || endByte > fileSize || startByte > endByte) {
                throw new IllegalArgumentException("Invalid byte range");
            }

            file = new RandomAccessFile(filepath, "r");
            file.seek(startByte);

            int fragmentSize = (int) (endByte - startByte);
            byte[] fragment = new byte[fragmentSize];
            file.readFully(fragment);

            response.put("file", fragment);
            System.out.println("Download request for file " + request.get("filename") + " is successful");

        } catch (Exception e) {
            System.out.println("Download request for file " + request.get("filename") + " failed: " + e.getMessage());
            response.put("file", null);

        } finally {
            if (file != null) {
                file.close();
            }
            out.writeObject(response);
        }
    }


    public void handleCheckActive() throws IOException {
        HashMap<String, String> response = new HashMap<>();
        response.put("active", "true");
        out.writeObject(response);
    }
}
