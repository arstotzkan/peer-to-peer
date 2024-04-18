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


public class Peer extends Thread {
    private String name;
    private String password;
    private ServerSocket server;
    private String sharedDirPath;
    private String tokenId; // meta to login pairno auto
    private Socket trackerSocket;
    private ObjectOutputStream trackerWriter;
    private ObjectInputStream trackerReader;

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

    private void createSocket() throws IOException {
        trackerSocket = new Socket(trackerAddress, tracker_port);
        trackerWriter = new ObjectOutputStream(trackerSocket.getOutputStream());
        trackerReader = new ObjectInputStream(trackerSocket.getInputStream());

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
            System.out.println(this.name + ": Setting up peer server @port" + localPort );

            System.out.println(getUsername() + ": " +register());
            System.out.println(getUsername() + ": " +logIn());
            System.out.println(getUsername() + ": " +uploadFileNames() );

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
            createSocket();
            // Send registration request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "register");
            request.put("username", name);
            request.put("password", password);
            request.put("port", Integer.toString(localPort) );

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully registered") ) {
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
            createSocket();

            // Send login request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logIn");
            request.put("username", name);
            request.put("password", password);
            request.put("port", Integer.toString(localPort) );

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully logged in") ) {
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
            createSocket();

            // Send logout request to tracker
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "logOut");
            request.put("username", name);

            trackerWriter.writeObject(request);
            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

            // Check response from tracker
            if (response.get("message").equals("Succesfully logged out") ) {
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

    public String uploadFileNames(){

//        ArrayList<String> files = new ArrayList<>();
        int i = 0;
        final File folder = new File(this.sharedDirPath);

        File[] filesInDir = folder.listFiles();
        for (int j =0; j < filesInDir.length; j++ ) {
            try{
                File fileEntry = filesInDir[j];
                if (!fileEntry.isDirectory()) {
                    createSocket();

                    HashMap<String, String> req = new HashMap<>();
                    req.put("type", "uploadFileName");
                    req.put("username", name);
                    req.put("filename", fileEntry.getName() );
                    trackerWriter.writeObject(req);

                    HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();
                    if (response.get("message").equals("Success") ) {
                        i++;
                    }

                }
            }catch(Exception e){

            }

        }
        return "Uploaded " + i + " files";
    }

    public ArrayList<String> list() {
        try {
            createSocket();

            // stelnei request sto tracker gia ta available list
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "listRequest");
            trackerWriter.writeObject(request);

            HashMap<String, ArrayList<String>> response = (HashMap<String, ArrayList<String>>) trackerReader.readObject();
            return  response.get("fileList");
        } catch (IOException e) {
            return new ArrayList<String>();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String downloadFile(String filename){
        UploadedFile file = details(filename);
        System.out.println("Users with file " + file.getName() + " : " + file.getUsersWithFile());
        OnlineUser best = findBestPeer(file);
        System.out.println("Best peer: " + best.getUsername());
        String downloadMessage = simpleDownload(filename, best);
        uploadFileNames();
        return downloadMessage;
    }


    public UploadedFile details(String fileName) {
        try {
            createSocket();

            // stelnei request ston tracker gia plirofories enos sugkekrimenou arxeiou
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "detailsRequest");
            request.put("filename", fileName);
            trackerWriter.writeObject(request);

            HashMap<String, UploadedFile> response = (HashMap<String, UploadedFile>) trackerReader.readObject();
            return response.get("details");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public String checkActive(OnlineUser peer) {
        try {
            Socket s = new Socket(peer.getAddress(), peer.getPort());
            trackerWriter = new ObjectOutputStream(s.getOutputStream());
            trackerReader = new ObjectInputStream(s.getInputStream());


            // stelenei request ston tracker na dei an kapoios peer einai active
            HashMap<String, String> request = new HashMap<>();
            request.put("type", "checkActive");
            trackerWriter.writeObject(request);

            HashMap<String, String> response = (HashMap<String, String>) trackerReader.readObject();

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
            Socket s = new Socket(peer.getAddress(), peer.getPort());
            trackerWriter = new ObjectOutputStream(s.getOutputStream());
            trackerReader = new ObjectInputStream(s.getInputStream());


            HashMap<String, String> request = new HashMap<>();
            request.put("type", "simpleDownload");
            request.put("filename", filename);
            trackerWriter.writeObject(request);

            HashMap<String, byte[]> response = (HashMap<String, byte[]>) trackerReader.readObject();
            this.writeFile(filename, response.get("file"));

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

    public String getUsername() {
        return name;
    }
}
