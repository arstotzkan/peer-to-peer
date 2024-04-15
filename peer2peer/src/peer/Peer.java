package peer;

import models.UploadedFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Peer extends Thread {
    private String name;
    private String password;
    private ServerSocket server;
    private String sharedDirPath;
    private String tokenId; // meta to login pairno auto
    private Socket trackerSocket;
    private ObjectOutputStream trackerWriter;
    private ObjectInputStream trackerReader;
    private static final int tracker_port = 12345; //to allazo me to actual port

    public Peer(String name, String password, int port, String sharedDirPath) throws IOException {
        this.name = name;
        this.password = password;
        this.server = new ServerSocket(port);
        this.sharedDirPath = sharedDirPath;

        // Create shared directory if it doesn't exist
        File sharedDir = new File(sharedDirPath);
        if (!sharedDir.exists()) {
            sharedDir.mkdirs(); // The mkdirs() method is used to create the directory specified by the File object. If the directory already exists, it will do nothing and return false.
        }

        // Connect to the tracker
        trackerSocket = new Socket("tracker_address", tracker_port); // Replace kai edo
        trackerWriter = new ObjectOutputStream(trackerSocket.getOutputStream());
        trackerReader = new ObjectInputStream(trackerSocket.getInputStream());
    }

    public void register() {
        try {
            // Send registration request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "register");
            request.put("username", name);
            request.put("password", password);

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully registered") ) {
                System.out.println("Registration successful.");
            } else {
                System.out.println("Registration failed: " + response.get("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void logIn() {
        try {
            // Send login request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logIn");
            request.put("username", name);
            request.put("password", password);

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully logged in") ) {
                System.out.println("Registration successful.");
            } else {
                System.out.println("Registration failed: " + response.get("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void logOut() {
        try {
            // Send logout request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logOut");
            request.put("username", name);

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully logged out") ) {
                System.out.println("Registration successful.");
            } else {
                System.out.println("Registration failed: " + response.get("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadInitialFiles() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("fileDownloadList.txt"));
            String line;
            while ((line = reader.readLine()) != null) { // sinexizei oso den einai null
                String filePath = sharedDirPath + File.separator + line;
                //On Windows, for example, the file separator is \, while on Unix-based systems (such as Linux and macOS), it is /.
                //By using File.separator, your code becomes platform-independent because it adapts to the correct file separator character based on the underlying operating system.
                // This ensures that your file paths are correctly formatted regardless of the platform on which your code is running ( to brika sto SO)
                File file = new File(filePath);
                if (file.exists()) {
                    // an uparxei sinexizei sto epomeno
                    continue;
                }

                // create a file ( einai empty mporei na thelei allagi edo)
                file.createNewFile();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void list() {
        try {
            // stelnei request sto tracker gia ta available list
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "listRequest");
            trackerWriter.writeObject(request);

            HashMap<String, ArrayList<String>> response = (HashMap<String, ArrayList<String>>) trackerReader.readObject();
            System.out.println("Available files: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void details(String fileName) {
        try {
            // stelnei request ston tracker gia plirofories enos sugkekrimenou arxeiou
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "listRequest");
            request.put("filename", fileName);
            trackerWriter.writeObject(request);

            HashMap<String, UploadedFile> response = (HashMap<String, UploadedFile>) trackerReader.readObject();
            System.out.println("Details for file " + fileName + ": " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkActive(String peerIP, int peerPort) {
        try {
            // stelenei request ston tracker na dei an kapoios peer einai active
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "checkActive");

            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            if (response.get("active").equals("true")) {
                System.out.println("Peer " + peerIP + ":" + peerPort + " is active.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Peer " + peerIP + ":" + peerPort + " is not active.");
        }


    }
}
