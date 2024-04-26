package tracker;

import models.OnlineUser;
import models.UploadedFile;
import models.User;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackerRequestHandler extends Thread{
    /*WIP, handle requests*/
    ObjectInputStream in;
    ObjectOutputStream out;
    String senderAddress;
    TrackerMemory memory;

    public TrackerRequestHandler(Socket req , TrackerMemory mem) throws IOException {
        this.out = new ObjectOutputStream(req.getOutputStream());
        this.in = new ObjectInputStream(req.getInputStream());
        this.senderAddress =  req.getInetAddress().getHostAddress();
        this.memory = mem;
    }

    public void run(){
        try {
            HashMap<String,String> request = (HashMap<String,String>) in.readObject();
            String message = request.get("type");

            switch (message){
                case "register":
                    this.handleRegister(request);
                    break;
                case "logIn":
                    this.handleLogIn(request);
                    break;
                case "logOut":
                    this.handleLogOut(request);
                    break;
                case "listRequest":
                    this.handleListRequest(request);
                    break;
                case "detailsRequest":
                    this.handleDetailsRequest(request);
                    break;
                case "uploadFileName":
                    this.handleUploadRequest(request);
                    break;
                case "updateDownloadCount":
                    this.updateDownloadCount(request);
                    break;
                case "updateFailureCount":
                    this.updateFailureCount(request);
                    break;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleRegister(HashMap<String,String> request) throws IOException {

        // elegxos oti an yparxei idi o xristis
        String username = request.get("username");
        int peerPort = Integer.parseInt(request.get("port"));
        if (this.memory.getUser(username) != null) {

            HashMap<String, String> response = new HashMap<>();
            System.out.println("Failed registration for peer " + username + ": User with this username already exists");
            response.put("message", "User with this username already exists");
            out.writeObject(response);
            return;
        }

        User newUser = new User(request.get("username"), request.get("password") );

        OnlineUser loggedIn = new OnlineUser(request.get("username"),request.get("password"), "", this.senderAddress, peerPort);

        this.memory.addUser(newUser);
        this.memory.addOnlineUser(loggedIn);

        HashMap<String, String> response = new HashMap<>();
        System.out.println("Peer " + username + " was successfully registered");

        response.put("message", "Successfully registered");
        out.writeObject(response);
    }
    public void handleLogIn(HashMap<String,String> request) throws IOException {
        //TODO: function to generate tokenID

        //elegxo an yparxei o xristis
        String username = request.get("username");
        String password = request.get("password");
        User user = this.memory.getUser(username);

        if (user == null || !password.equals(user.getPassword())) {
            HashMap<String, String> response = new HashMap<>();
            String msg = (user == null) ? "User does not exist" : "Invalid credentials";
            System.out.println("Login failed for peer" +  username + ": " + msg);

            response.put("message", msg);
            out.writeObject(response);
            return;
        }

        int peerPort = Integer.parseInt(request.get("port"));
        OnlineUser loggedIn = new OnlineUser(request.get("username"),request.get("password"), "", this.senderAddress, peerPort);
        this.memory.removeOnlineUser(username); //avoid duplicate online users
        this.memory.removeUserFromFileRegistry(username);

        this.memory.addOnlineUser(loggedIn);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Successfully logged in");
            System.out.println("Successful login for peer " + username);

        out.writeObject(response);
    }
    public void handleLogOut(HashMap<String,String> request) throws IOException {
       // elegxei an einai login o xristis
        String username = request.get("username");
        if (this.memory.getOnlineUser(username) == null) {

            HashMap<String, String> response = new HashMap<>();
            response.put("message", "User is not logged in");
            System.out.println("Failed logout for peer " + username + ": User is not logged in");
            out.writeObject(response);
            return;
        }

        this.memory.removeOnlineUser(username);
        this.memory.removeUserFromFileRegistry(username);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Successfully logged out");
        System.out.println("Successful logout for peer " + username);

        out.writeObject(response);
    }

    public void handleListRequest(HashMap<String,String> request) throws IOException {
        HashMap<String, ArrayList<String>> response = new HashMap<>();
        response.put("fileList", this.memory.getListFileNames());
        System.out.println("Files: " + this.memory.getListFileNames());
        out.writeObject(response);
    }
    public void handleDetailsRequest(HashMap<String,String> request) throws IOException {
        //get and return details from file
        String filename = request.get("filename");
        UploadedFile file = this.memory.getUploadedFile(filename);
        HashMap<String, UploadedFile> response = new HashMap<>();
        System.out.println("Peers with file " + file.getName()  + " : " + file.getUsersWithFile());

        response.put("details", file);
        out.writeObject(response);
    }

    public void handleUploadRequest(HashMap <String, String> request) throws IOException{
        String filename = request.get("filename");
        String username = request.get("username");

        UploadedFile f = this.memory.getUploadedFile(filename);
        OnlineUser user = this.memory.getOnlineUser(username);


        HashMap<String, String> response = new HashMap<>();

        if (f == null || user == null){
            response.put("message", "Failure");
        }else{
            f.getUsersWithFile().add(user); //TODO: check if already uploaded
            System.out.println("Peer " + username + " has file " + filename);
            response.put("message", "Success");
        }

        out.writeObject(response);

    }

    public void updateDownloadCount(HashMap <String, String> request) throws IOException{
        String username = request.get("username");
        HashMap<String, String> response = new HashMap<>();

        if (this.memory.getOnlineUser(username) != null){
            this.memory.increaseCountDownloads(username);
            System.out.println("Updated download count for peer " + username);
            response.put("message", "Updated download count for peer " + username);
        } else {
            response.put("message", "No user with this username");
        }

        out.writeObject(response);

    }
    public void updateFailureCount(HashMap <String, String> request) throws IOException{
        String username = request.get("username");
        HashMap<String, String> response = new HashMap<>();

        if (this.memory.getOnlineUser(username) != null){
            this.memory.increaseCountFailures(username);
            System.out.println("Updated failure count for peer " + username);
            response.put("message", "Updated failure count for peer " + username);
        } else {
            response.put("message", "No user with this username");
        }

        out.writeObject(response);
    }
}
