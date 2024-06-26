package peer;

import models.OnlineUser;
import models.UploadedFile;
import models.User;
import tracker.TrackerRequestHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import models.UploadedFile;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.nio.file.*;



public class Peer extends Thread {
    private String name;
    private String password;
    private ServerSocket server;
    private String sharedDirPath;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private int localPort;
    private String trackerAddress;
    private static final int tracker_port = 12345; //to allazo me to actual port

    public Peer(String name, String password, String sharedDirPath, String trackerAddress) throws IOException {
        this.name = name;
        this.password = password;
        this.server = new ServerSocket();
        this.sharedDirPath = sharedDirPath;
        this.trackerAddress = trackerAddress;
        // Create shared directory if it doesn't exist
        File sharedDir = new File(sharedDirPath);
        if (!sharedDir.exists()) {
            sharedDir.mkdirs(); // The mkdirs() method is used to create the directory specified by the File object. If the directory already exists, it will do nothing and return false.
        }
        // Connect to the tracker
    }

    private void initializeSocket(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

    }

    public void run(){
        this.openPeerServer();
    }

    private void openPeerServer(){
        Socket req = null;
        try {
            /* Create Server Socket */
            server = new ServerSocket(0);
            this.localPort = server.getLocalPort();
            System.out.println(this.name + ": Setting up peer server @ port" + localPort );

            while (true) {
                /* Accept the connection */
                req = server.accept();
                Thread reqThread = new PeerRequestHandler(req, this.sharedDirPath);
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

    public String register() {
        try {
            initializeSocket(trackerAddress, tracker_port);
            // Send registration request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "register");
            request.put("username", name);
            request.put("password", password);
            request.put("port", Integer.toString(localPort) );

            out.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) in.readObject();

            // Check response from tracker
            if (response.get("message").equals("Successfully registered") ) {
                return "Registration successful.";
            } else {
                return "Registration failed: " + response.get("message");
            }
        } catch (IOException e) {
            return "Registration failed: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String logIn() {
        try {
            initializeSocket(trackerAddress, tracker_port);


            // Send login request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logIn");
            request.put("username", name);
            request.put("password", password);
            request.put("port", Integer.toString(localPort) );

            out.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) in.readObject();

            // Check response from tracker
            if (response.get("message").equals("Successfully logged in") ) {
                return "Login successful.";
            } else {
                return "Failed to login: " + response.get("message");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            //return "Login failed: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String logOut() {
        try {
            initializeSocket(trackerAddress, tracker_port);


            // Send logout request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logOut");
            request.put("username", name);

            out.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) in.readObject();

            // Check response from tracker
            if (response.get("message").equals("Successfully logged out") ) {
                return "Logout successful.";
            } else {
                return "Logout failed: " + response.get("message");
            }
        } catch (IOException e) {
            return "Logout failed: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
//    public void loadInitialFiles() {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("fileDownloadList.txt"));
//            String line;
//            while ((line = reader.readLine()) != null) { // sinexizei oso den einai null
//                String filePath = sharedDirPath + File.separator + line;
//                //On Windows, for example, the file separator is \, while on Unix-based systems (such as Linux and macOS), it is /.
//                //By using File.separator, your code becomes platform-independent because it adapts to the correct file separator character based on the underlying operating system.
//                // This ensures that your file paths are correctly formatted regardless of the platform on which your code is running ( to brika sto SO)
//                File file = new File(filePath);
//                if (file.exists()) {
//                    // an uparxei sinexizei sto epomeno
//                    continue;
//                }
//
//                // create a file ( einai empty mporei na thelei allagi edo)
//                file.createNewFile();
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }

    public String uploadFileNames() {
        int i = 0;
        final File folder = new File(this.sharedDirPath);
        File[] filesInDir = folder.listFiles();
        for (File fileEntry : filesInDir) {
            try {
                if (!fileEntry.isDirectory()) {
                    initializeSocket(trackerAddress, tracker_port);
                    HashMap<String, String> req = new HashMap<>();
                    req.put("type", "uploadFileName");
                    req.put("username", name);
                    req.put("filename", fileEntry.getName());
                    out.writeObject(req);
                    HashMap<String, String> response = (HashMap<String, String>) in.readObject();
                    if (response.get("message").equals("Success")) {
                        i++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Uploaded " + i + " files";
    }

    public ArrayList<String> list() {
        try {
            initializeSocket(trackerAddress, tracker_port);


            // stelnei request sto tracker gia ta available list
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "listRequest");
            out.writeObject(request);

            HashMap<String, ArrayList<String>> response = (HashMap<String, ArrayList<String>>) in.readObject();
            return  response.get("fileList");
        } catch (IOException e) {
            return new ArrayList<String>();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String downloadFile(String filename){
        UploadedFile file = details(filename);

        if (file == null){
            return "No file with this name";
        }

        System.out.println("Users with file " + file.getName() + " : " + file.getUsersWithFile());
        OnlineUser best = findBestPeer(file);
        System.out.println("Best peer: " + best.getUsername());
        String downloadMessage = simpleDownload(filename, best);
        uploadFileNames();
        return downloadMessage;
    }


    public UploadedFile details(String fileName) {
        try {
            initializeSocket(trackerAddress, tracker_port);


            // stelnei request ston tracker gia plirofories enos sugkekrimenou arxeiou
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "detailsRequest");
            request.put("filename", fileName);
            out.writeObject(request);

            HashMap<String, UploadedFile> response = (HashMap<String, UploadedFile>) in.readObject();
            return response.get("details");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public String checkActive(OnlineUser peer) {
        try {
            System.out.println(peer.getAddress() + " " + peer.getPort() + " " + peer.getUsername());
            initializeSocket(peer.getAddress(), peer.getPort());

            // stelenei request ston tracker na dei an kapoios peer einai active
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "checkActive");
            out.writeObject(request);

            HashMap<String, String> response = (HashMap<String, String>) in.readObject();

            if (response.get("active").equals("true")) {
                return ("Peer " + peer.getUsername() + " is active.");
            } else {
                return ("Peer " + peer.getUsername() + " is not active.");
            }
        } catch (Exception e) {
            return ("Peer " + peer.getUsername() + " is not active.");
        }
    }

    public OnlineUser findBestPeer(UploadedFile file){
        double max = POSITIVE_INFINITY;
        OnlineUser bestUser = new OnlineUser();

        for (OnlineUser u: file.getUsersWithFile()){

            if (u.getUsername().equals(name)){
                continue;
            }

            long begin = System.currentTimeMillis();
            String msg = checkActive(u);
            long end = System.currentTimeMillis();
            if (msg.contains("is active.")){
                double score = (end - begin) * (Math.pow(0.75, u.getCountDownloads()) *  Math.pow(1.25, u.getCountFailures()) );
                System.out.println("Peer " + u.getUsername() + " has score: "  + score);

                if (score < max){
                    max = score;
                    bestUser = u;
                }
            }
        }
        return bestUser;
    }

    public String simpleDownload(String filename, OnlineUser peer){
        try {
            //System.out.println(peer.getAddress()+ " fefe " + peer.getPort());
            initializeSocket(peer.getAddress(), peer.getPort());

            HashMap<String, String> request = new HashMap<>();
            request.put("type", "simpleDownload");
            request.put("filename", filename);
            out.writeObject(request);

            HashMap<String, byte[]> response = (HashMap<String, byte[]>) in.readObject();
            byte[] rawFile =  response.get("file");

            if (rawFile == null){
                //System.out.println(this.updateFailureCount(peer.getUsername()));
                return  "Failed to download file";
            }
            this.writeFile(filename, response.get("file"));
            //System.out.println(this.updateDownloadCount(peer.getUsername()));

            return "Downloaded file " + filename;
        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return "";
            //return "Failed to download file " + filename;
        }
    }

    private void writeFile(String filename, byte[] content) throws IOException {
        File file = new File(this.sharedDirPath + File.separator + filename);
        OutputStream os = new FileOutputStream(file);
        os.write(content);
    }

    private String updateFailureCount(String username) throws IOException, ClassNotFoundException{
        initializeSocket(trackerAddress, tracker_port);
        HashMap<String, String> request = new HashMap<>();
        request.put("type", "updateFailureCount");
        request.put("username", username);
        out.writeObject(request);

        HashMap<String, String> response = (HashMap<String, String>) in.readObject();
        return response.get("message");
    }

    private String updateDownloadCount(String username) throws IOException, ClassNotFoundException {
        initializeSocket(trackerAddress, tracker_port);
        HashMap<String, String> request = new HashMap<>();
        request.put("type", "updateDownloadCount");
        request.put("username", username);
        out.writeObject(request);

        HashMap<String, String> response = (HashMap<String, String>) in.readObject();
        return response.get("message");
    }

    public String getUsername() {
        return name;
    }

    // Select a random file that the peer does not have
    public String selectRandomFile() {
        List<String> allFiles = list();
        File sharedDir = new File(sharedDirPath);
        Set<String> localFiles = new HashSet<>(Arrays.asList(sharedDir.list()));
        List<String> missingFiles = new ArrayList<>();
        for (String file : allFiles) {
            if (!localFiles.contains(file)) {
                missingFiles.add(file);
            }
        }
        if (missingFiles.isEmpty()) {
            return null; // All files are already downloaded
        }
        Random rand = new Random();
        return missingFiles.get(rand.nextInt(missingFiles.size()));
    }


    // Get users that have fragments of the specified file
    public HashMap<Integer, List<OnlineUser>> getUsersWithFragment(String filename) {
        UploadedFile file = details(filename);
        if (file == null) {
            return new HashMap<>();
        }
        HashMap<Integer, List<OnlineUser>> usersWithFragments = new HashMap<>();
        for (int i = 0; i < file.getFragmentCount(); i++) {
            usersWithFragments.put(i, new ArrayList<>());
        }
        for (OnlineUser user : file.getUsersWithFile()) {
            for (int i = 0; i < file.getFragmentCount(); i++) {
                if (user.hasFragment(i)) { // Corrected to only use fragment number
                    usersWithFragments.get(i).add(user);
                }
            }
        }
        return usersWithFragments;
    }

    // Assemble the file from its fragments
    public void assembleFile(String filename, byte[][] fragments) throws IOException {
        File outputFile = new File(sharedDirPath + File.separator + filename);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (byte[] fragment : fragments) {
                fos.write(fragment);
            }
        }
    }

    // Download a specific fragment from a peer
    public void downloadFragment(String filename, int fragmentNumber, OnlineUser receiver) {
        try {
            initializeSocket(receiver.getAddress(), receiver.getPort());
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "simpleDownload");
            request.put("filename", filename);
            request.put("fragmentNumber", Integer.toString(fragmentNumber));
            out.writeObject(request);
            HashMap<String, byte[]> response = (HashMap<String, byte[]>) in.readObject();
            byte[] fragment = response.get("file");
            if (fragment != null) {
                // Save the fragment locally (Implement the method saveFragment)
                saveFragment(filename, fragmentNumber, fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save a fragment locally
    private void saveFragment(String filename, int fragmentNumber, byte[] fragment) throws IOException {
        Path fragmentPath = Paths.get(sharedDirPath, filename + ".part" + fragmentNumber);
        Files.write(fragmentPath, fragment);
    }
}