import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.FileReader;



public class Peer extends Thread {
    private String name;
    private String password;
    private ServerSocket server;
    private String sharedDirPath;
    private String tokenId; // meta to login pairno auto
    private Socket trackerSocket;
    private DataOutputStream trackerWriter;
    private BufferedReader trackerReader;
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
        trackerWriter = new DataOutputStream(trackerSocket.getOutputStream());
        trackerReader = new BufferedReader(new InputStreamReader(trackerSocket.getInputStream()));
    }

    public void register() {
        try {
            // Send registration request to tracker
            trackerWriter.writeBytes("REGISTER " + name + " " + password + "\n");
            String response = trackerReader.readLine();

            // Check response from tracker
            if (response.startsWith("SUCCESS")) { // tha prepei to mnma apo tracker na arxizei me succes ( gia tin ulopoiisi)
                System.out.println("Registration successful.");
            } else {
                System.out.println("Registration failed: " + response.substring(8)); // Error message from tracker , to (8) einai gia na ksekinisi meta to erro( prepei na ginoun ston tracker auta)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logIn() {
        try {
            // Send login request to tracker
            trackerWriter.writeBytes("LOGIN " + name + " " + password + "\n");
            String response = trackerReader.readLine();

            // Check response from tracker
            if (response.startsWith("TOKEN")) { // idia logiki me register edo
                tokenId = response.substring(6); // pairnei to token
                System.out.println("Login successful. Token ID: " + tokenId);
            } else {
                System.out.println("Login failed: " + response.substring(6)); // Error message from tracker. same
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut() {
        try {
            // Send logout request to tracker
            trackerWriter.writeBytes("LOGOUT " + tokenId + "\n");
            String response = trackerReader.readLine();

            // Check response from tracker
            if (response.startsWith("SUCCESS")) {
                System.out.println("Logout successful.");
                tokenId = null; // Clear token ID meta to log out
            } else {
                System.out.println("Logout failed: " + response.substring(8)); // Error message from tracker
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            trackerWriter.writeBytes("LIST\n");
            String response = trackerReader.readLine();
            System.out.println("Available files: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void details(String fileName) {
        try {
            // stelnei request ston tracker gia plirofories enos sugkekrimenou arxeiou
            trackerWriter.writeBytes("DETAILS " + fileName + "\n");
            String response = trackerReader.readLine();
            System.out.println("Details for file " + fileName + ": " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkActive(String peerIP, int peerPort) {
        try {
            // stelenei request ston tracker na dei an kapoios peer einai active
            trackerWriter.writeBytes("CHECK_ACTIVE " + peerIP + " " + peerPort + "\n");
            String response = trackerReader.readLine();
            if (response.startsWith("ACTIVE")) {
                System.out.println("Peer " + peerIP + ":" + peerPort + " is active.");
            } else {
                System.out.println("Peer " + peerIP + ":" + peerPort + " is not active.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
